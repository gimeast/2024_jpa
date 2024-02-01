package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
//            Member member = new Member();
//            member.setId(2L);
//            member.setName("TesterB");

//            em.persist(member); //등록


//            Member member = em.find(Member.class, 1L);
//            System.out.println("member.getId: " + member.getId());
//            System.out.println("member.getId: " + member.getName());

//            em.remove(member); //삭제


//            Member member = em.find(Member.class, 2L);
//            member.setName("HelloJPA"); //수정

            int pageNumber = 1;
            int pageSize = 10;

            List<Member> memberList = em.createQuery("select m from Member m", Member.class)
                    .setFirstResult((pageNumber-1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();

            System.out.println("=========================================");

            for (Member member : memberList) {
                System.out.println("member.name = " + member.getName());
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
