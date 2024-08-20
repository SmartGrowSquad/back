package com.sgs.ugh.service

import com.sgs.ugh.controller.request.CancelPurchaseRequest
import com.sgs.ugh.controller.request.PurchaseRequest
import com.sgs.ugh.controller.request.UpdatePurchaseStateRequest
import com.sgs.ugh.controller.response.GetPurchaseResponse
import com.sgs.ugh.controller.response.PurchaseRequestResponse
import com.sgs.ugh.entity.Purchase
import com.sgs.ugh.repository.AvailableCropRepository
import com.sgs.ugh.repository.MemberRepository
import com.sgs.ugh.repository.PurchaseRepository
import com.sgs.ugh.repository.UrbaniRepository
import com.sgs.ugh.utils.PurchaseStatusCode
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.w3c.dom.traversal.NodeIterator
import java.util.UUID

@Service
@Transactional
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val availableCropRepository: AvailableCropRepository,
    private val memberRepository: MemberRepository,
) {
    /**
     * 구매 내역 생성
     */
    fun createPurchase(req: PurchaseRequest): PurchaseRequestResponse {
        val crop = availableCropRepository.findByIdOrNull(req.acId) ?: throw NotFoundException()
        val member = memberRepository.findMemberById(req.memberId) ?: throw NotFoundException()
        val passcode = UUID.randomUUID().toString()

        val purchase = purchaseRepository.save(
            Purchase(
                req.amount,
                0,
                passcode,
                member,
                crop
            )
        )
        return PurchaseRequestResponse(
            purchase.id!!,
            crop.name,
            passcode,
            purchase.createdAt!!,
        )
    }

    /**
     * 구매 상태 업데이트
     * 0: 승인 대기
     * 1: 승인
     * 2: 거래 완료
     * 3: 취소 - 일방적인 취소
     * 4: 취소 요청
     * 5: 취소 승인 - 이전 상태가 취소 요청 상태가 아니면 사용 불가
     */
    fun updatePurchaseStatus(req: UpdatePurchaseStateRequest): Purchase {
        val purchase = purchaseRepository.findByIdOrNull(req.id) ?: throw NotFoundException()

        /**
         * 해당 구매를 가지고 있는 어반이의 아이디와 요청한 어반이의 아이디가 같아야함
         */
        if(purchase.availableCrop.urbani.id == req.id) {
            purchase.apply {
                // 이전 상태가 4일 때만 5에 대한 상태 변화 가능
                if(status == PurchaseStatusCode.PURCHASE_CANCEL_APPROVE.ordinal) {
                    if(req.status == PurchaseStatusCode.PURCHASE_CANCEL_REQUESTED.ordinal) status = req.status
                    else throw RuntimeException("취소 승인 상태가 아닙니다.")
                }
                // todo 상태 변화 처리
                status = req.status
                purchaseRepository.save(purchase)
            }
        } else throw RuntimeException("해당 구매는 유효하지 않은 구매")

        return purchase
    }

    /**
     * 구매자의 취소 요청
     * 구매자의 취소는 요청 이후 관리자가 취소 승인해야 최종 취소된 것으로 분류
     *
     * 이미 승인 상태라면 취소 요청 불가능, 취소 상태라면 요청 불가능
     */
    fun cancelPurchase(req: CancelPurchaseRequest): String {
        val purchase = purchaseRepository.findByIdOrNull(req.id) ?: throw NotFoundException()

        if(purchase.status == PurchaseStatusCode.PURCHASE_WAIT_APPROVE.ordinal) {
            purchase.status = PurchaseStatusCode.PURCHASE_CANCEL_REQUESTED.ordinal

            purchaseRepository.save(purchase)
        }

        return "canceled"
    }

    /**
     * 구매 내역 조회
     */
    fun getPurchased(id: Long): GetPurchaseResponse {
        val purchaseHistory = purchaseRepository.findPurchasesByMemberId(id)

        return GetPurchaseResponse(purchaseHistory)
    }

    /**
     * 패스코드 검증
     */
    fun validationPasscode(id: Long, passcode: String): Boolean {
        val purchase = purchaseRepository.findByIdOrNull(id) ?: throw NotFoundException()
        var result = false

        if (purchase.passcode == passcode) {
            result = true
        }

        return result
    }

}