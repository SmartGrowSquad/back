package com.sgs.ugh.controller

import com.sgs.ugh.controller.request.CreateMemberRequest
import com.sgs.ugh.controller.response.GetMemberResponse
import com.sgs.ugh.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/user")
class MemberController(
    private val memberService: MemberService
) {
    @PostMapping("/create-member")
    fun createMember(
        @Valid @RequestBody req: CreateMemberRequest
    ): ResponseEntity<com.sgs.ugh.controller.response.CreateMemberResponse> {
        val result: com.sgs.ugh.controller.response.CreateMemberResponse = memberService.saveMember(req)
        return ResponseEntity(result, HttpStatus.CREATED)
    }

    @GetMapping("/{memberId}")
    fun getMember(
        @PathVariable memberId: Long
    ): ResponseEntity<GetMemberResponse> {
        val memberDetail = memberService.getMember(memberId)
        return ResponseEntity(memberDetail, HttpStatus.OK)
    }

    @DeleteMapping("/delete-user/{userId}")
    fun deleteMember(
        @PathVariable userId: Long
    ): ResponseEntity<String> {
        memberService.deleteMember(userId)
        return ResponseEntity("member deleted", HttpStatus.OK)
    }


}