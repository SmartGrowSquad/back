package com.sgs.ugh.entity

import com.sgs.ugh.entity.baseEntity.MutableEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "available_crop")
class AvailableCrop(
    @Column(name="name")
    val name: String,

    @Column(name="price")
    var price: Double,

    @Column(name="description")
    var description: String,

    @Column(name="amount")
    var amount: Int,

    @Column(name="status")
    var status: Boolean = true,

    @ManyToOne
    @JoinColumn(name = "id")
    val urbani: Urbani
): MutableEntity() {
    fun decreaseAmount(amount: Int) {
        this.amount -= amount
    }

    fun increaseAmount(amount: Int) {
        this.amount += amount
    }

    fun updateDescription(description: String) {
        this.description = description
    }

    fun updateStatus(status: Boolean) {
        this.status = status
    }

    fun updatePrice(price: Double) {
        this.price = price
    }
}