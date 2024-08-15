package com.sgs.ugh.entity

import com.sgs.ugh.entity.baseEntity.MutableEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import java.time.LocalTime

@Entity(name = "urbani")
class Urbani(
    @Column(name = "name")
    val name: String,

    @Column(name = "location")
    val location: String,

    @Column(nullable = true)
    val cLocate: String,

    @Column(name = "day_of_week")
    val dayOfWeek: String,

    @Column(name ="open_time")
    val openTime: LocalTime,

    @Column(name ="close_time")
    val closeTime: LocalTime,

    @OneToMany(mappedBy = "urbani")
    val availableCrop: MutableSet<AvailableCrop>

): MutableEntity()