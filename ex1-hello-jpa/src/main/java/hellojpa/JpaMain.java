package hellojpa;

import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member1 = new Member();
            member1.setUsername("A");
            Member member2 = new Member();
            member2.setUsername("B");
            Member member3 = new Member();
            member3.setUsername("C");

            System.out.println("111111111111111111");

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            System.out.println("member1.id = "+member1.getId());
            System.out.println("member2.id = "+member2.getId());
            System.out.println("member3.id = "+member3.getId());

            System.out.println("222222222222222222");
            tx.commit();
            System.out.println("333333333333333333");

            System.out.println("FINISH");
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
