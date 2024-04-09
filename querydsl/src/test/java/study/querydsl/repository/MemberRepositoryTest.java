package study.querydsl.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("1. 순수 JPA 리포지토리 test")
    void basicTest() throws Exception {
        //given
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        //when
        Optional<Member> findMember = memberRepository.findById(member.getId());

        //then
        Member actualMember = findMember.orElseThrow(() -> new Exception("해당 id를 가진 사용자가 존재하지 않습니다."));
        assertThat(actualMember).isEqualTo(member);
        assertThat(actualMember.getUsername()).isEqualTo("member1");
        assertThat(actualMember.getAge()).isEqualTo(10);

        List<Member> result1 = memberRepository.findAll();
        assertThat(result1).containsExactly(member);

        List<Member> result2 = memberRepository.findByUsername("member1");
        assertThat(result2).containsExactly(member);

    }

    @Test
    @DisplayName("2. Querydsl 리포지토리 test")
    void querydslTest() throws Exception {
        //given
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        //when
        Optional<Member> findMember = memberRepository.findById(member.getId());

        //then
        Member actualMember = findMember.orElseThrow(() -> new Exception("해당 id를 가진 사용자가 존재하지 않습니다."));
        assertThat(actualMember).isEqualTo(member);
        assertThat(actualMember.getUsername()).isEqualTo("member1");
        assertThat(actualMember.getAge()).isEqualTo(10);

        List<Member> result1 = memberRepository.findAll();
        assertThat(result1).containsExactly(member);

        List<Member> result2 = memberRepository.findByUsername("member1");
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
        List<MemberTeamDto> memberTeamDtos = memberRepository.searchByWhere(condition);

        assertThat(memberTeamDtos).extracting("username").containsExactly("member4");
        assertThat(memberTeamDtos).extracting("teamName").containsExactly("teamB");
    }
    
    @Test
    @DisplayName("스프링 데이터 페이징 활용1 - Querydsl 페이징 연동")
    void searchPageSimple() {
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
//        condition.setAgeGoe(35);
//        condition.setAgeLoe(40);
//        condition.setTeamName("teamB");

        PageRequest pageRequest = PageRequest.of(0, 3);

//        List<MemberTeamDto> memberTeamDtos = memberJpaRepository.searchByBuilder(condition);
//        Page<MemberTeamDto> result = memberRepository.searchPageSimple(condition, pageRequest);
        Page<MemberTeamDto> result = memberRepository.searchPageComplex(condition, pageRequest);

        assertThat(result.getSize()).isEqualTo(3);
        assertThat(result.getContent()).extracting("username").containsExactly("member1", "member2", "member3");
        
    }



}