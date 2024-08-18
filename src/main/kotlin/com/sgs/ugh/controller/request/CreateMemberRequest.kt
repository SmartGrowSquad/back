package com.sgs.ugh.controller.request

import com.sgs.ugh.utils.Role
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateMemberRequest(
    @NotNull
    @NotBlank
    val name: String,

    @NotNull
    @Email
    val email: String,

    @NotNull
    val password: String,

    @NotNull
    val address: String,

    val role: String = Role.ROLE_CUSTOMER.name
)
