package com.sgs.ugh.security

import com.sgs.ugh.dto.CustomerDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    private val user: CustomerDto
): UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        val roles = listOf(user.role)
        return roles.map { SimpleGrantedAuthority( it ) }
    }
    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.email
    }
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}