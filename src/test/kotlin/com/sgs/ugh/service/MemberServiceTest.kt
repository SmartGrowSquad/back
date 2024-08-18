package com.sgs.ugh.service

import com.sgs.ugh.controller.request.CreateMemberRequest
import com.sgs.ugh.controller.request.UpdateMemberAddressRequest
import com.sgs.ugh.controller.request.UpdateMemberEmailRequest
import com.sgs.ugh.controller.response.CreateMemberResponse
import com.sgs.ugh.entity.Member
import com.sgs.ugh.exception.AlreadyExistException
import com.sgs.ugh.exception.MemberNotFoundException
import com.sgs.ugh.repository.MemberRepository
import com.sgs.ugh.utils.Role
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.security.crypto.password.PasswordEncoder

class MemberServiceTest: DescribeSpec({
    val memberRepository = mockk<MemberRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val memberService = MemberService(memberRepository, passwordEncoder)

    describe("멤버 서비스") {
        val req = CreateMemberRequest("user name", "user@email.com", "password", "address")
        val savedMember = Member(
            "username",
            req.email,
            "passcode",
            "address",
            null,
            Role.ROLE_CUSTOMER.name
        ).apply {
            this.id = 1L
        }
        every { memberRepository.save( any() ) } returns savedMember


        describe("멤버 생성") {

            context("새로운 이메일") {
                every { memberRepository.findMemberByEmail( any() ) } returns null
                every { passwordEncoder.encode( any() ) } returns "encoded password"

                it("CreateMemberResponse 반환") {
                    val res = memberService.saveMember(req)

                    res::class shouldBe CreateMemberResponse::class

                }
            }
            context("이미 존재하는 이메일") {
                every { memberRepository.findMemberByEmail( any() ) } throws AlreadyExistException()
                every { passwordEncoder.encode( any() ) } returns "encoded password"


                it("AlreadyExistException 발생") {
                    val exception = shouldThrow<AlreadyExistException> {
                        memberService.saveMember(req)
                    }
                    exception.message shouldBe "Already Exist"
                }

            }
        }

        describe("멤버 조회") {
            val memberId: Long = 1L
            context("존재하는 멤버") {
                every { memberRepository.findMemberById( any () ) } returns savedMember
                it("return GetUserResponse") {
                    val res = memberService.getMemberDetail(memberId)

                    res.id shouldBe 1
                    res.name shouldBe "username"
                    res.email shouldBe "user@email.com"
                }
            }
            context("존재하지 않는 멤버") {
                every { memberRepository.findMemberById( any() ) } returns null

                it("MemberNotFoundException 발생") {
                    val exception = shouldThrow<MemberNotFoundException> {
                        memberService.getMemberDetail(memberId)
                    }
                    exception.message shouldBe "Member Not found"
                }
            }
        }

        describe("update member email") {
            val memberId: Long = 1L
            val updatedEmail: String = "updated@email.com"
            val req = UpdateMemberEmailRequest(memberId, updatedEmail)
            context("존재하는 멤버") {
                every { memberRepository.findMemberById( any () ) } returns savedMember
                it("해당 멤버 이메일 수정") {

                    val res = memberService.updateMemberEmail(req)

                    res.email shouldBe updatedEmail
                }
            }
            context("존재하지 않는 멤버") {
                every { memberRepository.findMemberById( any() ) } returns null
                it("MemberNotFoundException 발생") {
                    val exception = shouldThrow<MemberNotFoundException> {
                        memberService.updateMemberEmail(req)
                    }
                    exception.message shouldBe "Member Not found"
                }
            }
            context("일단 알 수 없는 오류가 난 경우 에러") {
                every { memberRepository.findMemberById( any() ) } returns savedMember
                every { memberRepository.save( any() ) } throws RuntimeException()
                it("RuntimeException 발생") {
                    val exception = shouldThrow<RuntimeException> {
                        memberService.updateMemberEmail(req)
                    }
                    exception.message shouldBe "Unknown error in update member email service"
                }
            }

        }
        describe("update member address") {
            val memberId: Long = 1L
            val updatedAddress: String = "updatedAddress"
            val req = UpdateMemberAddressRequest(memberId, updatedAddress)

            context("존재하는 멤버") {
                every { memberRepository.findMemberById( any () ) } returns savedMember
                every { memberRepository.save( any() ) } returns savedMember.apply { updateAddress(updatedAddress)}
                it("해당 멤버 이메일 수정") {
                    val res = memberService.updateMemberAddress(req)

                    res.address shouldBe updatedAddress
                }
            }
            context("존재하지 않는 멤버") {
                every { memberRepository.findMemberById( any() ) } returns null
                it("MemberNotFoundException 발생") {
                    val exception = shouldThrow<MemberNotFoundException> {
                        memberService.updateMemberAddress(req)
                    }
                    exception.message shouldBe "Member Not found"
                }
            }
            context("일단 알 수 없는 오류가 난 경우 에러") {
                every { memberRepository.findMemberById( any() ) } returns savedMember
                every { memberRepository.save( any() ) } throws RuntimeException()
                it("RuntimeException 발생") {
                    val exception = shouldThrow<RuntimeException> {
                        memberService.updateMemberAddress(req)
                    }
                    exception.message shouldBe "Unknown error in update member address service"
                }
            }

        }

        describe("멤버 삭제") {
            val memberId: Long = 1
            context("존재하는 멤버") {
                every { memberRepository.findMemberById( any() ) } returns savedMember
                every { memberRepository.deleteById( any() ) } returns Unit
                it("아무것도 반환하지 않음") {
                    val deleteMemberId = memberService.deleteMember(memberId)
                }
            }
            context("memberRepository delete 에러") {
                every { memberRepository.findMemberById( any() ) } returns savedMember
                every { memberRepository.deleteById( any() ) } throws Exception()
                it("아무것도 반환하지 않음") {
                    val exception = shouldThrow<RuntimeException> {
                        memberService.deleteMember(memberId)
                    }
                    exception.message shouldBe "Unknown error in delete member service"
                }
            }
            context("존재하지 않는 멤버") {
                every { memberRepository.findMemberById( any() ) } returns null
                it("MemberNotFoundException 발생") {
                    val exception = shouldThrow<MemberNotFoundException> {
                        memberService.deleteMember(memberId)
                    }
                    exception.message shouldBe "Member Not found"
                }
            }
        }
    }
})