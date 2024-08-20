package com.sgs.ugh.controller.request

data class UpdateCropStatusRequest(
    val id: Long,
    val status: Boolean
)