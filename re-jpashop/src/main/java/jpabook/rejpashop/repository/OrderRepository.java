package jpabook.rejpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jpabook.rejpashop.domain.Order;
import jpabook.rejpashop.domain.OrderStatus;
import jpabook.rejpashop.domain.QMember;
import jpabook.rejpashop.domain.QOrder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrderRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return query.select(order)
                .from(order)
                .join(order.member, member)
                //where절의 특징으로 콤마(,)를 사용하면 and 조건으로 처리 된다. 만약 null이면 해당 조건은 제외 된다.
                .where(statusEq(orderSearch.getOrderStatus()), (nameLike(orderSearch.getMemberName())))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        if (statusCond == null) {
            return null;
        }
        return QOrder.order.status.eq(statusCond);
    }

    private BooleanExpression nameLike(String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }
        return QMember.member.name.like(name);
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {

        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

        /**
         * @Method         : findAllByCriteria
         * @Description    : Cirteria를 이용한 검색 조회
         * @Author         : gimeast
         * @Date           : 2024. 03. 03.
         * @params         : orderSearch
         * @return         : List
         */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);

        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        if (orderSearch.getMemberName() != null) {
            Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq);

        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o " +
                        " join fetch o.member m " +
                        " join fetch o.delivery d", Order.class
                ).getResultList();
    }

    public List<Order> findAllWithItem() {
        //쿼리에서는 총 4개의 데이터를 가져온다.
        //포스트 맨으로 확인시 2개의 데이터를 가져오는데 그 이유는
        //하이버네이트6 부터 데이터가 자동으로 distinct 해주기 때문이다.

        //주의:
        //     컬렉션 페치조인 (1대다 관계를 조인하는 경우 ex-> o.orderItems oi)은
        //     페이징이 불가능 하다.
        //     컬렉션 페치 조인은 1개만 사용할 수 있다. -> 데이터가 부정합하게 조회될 수 있다.
        return
                em.createQuery("select distinct o from Order o " +
                        " join fetch o.member m " +
                        " join fetch o.delivery d " +
                        " join fetch o.orderItems oi " +
                        " join fetch oi.item i ", Order.class)
                        .setFirstResult(1)
                        .setMaxResults(100)
                        .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                        "select o from Order o " +
                                " join fetch o.member m " +
                                " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
