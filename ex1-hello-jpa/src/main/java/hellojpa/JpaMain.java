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

//            int pageNumber = 1;
//            int pageSize = 10;

//            List<Member> memberList = em.createQuery("select m from Member m", Member.class)
//                    .setFirstResult((pageNumber-1) * pageSize)
//                    .setMaxResults(pageSize)
//                    .getResultList();

//            System.out.println("=========================================");

//            for (Member member : memberList) {
//                System.out.println("member.name = " + member.getName());
//            }


            //비영속 상태
//            Member member = new Member();
//            member.setId(104L);
//            member.setName("HelloJPA");
            //여기까지 비영속 상태

//            System.out.println("BEFORE");
            //영속 상태
//            em.persist(member);
//            System.out.println("AFTER");

            //영속성 분리, 준영속 상태
//            em.detach(member);

            //객체 조회
//            Member findMember1 = em.find(Member.class, 104L);
//            Member findMember2 = em.find(Member.class, 104L);
            //객체를 삭제한 상태
//            em.remove(findMember);

//            System.out.println("findMember1.id = " + findMember1.getId());
//            System.out.println("findMember1.name = " + findMember1.getName());
//            System.out.println("==============================================");
//            System.out.println("findMember2.id = " + findMember2.getId());
//            System.out.println("findMember2.name = " + findMember2.getName());

//            System.out.println("findMember1 == findMember2 : " + (findMember1 == findMember2)); //영속성 컨텍스트에 저장되어있는 있는것을 바라보므로 동일한 객체이다

//            Member member1 = new Member(150L, "A");
//            Member member2 = new Member(160L, "B");
//            em.persist(member1);
//            em.persist(member2);

            Member findMember = em.find(Member.class, 150L);
            findMember.setName("ZZZZZZZ"); //update

            System.out.println("findMember.name = " + findMember.getName());
            System.out.println("==========================");

            tx.commit();

            System.out.println("FINISH");
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
