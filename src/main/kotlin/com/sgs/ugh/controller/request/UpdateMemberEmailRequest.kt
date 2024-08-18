package com.sgs.ugh.controller.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class UpdateMemberEmailRequest(
    @NotNull
    val id: Long,

    @NotNull
    @Email
    val email: String,
)