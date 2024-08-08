package com.sgs.ugh.service

import com.sgs.ugh.controller.TestController
import com.sgs.ugh.controller.request.CreateUserRequest
import com.sgs.ugh.controller.response.CreateUserResponse
import com.sgs.ugh.controller.response.GetUserResponse
import com.sgs.ugh.entity.User
import com.sgs.ugh.exception.AlreadyExistException
import com.sgs.ugh.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {
    private val log = LoggerFactory.getLogger(TestController::class.java)
    /**
     * 유저 생성
     * @param req [CreateUserRequest]
     * @return [CreateUserResponse]
     *
     */
    @Transactional
    fun saveUser(req: CreateUserRequest): CreateUserResponse {
        val user = User(
            name = req.name,
            email = req.email,
            password = req.password
        )

        userRepository.findUserByEmail(user.email)?.let { throw AlreadyExistException() }

        val savedUser = userRepository.save(user)
        return CreateUserResponse(
            savedUser.id!!,
            savedUser.name,
            savedUser.email
        )
    }

    /**
     * 유저 조회
     * @param userId [Long]
     * @return [GetUserResponse]
     * @exception RuntimeException
     */
    fun getUser(userId: Long): GetUserResponse {
        val findUser = userRepository.findUserById(userId) ?: throw RuntimeException("not exist user")

        return GetUserResponse(findUser.id!!, findUser.name, findUser.email )
    }
    fun updateUser() {}
    /**
     * 유저 삭제
     * @param userId [Long]
     * @return [GetUserResponse]
     * @exception RuntimeException
     */
    @Transactional
    fun deleteUser(userId: Long) {
        userRepository.findUserById(userId) ?: throw RuntimeException("not exist user")
        userRepository.deleteById(userId)
    }
}