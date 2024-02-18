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
                Team team = new Team();
                team.setName("teamA");
                em.persist(team);

                Team teamB = new Team();
                teamB.setName("teamB");
                em.persist(teamB);

                Member member1 = new Member();
                member1.setName("memberA");
                member1.setAge(20);
                member1.setMemberType(MemberType.USER);
//                member.setMemberType(MemberType.ADMIN);
                member1.setTeam(team);
                em.persist(member1);

                Member member2 = new Member();
                member2.setName("memberB");
                member2.setAge(30);
                member2.setMemberType(MemberType.ADMIN);
                member2.setTeam(team);
                em.persist(member2);

                Member member3 = new Member();
                member3.setName("memberC");
                member3.setAge(40);
                member3.setMemberType(MemberType.ADMIN);
                member3.setTeam(teamB);
                em.persist(member3);

                Member member4 = new Member();
                member4.setName("memberD");
                member4.setAge(40);
                member4.setMemberType(MemberType.ADMIN);
                em.persist(member4);

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
//            String query = "select function('group_concat', m.name) from Member m";

            //경로 표현식 특징
//            String query = "select m.name from Member m"; //상태필드: 경로 탐색의 끝, 탐색X
//            String query = "select m.team from Member m"; //단일 값 연관 경로: 묵시적 내부 조인 발생, 탐색O
//            String query = "select t.members.name from Team t"; //이러한 JPQL은 불가능하다. 아래와 같은 명시적 조인을 쓰자!
//            String query = "select m.name from Team t join t.members m";
            String query = "select m from Member m left join fetch m.team";

//            Integer result = em.createQuery(query, Integer.class)
//            List<Integer> result = em.createQuery(query, Integer.class)
//            List<String> result = em.createQuery(query, String.class)
            List<Member> result = em.createQuery(query, Member.class)
//                    .setParameter("memberType", MemberType.ADMIN)
//                    .getResultList();
                    .getResultList();

            System.out.println("====================================");
            for (Member member : result) {
                if (member.getTeam() == null) {
                    System.out.println("member = " + member.getName() + " : " + member.getTeam());
                } else {
                    System.out.println("member = " + member.getName() + " : " + member.getTeam().getName());
                }
            }
            //            for (String s : result) {
//            for (Member s : result) {
//                System.out.println("s = " + s);
//            }
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
