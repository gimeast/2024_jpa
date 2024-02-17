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
            member.setAge(10);
            em.persist(member);

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

            List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.name, m.age) from Member m", MemberDTO.class).getResultList();
            MemberDTO memberDTO = result.get(0);

            System.out.println("memberDTO.getName() = " + memberDTO.getName());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());

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
