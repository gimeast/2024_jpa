package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

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

    @Test
    @DisplayName("동적 쿼리와 성능 최적화 조회 - Builder 사용")
    void searchTest() {
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

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

//        List<MemberTeamDto> memberTeamDtos = memberJpaRepository.searchByBuilder(condition);
        List<MemberTeamDto> memberTeamDtos = memberJpaRepository.searchByWhere(condition);

        assertThat(memberTeamDtos).extracting("username").containsExactly("member4");
        assertThat(memberTeamDtos).extracting("teamName").containsExactly("teamB");
    }

}