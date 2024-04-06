package study.querydsl.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.querydsl.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;
    
    @Test
    @DisplayName("1. 순수 JPA 리포지토리 test")
    void basicTest() throws Exception {
        //given
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        //when
        Optional<Member> findMember = memberJpaRepository.findById(member.getId());

        //then
        Member actualMember = findMember.orElseThrow(() -> new Exception("해당 id를 가진 사용자가 존재하지 않습니다."));
        assertThat(actualMember).isEqualTo(member);
        assertThat(actualMember.getUsername()).isEqualTo("member1");
        assertThat(actualMember.getAge()).isEqualTo(10);

        List<Member> result1 = memberJpaRepository.findAll();
        assertThat(result1).containsExactly(member);

        List<Member> result2 = memberJpaRepository.findByUsername("member1");
        assertThat(result2).containsExactly(member);

    }

    @Test
    @DisplayName("2. Querydsl 리포지토리 test")
    void querydslTest() throws Exception {
        //given
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        //when
        Optional<Member> findMember = memberJpaRepository.findById(member.getId());

        //then
        Member actualMember = findMember.orElseThrow(() -> new Exception("해당 id를 가진 사용자가 존재하지 않습니다."));
        assertThat(actualMember).isEqualTo(member);
        assertThat(actualMember.getUsername()).isEqualTo("member1");
        assertThat(actualMember.getAge()).isEqualTo(10);

        List<Member> result1 = memberJpaRepository.findAllByQuerydsl();
        assertThat(result1).containsExactly(member);

        List<Member> result2 = memberJpaRepository.findByUsernameByQuerydsl("member1");
        assertThat(result2).containsExactly(member);

    }

}