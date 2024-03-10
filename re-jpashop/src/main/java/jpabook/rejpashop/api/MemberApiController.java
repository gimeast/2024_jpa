package jpabook.rejpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.rejpashop.domain.Member;
import jpabook.rejpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * @Method         : saveMemberV1
     * @Description    : 잘못된 방식 - 엔티티로 request 받지 말것!
     * @Author         : gimeast
     * @Date           : 2024. 03. 10.
     * @params         : Member
     * @return         : CreatemMemberResponse
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * @Method         : membersV1
     * @Description    : 잘못된 방식 - 엔티티를 직접 외부에 노출하지 말것!
     * @Author         : gimeast
     * @Date           : 2024. 03. 11.
     * @params         :
     * @return         : List
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    /**
     * @Method         : saveMemberV2
     * @Description    : 옳은 방식 - 엔티티 대신 dto를 만들어서 받아야한다
     * @Author         : gimeast
     * @Date           : 2024. 03. 10.
     * @params         : CreateMemberRequest
     * @return         : CreateMemberResponse
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * @Method         : membersV2
     * @Description    : 옳은 방식 - entity를 dto로 변환하여 리턴한다.
     * @Author         : gimeast
     * @Date           : 2024. 03. 11.
     * @params         : 
     * @return         : Result
     */
    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<MemberDto> memberDtos = memberService.findMembers().stream()
                .map(member -> new MemberDto(member.getName()))
                .collect(Collectors.toList());

        return new Result<>(memberDtos.size(), memberDtos);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request
    ) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }

}
