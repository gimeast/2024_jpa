package jpabook.rejpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        //query 1번 -> N개 (1+N 문제 발생)
        List<OrderQueryDto> result = findOrders();

        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return result;
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select " +
                        "new jpabook.rejpashop.repository.order.query.OrderQueryDto" +
                        "(o.id, m.name, o.orderDate, o.status, d.address) " +
                        " from Order o " +
                        " join o.member m " +
                        " join o.delivery d", OrderQueryDto.class
        ).getResultList();
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select " +
                                "new jpabook.rejpashop.repository.order.query.OrderItemQueryDto" +
                                "(oi.order.id, oi.item.name, oi.orderPrice, oi.count) " +
                                " from OrderItem oi " +
                                " join oi.item i " +
                                " where oi.order.id = :orderId", OrderItemQueryDto.class
                )
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> orders = findOrders();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(orders));

        orders.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return orders;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select " +
                                "new jpabook.rejpashop.repository.order.query.OrderItemQueryDto" +
                                "(oi.order.id, oi.item.name, oi.orderPrice, oi.count) " +
                                " from OrderItem oi " +
                                " join oi.item i " +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class
                )
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
        return orderItemMap;
    }

    private static List<Long> toOrderIds(List<OrderQueryDto> orders) {
        List<Long> orderIds = orders.stream()
                .map(OrderQueryDto::getOrderId)
                .toList();
        return orderIds;
    }
}
