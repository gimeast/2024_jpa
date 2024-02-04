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

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.changeTeam(team); //**관례상 setTeam보다는 새로운 연관관계 편의 메소드를 생성하는게 좋다.
            em.persist(member);

//            team.getMembers().add(member); //**양방향 연관관계인 경우 양쪽에 값을 셋팅 해주는게 맞다. (연관관계 편의 메소드를 이용하자.)

//            em.flush();
//            em.clear();

            Team findTeam = em.find(Team.class, team.getId()); //*em.flush()와 em.clear()를 주석하게 되면 1차캐시에 있는 값을 가지고온다.
            List<Member> members = findTeam.getMembers(); //*1차 캐시에는 findTeam에 members가 비어있다.

            System.out.println("==========================");
            for (Member m : members) {
                System.out.println("m = " + m.getUsername());
            }
            System.out.println("==========================");

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
