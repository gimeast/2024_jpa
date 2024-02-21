package jpabook.rejpashop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("저장")
    @Transactional
    @Rollback(value = false)
    void save() {
        //given
        Member member = new Member();
        member.setUsername("홍길동");

        //when
        Long id = memberRepository.save(member);
        Member findMember = memberRepository.find(id);

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        //true => equals와 hashcode를 생성하지 않는다면 같은 영속성 컨텍스트 안에서 같은 식별자값이 같으면 1차 캐시에 있는 엔티티를 바라보게되어 true가 나온다.
        assertThat(findMember).isEqualTo(member);
        System.out.println("===================================================");
        System.out.println("findMember: " + findMember + ", member: " + member);
        System.out.println("===================================================");
    }

}