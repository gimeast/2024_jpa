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

            Member member = new Member();
            member.setUsername("member1");

            Locker locker = new Locker();
            locker.setName("my Locker");
            em.persist(locker);

            member.setLocker(locker);
            em.persist(member);

            Team team = new Team();
            team.setName("teamA");
            team.getMembers().add(member);

            em.persist(team);

            em.flush();
            em.clear();

            Locker findLocker = em.find(Locker.class, locker.getId());
            System.out.println("=============================");
            System.out.println(findLocker.getMember().getUsername());
            System.out.println("=============================");

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
