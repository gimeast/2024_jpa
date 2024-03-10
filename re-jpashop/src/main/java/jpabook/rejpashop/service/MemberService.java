package jpabook.rejpashop.service;

import jpabook.rejpashop.domain.Member;
import jpabook.rejpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * @Method         : join
     * @Description    : 회원 가입
     * @Author         : gimeast
     * @Date           : 2024. 02. 25.
     * @params         : member
     * @return         : 회원 key 값 반환
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * @Method         : validateDuplicateMember
     * @Description    : 동일 이름의 회원 중복 조회 체크 로직(이 프로젝트에서는 회원의 이름을 유일값으로 가정 한다.)
     * @Author         : gimeast
     * @Date           : 2024. 02. 25.
     * @params         : member
     * @return         :
     */
    private void validateDuplicateMember(Member member) {

        List<Member> findMembers = memberRepository.findByName(member.getName());

        if (!findMembers.isEmpty()) throw new IllegalStateException("이미 존재하는 회원 입니다.");
    }

    //회원 전체 조회
    /**
     * @Method         : findMembers
     * @Description    : 회원 전체 조회
     * @Author         : gimeast
     * @Date           : 2024. 02. 25.
     * @params         : 
     * @return         : 회원 목록 반환
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * @Method         : findOne
     * @Description    : 회원 단일 조회
     * @Author         : gimeast
     * @Date           : 2024. 02. 25.
     * @params         : memberId
     * @return         : 회원 반환
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}