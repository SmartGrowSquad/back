package com.sgs.ugh.controller.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateUserRequest(
    @NotNull
    @NotBlank
    val name: String,

    @NotNull
    @Email
    val email: String,

    @NotNull
    val password: String
)
