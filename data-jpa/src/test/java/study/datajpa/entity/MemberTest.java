package study.datajpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {
    
    @PersistenceContext
    EntityManager em;

    @Autowired
    private MemberRepository memberRepository;
    
    @Test
    @DisplayName("team과 member 연결")
    @Rollback(value = false)
    void testEntity() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        
        em.flush();
        em.clear();
        //when
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        //then
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam() = " + member.getTeam());
        }
        
    }
    
    @Test
    @DisplayName("MappedSuperclass를 이용한 Auditing")
    @Rollback(value = false)
    void JpaEventBaseEntity() throws Exception {
        //given
        Member member = new Member("member1");
        memberRepository.save(member); //@PrePersist 작동

        Thread.sleep(3000);
        member.changeUserName("member2");

        em.flush(); //@PreUpdate
        em.clear();
        //when
        Member findMember = memberRepository.findById(member.getId()).get();
        //then
        System.out.println("findMember.getCreatedDate() = " + findMember.getCreatedDate());
        System.out.println("findMember.getUpdatedDate() = " + findMember.getLastModifiedDate());
        System.out.println("findMember.getCreatedBy() = " + findMember.getCreatedBy());
        System.out.println("findMember.getUpdatedBy() = " + findMember.getLastModifiedBy());
    }

}