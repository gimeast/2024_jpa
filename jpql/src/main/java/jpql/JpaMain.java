package jpql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.OrderColumn;
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
//                Team team = new Team();
//                team.setName("team"+i);
//                em.persist(team);

                Member member = new Member();
                member.setName("memberA");
                member.setAge(20);
                member.setMemberType(MemberType.USER);
//                member.setMemberType(MemberType.ADMIN);
//                member.setTeam(team);
                em.persist(member);
                Member member2 = new Member();
                member2.setName("memberB");
                member2.setAge(30);
                member2.setMemberType(MemberType.ADMIN);
                em.persist(member2);

//                Team team = new Team();
//                team.setName("teamA");

//                member.changeTeam(team);

//                em.persist(team);

//            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);//반환 타입이 명확 할 때는 TypeQuery
//            Query query2 = em.createQuery("select m.id, m.name from Member m"); //반환 타입이 명확 하지 않을 때는 Query

            em.flush();
            em.clear();

//            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
//            List<Member> resultList = query.getResultList();
//            for (Member member1 : resultList) {
//                System.out.println(member1);
//            }
//            TypedQuery<Member> query1 = em.createQuery("select m from Member m where m.id = 1", Member.class);
//            Member singleResult = query1.getSingleResult(); //값이 딱 하나만 있다고 보장될때 사용해야한다 안그러면 exception이 발생한다.
//            System.out.println("singleResult = " + singleResult);

//            Member result = em.createQuery("select m from Member m where m.name = :name", Member.class)
//                    .setParameter("name", "member1")
//                    .getSingleResult();

//            System.out.println("result = " + result.getName());

//            List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();
//            Member findMember = result.get(0);
//            findMember.setAge(20);
//            findMember.setName("member2");

            //엔티티 프로젝션
//            List<Team> result = em.createQuery("select m.team from Member m", Team.class).getResultList(); //JOIN된 쿼리가 나간다.
//            List<Team> result = em.createQuery("select t from Member m join m.team t", Team.class).getResultList(); //되도록 이렇게 한눈에 알아볼 수 있도록 사용하자

            //임베디드 프로젝션
//            List<Address> result = em.createQuery("select o.address from Order o", Address.class).getResultList();

            //스칼라 프로젝션
//            List<Object[]> resultList = em.createQuery("select m.name, m.age from Member m").getResultList();
//            for (Object o : resultList) {
//                Object[] result = (Object[]) o;
//                System.out.println("result[0] = " + result[0]);
//                System.out.println("result[1] = " + result[1]);
//            }

//            List<Object[]> resultList = em.createQuery("select m.name, m.age from Member m").getResultList();
//            System.out.println("resultList.get(0)[0] = " + resultList.get(0)[0]);
//            System.out.println("resultList.get(0)[0] = " + resultList.get(0)[0]);

//            List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.name, m.age) from Member m", MemberDTO.class).getResultList();
//            MemberDTO memberDTO = result.get(0);

//            System.out.println("memberDTO.getName() = " + memberDTO.getName());
//            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());

//            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
//                    .setFirstResult(0)
//                    .setMaxResults(10)
//                    .getResultList();

//            for (Member member1 : result) {
//                System.out.println("member1 = " + member1);
//            }

            String query1 = "select m from Member m inner join m.team t";
            String query2 = "select m from Member m, Team t where m.name = t.name"; //세타조인 : 카티션곱 발생.
            String query3 = "select m from Member m left join m.team t on t.name = 'teamA'";
            String query4 = "select m from Member m left join Team t on m.name = t.name"; //연관관계 없는 엔티티 외부 조인

//            String query = "select (select avg(m1.age) from Member m1) as avgAge from Member m inner join m.team t"; //select 절 서브쿼리 예제
//            String query = "select m from Member m where m.memberType = :memberType";
//            String query = " select " +
//                            " case when m.age <= 10 then '학생요금' " +
//                                " when m.age >= 60 then '경로요금' " +
//                                " else '일반요금' " +
//                            " end " +
//                            " from Member m ";
//            String query = "select coalesce(m.name, '이름 없는 회원') from Member m"; //member name이 null이면 '이름 없는 회원' 반환
//            String query = "select nullif(m.name, 'memberA') from Member m"; //member name이 memberA면 null을 반환
//            String query = "select concat('a','b') from Member m";
//            String query = "select length(substring(m.name, 2,3)) from Member m";
//            String query = "select locate('de', 'abcdefg') from Member m ";
//            String query = "select size(t.members) from Team t";
            String query = "select function('group_concat', m.name) from Member m";
//            List<Integer> result = em.createQuery(query, Integer.class)
            List<String> result = em.createQuery(query, String.class)
//            List<Member> result = em.createQuery(query, Member.class)
//                    .setParameter("memberType", MemberType.ADMIN)
                    .getResultList();

            System.out.println("====================================");
            System.out.println("result = " + result);
            for (String s : result) {
                System.out.println("s = " + s);
            }
            System.out.println("====================================");

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
