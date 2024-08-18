package com.sgs.ugh.controller.response

import com.sgs.ugh.entity.Urbani

data class SearchUrbaniResponse(
    val result: Set<Urbani>
)