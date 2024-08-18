package com.sgs.ugh.service

import com.sgs.ugh.controller.request.CreateMemberRequest
import com.sgs.ugh.controller.request.UpdateMemberAddressRequest
import com.sgs.ugh.controller.request.UpdateMemberEmailRequest
import com.sgs.ugh.controller.response.CreateMemberResponse
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
     * 멤버 생성
     * @param req [CreateMemberRequest]
     * @return [CreateMemberRequest]
     *
     */
    fun saveMember(req: CreateMemberRequest): CreateMemberResponse {
        val encodedPassword = passwordEncoder.encode(req.password) ?: throw RuntimeException("Fail to encoding password")

        memberRepository.findMemberByEmail(req.email)?.let { throw AlreadyExistException() }

        val savedUser = memberRepository.save(
            Member(
                req.name,
                req.email,
                encodedPassword,
                req.address,
                null,
                req.role,
            )
        )

        return CreateMemberResponse(
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
    fun getMemberDetail(memberId: Long): MemberDto {
        val foundMember = memberRepository.findMemberById(memberId) ?: throw MemberNotFoundException()

        return MemberDto(
            foundMember.id!!,
            foundMember.name,
            foundMember.email,
            foundMember.address,
            foundMember.purchases,
        )
    }

    /**
     *  update member email
     */
    @Transactional
    fun updateMemberEmail(req: UpdateMemberEmailRequest): MemberDto {
        val foundMember = memberRepository.findMemberById(req.id) ?: throw MemberNotFoundException()

        foundMember.updateEmail(req.email)

        try {
           val updatedMember =  memberRepository.save(foundMember)

            return MemberDto(
                updatedMember.id!!,
                updatedMember.name,
                updatedMember.email,
                updatedMember.address,
                updatedMember.purchases,
            )
        } catch (e: RuntimeException) { throw RuntimeException("Unknown error in update member email service") }
    }

    /**
     * update member address
     */
    @Transactional
    fun updateMemberAddress(req: UpdateMemberAddressRequest): MemberDto {
        val foundMember = memberRepository.findMemberById(req.id) ?: throw MemberNotFoundException()

        foundMember.updateAddress(req.address)

        try {
            val updatedMember =  memberRepository.save(foundMember)

            return MemberDto(
                updatedMember.id!!,
                updatedMember.name,
                updatedMember.email,
                updatedMember.address,
                updatedMember.purchases,
            )
        } catch (e: Exception) { throw RuntimeException("Unknown error in update member address service") }

    }
    /**
     * 유저 삭제
     * @param userId [Long]
     * @return [Unit]
     * @exception RuntimeException
     */
    @Transactional
    fun deleteMember(memberId: Long) {
        memberRepository.findMemberById(memberId) ?: throw MemberNotFoundException()

        try {
            memberRepository.deleteById(memberId)
        } catch (e:Exception) { throw RuntimeException("Unknown error in delete member service") }

    }
}