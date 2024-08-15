package com.sgs.ugh.dto

data class CustomerDto(
    val id: Long,
    val name: String,
    val password: String,
    val email: String,
    val role: String,
)