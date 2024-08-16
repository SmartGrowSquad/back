package com.sgs.ugh.entity

import com.sgs.ugh.entity.baseEntity.MutableEntity
import jakarta.persistence.*

@Entity(name = "purchase")
class Purchase(
    @Column(name="amount")
    val amount: Int,

    @Column(name="status")
    var status: Int,

    @Column(name="passcode")
    val passcode: String,

    @ManyToOne
    @JoinColumn(name = "m_id")
    val member: Member
): MutableEntity() {
    fun updateStatus(status: Int) {
        this.status = status
    }
    // TODO passcode 생성 어떻게 할 지 정해야 함
    fun createPasscode() {}
}