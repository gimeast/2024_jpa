package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
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

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;
    
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


}