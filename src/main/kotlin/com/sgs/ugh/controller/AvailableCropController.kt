package com.sgs.ugh.controller

import com.sgs.ugh.controller.request.SaveAvailableCropRequest
import com.sgs.ugh.controller.request.UpdateCropAmountRequest
import com.sgs.ugh.controller.request.UpdateCropStatusRequest
import com.sgs.ugh.controller.response.SaveAvailableCropResponse
import com.sgs.ugh.entity.AvailableCrop
import com.sgs.ugh.service.AvailableCropService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Available Crop Api")
@RestController
@RequestMapping("/v1/crop")
class AvailableCropController(
    private val availableCropService: AvailableCropService
) {
    @PostMapping("/save-crop")
    fun saveCrop(
        @Valid @RequestBody req: SaveAvailableCropRequest
    ): ResponseEntity<SaveAvailableCropResponse> {
        val crop = availableCropService.saveCrop(req)
        return ResponseEntity(crop, HttpStatus.CREATED)
    }

    @PostMapping("/update-amount")
    fun updateCropAmount(
        @Valid @RequestBody req: UpdateCropAmountRequest
    ): ResponseEntity<AvailableCrop> {
        val crop = availableCropService.updateCropAmount(req)
        return ResponseEntity(crop, HttpStatus.OK)
    }
    @PostMapping("/update-status")
    fun updateCropStatus(
        @Valid @RequestBody req: UpdateCropStatusRequest
    ): ResponseEntity<AvailableCrop> {
        val crop = availableCropService.updateCropStatus(req)
        return ResponseEntity(crop, HttpStatus.OK)
    }

    @GetMapping("/get-crops/{id}")
    fun getAvailableCrops(
        @PathVariable id: Long
    ): ResponseEntity<Set<AvailableCrop>> {
        val crop = availableCropService.getAvailableCrops(id)
        return ResponseEntity(crop, HttpStatus.FOUND)
    }
    @GetMapping("/get-info{id}")
    fun getAvailableCropInfoWithId(
        @PathVariable id: Long
    ): ResponseEntity<AvailableCrop> {
        val crop = availableCropService.getAvailableCropInfoWithId(id)
        return ResponseEntity(crop, HttpStatus.FOUND)
    }
}