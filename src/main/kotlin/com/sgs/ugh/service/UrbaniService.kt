package com.sgs.ugh.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.sgs.ugh.controller.request.CreateUrbaniRequest
import com.sgs.ugh.controller.response.CreateUrbaniResponse
import com.sgs.ugh.controller.response.SearchUrbaniResponse
import com.sgs.ugh.entity.Urbani
import com.sgs.ugh.exception.AlreadyExistException
import com.sgs.ugh.repository.UrbaniRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

/**
 *
 * 어반이 서비스
 * 어반이 등록, 수정, 삭제는 관리자 왜 실행 불가
 */
@Service
class UrbaniService(
    private val urbaniRepository: UrbaniRepository,
    private val objectMapper: ObjectMapper
) {
    /**
     * 어반이 등록
     * 어드민만 사용 가능
     */
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    fun createUrbani(req: CreateUrbaniRequest): CreateUrbaniResponse {
        urbaniRepository.findByName(req.name)?.let { throw AlreadyExistException() }

        val savedUrbani = urbaniRepository.save(
            Urbani(
                req.name,
                req.location,
                null,
                objectMapper.writeValueAsString(req.dayOfWeek),
                req.openTime,
                req.closeTime
            )
        )

        return CreateUrbaniResponse(
            savedUrbani.name,
            savedUrbani.location,
            savedUrbani.cLocate,
            objectMapper.readValue(savedUrbani.dayOfWeek, Array<String>::class.java) ,
            savedUrbani.openTime,
            savedUrbani.closeTime,
            savedUrbani.availableCrop
        )
    }

    fun updateUrbani() {}

    fun deleteUrbani(id: Long): String {
        urbaniRepository.findById(id) ?: throw AlreadyExistException()
        urbaniRepository.deleteById(id)

        return "deleted"
    }

    fun searchNearUrbani(location: String): SearchUrbaniResponse {
        val foundUrbani = urbaniRepository.findAllByLocation(location)

        return SearchUrbaniResponse(foundUrbani)
    }
}