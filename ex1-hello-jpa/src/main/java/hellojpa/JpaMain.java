package hellojpa;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member1 = new Member();
            member1.setUsername("사용자1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("사용자2");
            em.persist(member2);


            em.flush();
            em.clear();

            Member m1 = em.find(Member.class, member1.getId());
//            Member m2 = em.find(Member.class, member1.getId());

//            System.out.println(m1 == m2); //true
//            System.out.println(m1.getClass() == m2.getClass()); //true

            Member m2ref = em.getReference(Member.class, member2.getId());
            System.out.println(m1.getClass() == m2ref.getClass()); //false

            System.out.println(m1 instanceof Member); //true
            System.out.println(m2ref instanceof Member); //true

            Member m1ref = em.getReference(Member.class, member1.getId());
            System.out.println(m1ref.getClass());
            System.out.println(m2ref.getClass());

            em.flush();
            em.clear();

            Member refMember = em.getReference(Member.class, member1.getId());
            System.out.println(refMember.getClass());
//            refMember.getUsername();
            Hibernate.initialize(refMember); //강제초기화
            System.out.println("isLodaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    private static void printMemberAndTeam(Member findMember) {
        String username = findMember.getUsername();
        System.out.println("username = " + username);

        Team team = findMember.getTeam();
        System.out.println("team = " + team.getName());

    }
}
