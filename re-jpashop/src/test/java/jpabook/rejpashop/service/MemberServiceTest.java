package jpabook.rejpashop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.rejpashop.domain.Member;
import jpabook.rejpashop.repository.MemberRepositoryOld;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepositoryOld memberRepositoryOld;
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("회원가입")
    void join() {
        //given
        Member member = new Member();
        member.setName("kim");
        
        //when
        Long memberId = memberService.join(member);

        //then
        assertEquals(member, memberRepositoryOld.findOne(memberId));

    }

    @Test
    @DisplayName("중복회원예외")
    void duplicateMemberException() {
        //given
        Member member1 = new Member();
        member1.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1");

        //when
        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member1);
            memberService.join(member2);
        });


    }
    
}