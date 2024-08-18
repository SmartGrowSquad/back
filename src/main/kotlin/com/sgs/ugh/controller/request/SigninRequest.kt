package com.sgs.ugh.controller.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class SigninRequest(
    @NotNull
    @Email
    val email: String,

    @NotNull
    val password: String
)
