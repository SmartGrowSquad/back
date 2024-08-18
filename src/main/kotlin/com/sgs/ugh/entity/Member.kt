package com.sgs.ugh.entity

import com.sgs.ugh.entity.baseEntity.MutableEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany

@Entity(name = "member")
class Member(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var address: String,

    @Column(nullable = true)
    var cLocate: String?,

    @Column(nullable = false)
    var role: String,

    @OneToMany(mappedBy = "member")
    var purchases: MutableSet<Purchase> = mutableSetOf()
): MutableEntity() {
    fun updateEmail(updatedEmail: String) {
        this.email = updatedEmail
    }

    fun updatePassword(updatedPassword: String) {
        this.password = updatedPassword
    }

    fun updateAddress(updatedAddress: String) {
        this.address = updatedAddress
    }
}
