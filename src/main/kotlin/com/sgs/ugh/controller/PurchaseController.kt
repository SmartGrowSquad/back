package com.sgs.ugh.controller

import com.sgs.ugh.controller.request.CancelPurchaseRequest
import com.sgs.ugh.controller.request.PurchaseRequest
import com.sgs.ugh.controller.request.UpdatePurchaseStateRequest
import com.sgs.ugh.controller.response.GetPurchaseResponse
import com.sgs.ugh.controller.response.PurchaseRequestResponse
import com.sgs.ugh.entity.Purchase
import com.sgs.ugh.service.PurchaseService
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

@Tag(name = "Purchase Api")
@RestController
@RequestMapping("/v1/purchase")
class PurchaseController(
    private val purchaseService: PurchaseService
) {
    @PostMapping("/")
    fun createPurchase(
        @Valid @RequestBody req: PurchaseRequest
    ): ResponseEntity<PurchaseRequestResponse> {
        val response = purchaseService.createPurchase(req)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @PostMapping("/update-state")
    fun updatePurchaseStatus(
        @Valid @RequestBody req: UpdatePurchaseStateRequest
    ): ResponseEntity<Purchase> {
        val response = purchaseService.updatePurchaseStatus(req)
        return ResponseEntity(response, HttpStatus.OK)
    }
    @GetMapping("/{id}")
    fun getPurchased(
        @PathVariable id: Long
    ): ResponseEntity<GetPurchaseResponse> {
        val response = purchaseService.getPurchased(id)
        return ResponseEntity(response, HttpStatus.FOUND)
    }

    /**
     * 관리자만 가능(관리자: 어반이 시스템 또는 관리자)
     */
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @PostMapping("/approval")
    fun approvalPurchase(
        @Valid @RequestBody req: UpdatePurchaseStateRequest
    ): ResponseEntity<Purchase> {
        val response = purchaseService.updatePurchaseStatus(req)
        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * 구매자가 구매 취소를 요청하는 경우
     * 승인이 되고 준비 완료 상태에서는 취소를 하지 못함
     */
    @PostMapping("/cancel")
    fun cancelPurchase(
        @Valid @RequestBody req: CancelPurchaseRequest
    ): ResponseEntity<String> {
        val response = purchaseService.cancelPurchase(req)

        return ResponseEntity(response, HttpStatus.OK)
    }
}