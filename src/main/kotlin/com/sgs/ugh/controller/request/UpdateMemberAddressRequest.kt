package com.sgs.ugh.controller.request

import jakarta.validation.constraints.NotNull

data class UpdateMemberAddressRequest(
    @NotNull
    val id: Long,

    @NotNull
    val address: String,
)
