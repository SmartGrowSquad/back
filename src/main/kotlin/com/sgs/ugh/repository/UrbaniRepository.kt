package com.sgs.ugh.repository

import com.sgs.ugh.entity.Urbani
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UrbaniRepository: JpaRepository<Urbani, Long> {
    // TODO 쿼리 테스트 필요
    @Query("select u from urbani u where u.name=?1")
    fun findByName(name: String): Urbani?

    @Query("select u from urbani u where u.location like ?1")
    fun findAllByLocation(location: String): Set<Urbani>
}