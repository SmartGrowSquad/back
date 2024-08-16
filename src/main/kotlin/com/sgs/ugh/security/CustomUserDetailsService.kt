package com.sgs.ugh.security

import com.sgs.ugh.dto.CustomerDto
import com.sgs.ugh.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val memberRepository: MemberRepository,
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val member = memberRepository.findMemberByEmail(email) ?: throw RuntimeException("해당하는 유저가 없습니다.")

        val dto = CustomerDto(
            member.id!!,
            member.name,
            member.password,
            member.email,
            member.role
        )
        return CustomUserDetails(dto)
    }
}