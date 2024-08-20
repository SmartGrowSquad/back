package com.sgs.ugh.service

import io.kotest.core.spec.style.DescribeSpec

class PurchaseServiceTest: DescribeSpec({
    describe("구매 서비스 테스트") {
        describe("구매 요청 테스트") {
            describe("정상적인 요청") {}
            describe("존재하지 않는 crop") {}
            describe("존재하지 않는 어반이") {}
            describe("존재하지 않는 멤버") {}
            describe("존재하지 않는 crop") {}
            describe("가능한 수량보다 많은 요청") {}
        }
        describe("구매 상태 업데이트: 어반이") {
            describe("정상적인 요청") {}
            describe("존재하지 않는 구매 내역") {}
            describe("구매 상태를 업데이트 할 수 없는 경우") {}
        }
        describe("구매 취소 요청") {
            describe("정상적인 요청") {}
            describe("승인 대기 상태가 아닌 경우") {}
        }

        describe("구매 내역 조회") {
            describe("정상적인 요청") {}
            describe("존재하지 않는 멤버") {}
        }

        describe("passcode 검사") {
            describe("정상적인 요청") {}
            describe("존재하지 않는 구매 내역") {}
        }
    }
})