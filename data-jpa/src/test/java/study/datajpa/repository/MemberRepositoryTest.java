package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.exception.MemberNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    
    @Test
    @DisplayName("Spring data JPA를 이용한 member 생성 및 조회")
    @Rollback(value = false)
    void testMember() {
        Member member = new Member("userB", 10, null);
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

    @Test
    @DisplayName("CRUD 테스트")
    @Rollback(value = false)
    void basicCRUD() {
        //given
        Member member1 = new Member("member1", 20, null);
        Member member2 = new Member("member2", 30, null);
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        //then
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        assertThat(all).contains(member1, member2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long finalCount = memberRepository.count();
        assertThat(finalCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThan() {
        //given
        Member member1 = new Member("member_A", 20, null);
        Member member2 = new Member("member_A", 30, null);
        Member member3 = new Member("member_B", 40, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> findMembers = memberRepository.findByUsernameAndAgeGreaterThan("member_A", 23);

        //then
        assertThat(findMembers.size()).isEqualTo(1);
    }

    @Test
    void findWowBy () {
        memberRepository.findWowBy();
    }

}