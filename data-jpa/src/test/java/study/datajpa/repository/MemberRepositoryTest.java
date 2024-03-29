package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.exception.MemberNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MemberQueryRepository memberQueryRepository;
    
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

    @Test
    @DisplayName("JPA NamedQuery 테스트")
    void testNamedQuery() {
        //given
        Member member1 = new Member("member_A", 20, null);
        Member member2 = new Member("member_B", 30, null);
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        List<Member> memberB = memberRepository.findByUsernameNamedQuery("member_B");
        //then
        assertThat(memberB.get(0)).isEqualTo(member2);
    }

    @Test
    @DisplayName("JPA NamedQuery 테스트")
    void testQuery() {
        //given
        Member member1 = new Member("member_A", 20, null);
        Member member2 = new Member("member_B", 30, null);
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        List<Member> memberB = memberRepository.findByUser("member_B", 30);
        //then
        assertThat(memberB.get(0)).isEqualTo(member2);
    }

    @Test
    @DisplayName("@Query로 값 조회하기")
    void testQuery2() {
        //given
        Member member1 = new Member("member_A", 20, null);
        Member member2 = new Member("member_B", 30, null);
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        List<String> username = memberRepository.findByUsernameList();
        //then
        username.forEach(System.out::println);
    }

    @Test
    @DisplayName("jpql에 new Operator 사용하여 dto로 바로 조회하기")
    @Rollback(value = false)
    void testQuery3() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.saveAll(Arrays.asList(teamA, teamB));

        Member member1 = new Member("member_A", 20, teamA);
        Member member2 = new Member("member_B", 30, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        //when
        List<MemberDto> username = memberRepository.findMemberDto();
        //then
        username.forEach(System.out::println);
    }

    @Test
    @DisplayName("jpql에서 in절 예제")
    @Rollback(value = false)
    void testQuery4() {
        //given
        Member member1 = new Member("member_A", 20, null);
        Member member2 = new Member("member_B", 30, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> findMember = memberRepository.findByNames(Arrays.asList("member_A", "member_B"));
        //then
        findMember.forEach(System.out::println);
    }

    @Test
    @DisplayName("여러 반환타입 테스트")
    @Rollback(value = false)
    void returnType() {
        //given
        Member member1 = new Member("member_A", 20, null);
        Member member2 = new Member("member_B", 30, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> findListMember = memberRepository.findListByUsername("member_A");
        Member findMember = memberRepository.findMemberByUsername("member_B");
        Optional<Member> findOptionalMember = memberRepository.findOptionalByUsername("member_B");
        //then
        findListMember.forEach(System.out::println);
        System.out.println("findMember = " + findMember);
        System.out.println("findOptionalMember = " + findOptionalMember.get());
    }

    @Test
    @DisplayName("Spring data JPA를 이용한 페이징")
    void testPaging() {
        //given
        for (int i = 0; i < 54; i++) {
            Member member1 = new Member("member_"+i, 20+i, null);
            Member member2 = new Member("AAA"+i, 10, null);
            memberRepository.save(member1);
            memberRepository.save(member2);
        }

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> memberPage = memberRepository.findPageByAge(age, pageRequest);

        //then
        System.out.println("===================페이지================");
        List<Member> content = memberPage.getContent();

        long totalElements = memberPage.getTotalElements();
        int totalPages = memberPage.getTotalPages();
        int numberOfElements = memberPage.getNumberOfElements();
        int number = memberPage.getNumber();
        int size = memberPage.getSize();
        boolean first = memberPage.isFirst();
        boolean last = memberPage.isLast();
        boolean hasPrev = memberPage.hasPrevious();
        boolean hasNext = memberPage.hasNext();

        System.out.println("content = " + content);
        System.out.println("전체 페이지 데이터 수 = " + totalElements);
        System.out.println("총 페이지 수 = " + totalPages);
        System.out.println("조회한 페이지의 데이터 수 = " + numberOfElements);
        System.out.println("현재 페이지 번호 = " + number);
        System.out.println("페이지당 나타낼 수 있는 데이터 수 = " + size);
        System.out.println("첫 페이지 여부 = " + first);
        System.out.println("마지막 페이지 여부 = " + last);
        System.out.println("이전 페이지 존재 여부 = " + hasPrev);
        System.out.println("다음 페이지 존재 여부 = " + hasNext);
        System.out.println("============================================");
    }

    @Test
    @DisplayName("Spring data JPA를 이용한 slice")
    void testSlicePaging() {
        //given
        for (int i = 0; i < 54; i++) {
            Member member1 = new Member("member_"+i, 20+i, null);
            Member member2 = new Member("AAA"+i, 10, null);
            memberRepository.save(member1);
            memberRepository.save(member2);
        }

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Slice<Member> memberSlice = memberRepository.findSliceByAge(age, pageRequest);

        //then
        System.out.println("=================슬라이스=================");

        List<Member> sliceContent = memberSlice.getContent();
//        memberSlice.getTotalElements();
//        memberSlice.getTotalPages();
        int sliceNumberOfElements = memberSlice.getNumberOfElements();
        int sliceNumber = memberSlice.getNumber();
        int sliceSize = memberSlice.getSize();
        boolean sliceFirst = memberSlice.isFirst();
        boolean sliceLast = memberSlice.isLast();
        boolean sliceHasPrevious = memberSlice.hasPrevious();
        boolean sliceHasNext = memberSlice.hasNext();

        System.out.println("sliceContent = " + sliceContent);
        System.out.println("sliceNumberOfElements = " + sliceNumberOfElements);
        System.out.println("sliceNumber = " + sliceNumber);
        System.out.println("sliceSize = " + sliceSize);
        System.out.println("sliceFirst = " + sliceFirst);
        System.out.println("sliceLast = " + sliceLast);
        System.out.println("sliceHasPrevious = " + sliceHasPrevious);
        System.out.println("sliceHasNext = " + sliceHasNext);
        System.out.println("============================================");
    }

    @Test
    @DisplayName("paging 반환을 dto로")
    void pagingResultToDto() {
        //given
        for (int i = 0; i < 54; i++) {
            Member member1 = new Member("member_"+i, 20+i, null);
            Member member2 = new Member("AAA"+i, 10, null);
            memberRepository.save(member1);
            memberRepository.save(member2);
        }

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> memberPage = memberRepository.findPageByAge(age, pageRequest);

        Page<MemberDto> memberDtoPage = memberPage.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        //then
        System.out.println("===================페이지================");
        List<MemberDto> content = memberDtoPage.getContent();

        long totalElements = memberDtoPage.getTotalElements();
        int totalPages = memberDtoPage.getTotalPages();
        int numberOfElements = memberDtoPage.getNumberOfElements();
        int number = memberDtoPage.getNumber();
        int size = memberDtoPage.getSize();
        boolean first = memberDtoPage.isFirst();
        boolean last = memberDtoPage.isLast();
        boolean hasPrev = memberDtoPage.hasPrevious();
        boolean hasNext = memberDtoPage.hasNext();

        System.out.println("content = " + content);
        System.out.println("전체 페이지 데이터 수 = " + totalElements);
        System.out.println("총 페이지 수 = " + totalPages);
        System.out.println("조회한 페이지의 데이터 수 = " + numberOfElements);
        System.out.println("현재 페이지 번호 = " + number);
        System.out.println("페이지당 나타낼 수 있는 데이터 수 = " + size);
        System.out.println("첫 페이지 여부 = " + first);
        System.out.println("마지막 페이지 여부 = " + last);
        System.out.println("이전 페이지 존재 여부 = " + hasPrev);
        System.out.println("다음 페이지 존재 여부 = " + hasNext);
        System.out.println("============================================");
    }

    @Test
    @DisplayName("spring data jpa 벌크성 수정 쿼리 테스트")
    @Rollback(value = false)
    void bulkUpdate() {
        //given
        Member member1 = new Member("AAA", 30, null);
        Member member2 = new Member("BBB", 10, null);
        Member member3 = new Member("CCC", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        //when
        int result = memberRepository.bulkAgePlus(15);
        //then
        assertThat(result).isEqualTo(2);

        Member findMember = memberRepository.findMemberByUsername("CCC");
        System.out.println("findMember = " + findMember);

    }

    @Test
    @DisplayName("spring data jpa 벌크성 삭제 쿼리 테스트")
    @Rollback(value = false)
    void bulkDelete() {
        //given
        Member member1 = new Member("AAA", 30, null);
        Member member2 = new Member("BBB", 10, null);
        Member member3 = new Member("CCC", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        //when
        int result = memberRepository.bulkAgeDelete(15);
        //then
        assertThat(result).isEqualTo(2);

        List<String> usernameList = memberRepository.findByUsernameList();
        System.out.println("usernameList = " + usernameList);

    }

    @Test
    @DisplayName("spring data jpa 등록 쿼리 테스트")
    @Rollback(value = false)
    void insertByQuery() {
        //given
        Member member1 = new Member("AAA", 30, null);
        Member member2 = new Member("BBB", 10, null);
        Member member3 = new Member("CCC", 20, null);
        memberRepository.insertMemberByQuery("DDD", 31);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<String> usernameList = memberRepository.findByUsernameList();
        System.out.println("usernameList = " + usernameList);
    }

    @Test
    @DisplayName("entity graph 예제")
    void entityGraphExample() {
        //given
        Team team1 = new Team("teamA");
        Team team2 = new Team("teamB");
        teamRepository.saveAll(Arrays.asList(team1, team2));

        Member member1 = new Member("AAA", 30, team1);
        Member member2 = new Member("BBB", 10, team2);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
//        List<Member> all = memberRepository.findAll();
//        List<Member> all = memberRepository.findMemberFetchJoin();
//        List<Member> all = memberRepository.findAll();
//        List<Member> all = memberRepository.findMemberEntityGraph();
        List<Member> all = memberRepository.findEntityGraphByUsername("AAA");

        //then
        for (Member member : all) {
            System.out.println("member = " + member);
            System.out.println("member.getClass() = " + member.getClass());
            System.out.println("member.getTeam() = " + member.getTeam());
        }
    }
    
    @Test
    @DisplayName("JPA Hint")
    @Rollback(value = false)
    void queryHint() {
        //given
        Member member = new Member("member1", 10, null);
        memberRepository.save(member);
        em.flush();
        em.clear();
        
        //when
        Member findMember = memberRepository.findById(member.getId()).get();
        findMember.changeUserName("member2");
        em.flush();
        em.clear();

        Member findMember2 = memberRepository.findReadOnlyByUsername("member2");
        findMember2.changeUserName("member3");
        em.flush();
        em.clear();

        System.out.println("findMember2 = " + findMember2);
    }

    @Test
    @DisplayName("JPA Lock")
    @Rollback(value = false)
    void queryLock() {
        //given
        Member member = new Member("member1", 10, null);
        memberRepository.save(member);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findLockByUsername("member1");

        System.out.println("findMember = " + findMember);
    }
    
    @Test
    @DisplayName("repository custom1")
    void callCustom1() {
        List<Member> result = memberRepository.findMemberCustom();
    }
    @Test
    @DisplayName("repository custom2")
    void callCustom2() {
        List<Member> result = memberQueryRepository.findAllMembers();
    }

    @Test
    @DisplayName("JPA의 Specifications (명세)")
    void specBasic() {
        //given
        Team team = new Team("teamA");
        em.persist(team);

        Member member1 = new Member("m1", 0, team);
        Member member2 = new Member("m2", 0, team);
        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();
        //when
        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);

        //then
        assertThat(result.size()).isEqualTo(1);
        System.out.println("result = " + result);

    }


    @Test
    @DisplayName("queryByExample")
    void queryByExample() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("m1", 0, teamA);
        Member member2 = new Member("m2", 0, teamA);
        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        //when
        //Probe
        Team team = new Team("teamA");
        Member member = new Member("m1", 0, team);


        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");

        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);

        //then
        assertThat(result.get(0).getUsername()).isEqualTo("m1");

    }

    @Test
    @DisplayName("Projections")
    void projections() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("m1", 0, teamA);
        Member member2 = new Member("m2", 0, teamA);
        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        //when
        List<UsernameOnly> m1 = memberRepository.findProjectionsByUsername("m1");
        List<UsernameOnlyDto> m2 = memberRepository.findDtoProjectionsByUsername("m1", UsernameOnlyDto.class);
        List<NestedClosedProjections> m3 = memberRepository.findDtoProjectionsByUsername("m1", NestedClosedProjections.class);

        //then
        for (UsernameOnly usernameOnly : m1) {
            System.out.println("usernameOnly.getUsername() = " + usernameOnly.getUsername());
        }
        for (UsernameOnlyDto usernameOnlyDto : m2) {
            System.out.println("usernameOnlyDto.getUsername() = " + usernameOnlyDto.getUsername());
        }
        for (NestedClosedProjections nestedClosedProjections : m3) {
            System.out.println("nestedClosedProjections.getUsername() = " + nestedClosedProjections.getUsername());
            System.out.println("nestedClosedProjections.getTeam().getName() = " + nestedClosedProjections.getTeam().getName());
        }

    }

    @Test
    @DisplayName("native query test")
    void nativeQuery() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("m1", 0, teamA);
        Member member2 = new Member("m2", 0, teamA);
        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        //when
//        Member result = memberRepository.findByNativeQuery("m1");
        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));

        //then
        List<MemberProjection> content = result.getContent();
        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection.getUsername() = " + memberProjection.getUsername());
            System.out.println("memberProjection.getTeamName() = " + memberProjection.getTeamName());
        }
    }

}