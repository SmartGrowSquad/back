package com.sgs.ugh.controller

import com.sgs.ugh.controller.request.CreateUrbaniRequest
import com.sgs.ugh.controller.response.CreateUrbaniResponse
import com.sgs.ugh.controller.response.SearchUrbaniResponse
import com.sgs.ugh.service.UrbaniService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 어반이 컨트롤러
 */
@Tag(name = "Urbani Api")
@RestController
@RequestMapping("/v1/urbani")
class UrbaniController(
    private val urbaniService: UrbaniService
) {
    @PostMapping("/create-urbani")

    fun saveUrbani(
        @Valid @RequestBody req: CreateUrbaniRequest
    ): ResponseEntity<CreateUrbaniResponse> {
        val res = urbaniService.createUrbani(req)

        return ResponseEntity(res, HttpStatus.CREATED)
    }
    @PostMapping("/delete-urbani/{id}")
    fun deleteUrbani(
        @PathVariable id: Long
    ): ResponseEntity<String> {
        val res = urbaniService.deleteUrbani(id)
        return ResponseEntity(res, HttpStatus.OK)
    }

    fun updateUrbani() {}
    @GetMapping("/search/{location}")
    fun searchUrbani(
        @PathVariable location: String
    ): ResponseEntity<SearchUrbaniResponse> {
        val res = urbaniService.searchNearUrbani(location)

        return ResponseEntity(res, HttpStatus.FOUND)
    }

}