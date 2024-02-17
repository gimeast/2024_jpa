package jpql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setName("member1");
            em.persist(member);

//            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);//반환 타입이 명확 할 때는 TypeQuery
//            Query query2 = em.createQuery("select m.id, m.name from Member m"); //반환 타입이 명확 하지 않을 때는 Query

            em.flush();
            em.clear();

            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
            List<Member> resultList = query.getResultList();
            for (Member member1 : resultList) {
                System.out.println(member1);
            }
            TypedQuery<Member> query1 = em.createQuery("select m from Member m where m.id = 1", Member.class);
            Member singleResult = query1.getSingleResult(); //값이 딱 하나만 있다고 보장될때 사용해야한다 안그러면 exception이 발생한다.
            System.out.println("singleResult = " + singleResult);

            Member result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                    .setParameter("name", "member1")
                    .getSingleResult();

            System.out.println("result = " + result.getName());

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
