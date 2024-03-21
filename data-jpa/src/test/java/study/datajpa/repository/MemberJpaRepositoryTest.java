package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;
    
    @Test
    @DisplayName("JPA를 이용한 member 생성 및 조회")
    @Rollback(value = false)
    void testMember() {
        //given
        Member member = new Member("userA", 10, null);
        Member savedMember = memberJpaRepository.save(member);
        //when
        Member findMember = memberJpaRepository.find(savedMember.getId());
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
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        //when
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        //then
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        assertThat(all).contains(member1, member2);

        //카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long finalCount = memberJpaRepository.count();
        assertThat(finalCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThan() {
        //given
        Member member1 = new Member("member_A", 20, null);
        Member member2 = new Member("member_A", 30, null);
        Member member3 = new Member("member_B", 40, null);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //when
        List<Member> findMembers = memberJpaRepository.findByUsernameAndAgeGreaterThan("member_A", 23);

        //then
        assertThat(findMembers.size()).isEqualTo(1);

    }
}