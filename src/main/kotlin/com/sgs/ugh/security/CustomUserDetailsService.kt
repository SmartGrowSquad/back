package com.sgs.ugh.security

import com.sgs.ugh.dto.CustomerDto
import com.sgs.ugh.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findUserByEmail(email) ?: throw RuntimeException("해당하는 유저가 없습니다.")

        val dto = CustomerDto(user.id!!, user.name, user.email, user.password)
        return CustomUserDetails(dto)
    }
}