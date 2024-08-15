package com.sgs.ugh.service

import com.sgs.ugh.controller.request.CreateMemberRequest
import com.sgs.ugh.entity.Member
import com.sgs.ugh.exception.AlreadyExistException
import com.sgs.ugh.repository.MemberRepository
import com.sgs.ugh.utils.Role
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.crypto.password.PasswordEncoder


class UserServiceTest: DescribeSpec({
    val memberRepository = mockk<MemberRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val memberService = MemberService(memberRepository, passwordEncoder)

    describe("User Service class") {

        describe("saveUser method") {
            val req = CreateMemberRequest("user name", "user@email.com", "password", "address")

            context("new email") {
                every { memberRepository.findMemberByEmail( any() ) } returns null
                every { passwordEncoder.encode( any() ) } returns "encoded password"
                every { memberRepository.save( any() ) } returns Member("username", req.email, "passcode", "address", null, Role.ROLE_CUSTOMER.name)


                it("return CreateUserResponse") {
                    val res = memberService.saveMember(req)

                    res.id shouldBe 1
                    res.name shouldBe "user name"
                    res.email shouldBe "user@email.com"

                }
            }
            context("not new email") {
                every { memberRepository.findMemberByEmail( any() ) } throws AlreadyExistException()
                every { passwordEncoder.encode( any() ) } returns "encoded password"
                every { memberRepository.save( any() ) } returns Member("username", req.email, "passcode", "address", null, Role.ROLE_CUSTOMER.name)

                it("throw exception") {
                    val exception = shouldThrow<AlreadyExistException> {
                        memberService.saveMember(req)
                    }
                    exception.message shouldBe "Already Exist"
                }

            }
        }

        describe("getUser method") {
            val userId: Long = 1
            context("exist user") {
                every { memberRepository.findMemberById( any() ) } returns Member("username", "email@email.com", "passcode", "address", null, Role.ROLE_CUSTOMER.name)
                it("return GetUserResponse") {
                    val res = memberService.getMemberDetail(userId)

                    res.id shouldBe 1
                    res.name shouldBe "user name"
                    res.email shouldBe "user@email.com"
                }
            }
            context("not exist user") {
                every { memberRepository.findMemberById( any() ) } returns null
                it("throw exception") {
                    val exception = shouldThrow<RuntimeException> {
                        memberService.getMemberDetail(userId)
                    }
                    exception.message shouldBe "not exist user"
                }
            }
        }
        describe("deleteUser method") {
            val userId: Long = 1

            context("not exist user") {
                every { memberRepository.findMemberById( any() ) } returns null
                it("throw exception") {
                    val exception = shouldThrow<RuntimeException> {
                        memberService.deleteMember(userId)
                    }
                    exception.message shouldBe "not exist user"
                }
            }
        }
    }
})