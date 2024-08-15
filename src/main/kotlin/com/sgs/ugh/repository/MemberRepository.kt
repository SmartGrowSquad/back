package com.sgs.ugh.repository

import com.sgs.ugh.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: JpaRepository<Member, Long> {
    fun findMemberByEmail(email: String): Member?
    fun findMemberById(userId: Long): Member?
}