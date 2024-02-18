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

//            String query1 = "select m from Member m inner join m.team t";
//            String query2 = "select m from Member m, Team t where m.name = t.name"; //세타조인 : 카티션곱 발생.
//            String query3 = "select m from Member m left join m.team t on t.name = 'teamA'";
//            String query4 = "select m from Member m left join Team t on m.name = t.name"; //연관관계 없는 엔티티 외부 조인

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
//            String query = "select m from Member m join fetch m.team";

            /*
            애플리케이션에서 fetch join의 결과는 연관된 모든 엔티티가 있을것이라 가정하고 사용해야 합니다.
            이렇게 페치 조인에 별칭을 잘못 사용해서 컬렉션 결과를 필터링 해버리면, 객체의 상태와 DB의 상태 일관성이 깨지는 것이다.
            결론: fetch join의 대상은 on, where 등에서 필터링 조건으로 사용하면 안된다.
            그리고 fetch join의 대상이 아닌 엔티티는 where 문을 사용해도 된다.
            둘 이상의 컬렉션은 페치 조인 할 수 없다.
            *******또한 JPA의 설계 사상 자체가 객체 그래프를 탐색한다는 것은 Team.members가 모두 조회된다는것을 가정하고 설계 되어있다.*******
            만약 cascade 같은 것들이 적용되어있다면 나머지 데이터가 지워지는 현상이 발생할 수 있고 이상하게 동작 할 수 있다!!
             */
            String query1 = "select t from Team t join fetch t.members"; //일대다는 페이징 불가능!
            String query2 = "select m from Member m join fetch m.team"; //다대일은 페이징 가능! (Best)
            String query3 = "select t from Team t"; //페이징 성능 안좋음, batchsize를 적용하면

//            Integer result = em.createQuery(query, Integer.class)
//            List<Integer> result = em.createQuery(query, Integer.class)
//            List<String> result = em.createQuery(query, String.class)
//            List<Member> result = em.createQuery(query2, Member.class)
            List<Team> result = em.createQuery(query3, Team.class)
//                    .setParameter("memberType", MemberType.ADMIN)
//                    .getResultList();
                    .setFirstResult(0)
                    .setMaxResults(1)
                    .getResultList();

            System.out.println("====================================");
//            for (Member m : result) {
//                System.out.println("m = " + m);

            for (Team t : result) {
                System.out.println("team = " + t.getName() + "|" + t.getMembers().size());
                for (Member m : t.getMembers()) {
                    System.out.println("-->member = " + m);
                }
            
//                System.out.println("member = " + member.getName() + " : " + member.getTeam());
//                System.out.println("member = " + member.getName() + " : " + member.getTeam().getName());
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
