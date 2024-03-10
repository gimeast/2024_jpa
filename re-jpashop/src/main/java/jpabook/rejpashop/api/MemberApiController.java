package jpabook.rejpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.rejpashop.domain.Member;
import jpabook.rejpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
