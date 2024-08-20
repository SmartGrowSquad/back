package com.sgs.ugh.controller.response

data class SaveAvailableCropResponse(
    val id: Long,
    val name: String,
    val price: Double,
    val description: String,
    val amount: Int,
    val status: Boolean
)
