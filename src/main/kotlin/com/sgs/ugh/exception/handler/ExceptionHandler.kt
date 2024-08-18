package com.sgs.ugh.exception.handler

import com.sgs.ugh.exception.AlreadyExistException
import com.sgs.ugh.exception.BadRequestException
import com.sgs.ugh.exception.MemberNotFoundException
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    private val log = LoggerFactory.getLogger(ExceptionHandler::class.java)
    /**
     * 잘못된 요청 EXCEPTION
     */
    @ExceptionHandler(BadRequestException::class)
    fun handleGetTestBadRequestException(ex: BadRequestException): ResponseEntity<BadRequestException> {
        return ResponseEntity(BadRequestException(), HttpStatus.BAD_REQUEST)
    }

    /**
     * 잘못된 path variable EXCEPTION
     */
    @ExceptionHandler(MethodArgumentNotValidException::class, ConstraintViolationException::class)
    fun handleInvalidRequestVariableException(ex: MethodArgumentNotValidException): ResponseEntity<BadRequestException> {
        log.info("ConstraintViolationException")
        return ResponseEntity(BadRequestException(), HttpStatus.BAD_REQUEST)
    }

    /**
     * 이미 존재하는 데이터를 생성시 Exception
     */
    @ExceptionHandler(AlreadyExistException::class)
    fun handleAlreadyExistException(ex: AlreadyExistException): ResponseEntity<AlreadyExistException> {
        return ResponseEntity(AlreadyExistException(), HttpStatus.CONFLICT)
    }

    @ExceptionHandler(MemberNotFoundException::class)
    fun handleMemberNotFoundException(ex: MemberNotFoundException): ResponseEntity<MemberNotFoundException>{
        return ResponseEntity(MemberNotFoundException(), HttpStatus.NOT_FOUND)
    }
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<AccessDeniedException>{
        return ResponseEntity(AccessDeniedException("Security Denied Access"), HttpStatus.FORBIDDEN)
    }
    /**
     * fall back Exception
     */
    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<Exception> {
        log.info(ex.message)
        return ResponseEntity.internalServerError().body(Exception(ex.message))
    }
}