package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.exception.MemberNotFoundException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    
    @Test
    @DisplayName("Spring data JPA를 이용한 member 생성 및 조회")
    @Rollback(value = false)
    void testMember() {
        Member member = new Member("userB");
        Member savedMember = memberRepository.save(member);
        //when

        Member findMember = memberRepository.findById(savedMember.getId())
                .orElseThrow(() -> {
                    throw new MemberNotFoundException("존재하지 않는 member id 입니다");
                });

        //then
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(member);
    }
    
}