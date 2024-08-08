package com.sgs.ugh.repository

import com.sgs.ugh.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findUserByEmail(email: String): User?
    fun findUserById(userId: Long): User?
}