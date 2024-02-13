package hellojpa;

import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Address address = new Address("city", "street", "zipcode");

            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(address);
            em.persist(member);

            //값 타입의 실제 인스턴스인 값을 공유하는 것은 위험.
            //대신 값을 복사해서 사용.
            //객체 타입은 수정할 수 없게 만들어서 사이드 이펙트를 원천 차단해야한다.
            Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getStreet());

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setHomeAddress(copyAddress);
            em.persist(member2);

//            member.getHomeAddress().setCity("newCity");

            //setter를 없애고 난 상태에서 수정하고 싶다면 새로 생성해서 넣어줘야한다! 사이드 이펙트를 방지할 수 있다.
            Address newAddress = new Address("newCity", "street", "zipcode");

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setHomeAddress(newAddress);
            em.persist(member3);

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}
