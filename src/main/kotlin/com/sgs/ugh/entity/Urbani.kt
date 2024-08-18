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
    var name: String,

    @Column(name = "location")
    var location: String,

    @Column(nullable = true)
    var cLocate: String,

    @Column(name = "day_of_week")
    var dayOfWeek: String,

    @Column(name ="open_time")
    var openTime: LocalTime,

    @Column(name ="close_time")
    var closeTime: LocalTime,

    @OneToMany(mappedBy = "urbani")
    var availableCrop: MutableSet<AvailableCrop>

): MutableEntity()