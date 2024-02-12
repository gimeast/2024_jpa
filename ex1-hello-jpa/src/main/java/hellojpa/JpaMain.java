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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Team teamB = new Team();
            team.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("사용자1");
            member1.setTeam(team);
            em.persist(member1);
            
            Member member2 = new Member();
            member2.setUsername("사용자2");
            member2.setTeam(teamB);
            em.persist(member2);

            em.flush();
            em.clear();

            //fetch가 EAGER인 경우 N+1 문제를 일으킨다.
            System.out.println("============================================");
            List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class).getResultList();
            members.stream().forEach(member -> System.out.println(member.getUsername()));
            System.out.println("============================================");
            //SQL: select * from member
            //SQL: select * from team where team_id = xxx

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
