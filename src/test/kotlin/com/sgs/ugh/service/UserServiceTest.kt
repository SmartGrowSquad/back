package com.sgs.ugh.service

import com.sgs.ugh.controller.request.CreateUserRequest
import com.sgs.ugh.entity.User
import com.sgs.ugh.exception.AlreadyExistException
import com.sgs.ugh.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk


class UserServiceTest: DescribeSpec({
    val userRepository = mockk<UserRepository>()
    val userService = UserService(userRepository)

    describe("User Service class") {

        describe("saveUser method") {
            val req = CreateUserRequest("user name", "user@email.com", "password")

            context("new email") {
                every { userRepository.findUserByEmail( any() ) } returns null
                every { userRepository.save( any() ) } returns User(1, "user name", "user@email.com", "password")

                it("return CreateUserResponse") {
                    val res = userService.saveUser(req)

                    res.id shouldBe 1
                    res.name shouldBe "user name"
                    res.email shouldBe "user@email.com"
                }
            }
            context("not new email") {
                every { userRepository.findUserByEmail( any() ) } throws AlreadyExistException()
                every { userRepository.save( any() ) } returns User(1, "user name", "user@email.com", "password")

                it("throw exception") {
                    val exception = shouldThrow<AlreadyExistException> {
                        userService.saveUser(req)
                    }
                    exception.message shouldBe "Already Exist"
                }

            }
        }

        describe("getUser method") {
            val userId: Long = 1
            context("exist user") {
                every { userRepository.findUserById( any() ) } returns User(1, "user name", "user@email.com", "password")
                it("return GetUserResponse") {
                    val res = userService.getUser(userId)

                    res.id shouldBe 1
                    res.name shouldBe "user name"
                    res.email shouldBe "user@email.com"
                }
            }
            context("not exist user") {
                every { userRepository.findUserById( any() ) } returns null
                it("throw exception") {
                    val exception = shouldThrow<RuntimeException> {
                        userService.getUser(userId)
                    }
                    exception.message shouldBe "not exist user"
                }
            }
        }
        describe("deleteUser method") {
            val userId: Long = 1

            context("not exist user") {
                every { userRepository.findUserById( any() ) } returns null
                it("throw exception") {
                    val exception = shouldThrow<RuntimeException> {
                        userService.deleteUser(userId)
                    }
                    exception.message shouldBe "not exist user"
                }
            }
        }
    }
})