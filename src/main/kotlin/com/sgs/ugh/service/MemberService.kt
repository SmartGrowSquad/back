package com.sgs.ugh.service

import com.sgs.ugh.controller.request.CreateMemberRequest
import com.sgs.ugh.dto.MemberDto
import com.sgs.ugh.entity.Member
import com.sgs.ugh.exception.AlreadyExistException
import com.sgs.ugh.exception.MemberNotFoundException
import com.sgs.ugh.repository.MemberRepository
import com.sgs.ugh.utils.Role
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {
    private val log = LoggerFactory.getLogger(MemberService::class.java)
    /**
     * 유저 생성
     * @param req [CreateMemberRequest]
     * @return [CreateMemberRequest]
     *
     */
    fun saveMember(req: CreateMemberRequest): com.sgs.ugh.controller.response.CreateMemberResponse {
        val encodedPassword = passwordEncoder.encode(req.password)

        val user = Member(
            name = req.name,
            email = req.email,
            password = encodedPassword,
            address = req.address,
            cLocate = null,
            role = Role.ROLE_ADMIN.name,
        )

        memberRepository.findMemberByEmail(user.email)?.let { throw AlreadyExistException() }

        val savedUser = memberRepository.save(user)

        return com.sgs.ugh.controller.response.CreateMemberResponse(
            savedUser.id!!,
            savedUser.name,
            savedUser.email
        )
    }

    /**
     * 유저 조회
     * @param userId [Long]
     * @return [MemberDto]
     * @exception RuntimeException
     */
    fun getMemberDetail(userId: Long): MemberDto {
        val foundMember = memberRepository.findMemberById(userId) ?: throw MemberNotFoundException()

        return MemberDto(
            foundMember.id!!,
            foundMember.name,
            foundMember.email,
            foundMember.address,
            foundMember.purchases,
        )
    }
    fun updateUser() {}
    /**
     * 유저 삭제
     * @param userId [Long]
     * @return [Unit]
     * @exception RuntimeException
     */
    @Transactional
    fun deleteMember(userId: Long) {
        memberRepository.findMemberById(userId) ?: throw RuntimeException("not exist user")
        memberRepository.deleteById(userId)
    }
}