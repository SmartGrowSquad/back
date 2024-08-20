package com.sgs.ugh.service

import com.sgs.ugh.controller.request.SaveAvailableCropRequest
import com.sgs.ugh.controller.request.UpdateCropAmountRequest
import com.sgs.ugh.controller.request.UpdateCropStatusRequest
import com.sgs.ugh.controller.response.SaveAvailableCropResponse
import com.sgs.ugh.entity.AvailableCrop
import com.sgs.ugh.exception.AlreadyExistException
import com.sgs.ugh.repository.AvailableCropRepository
import com.sgs.ugh.repository.UrbaniRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class AvailableCropService(
    private val availableCropRepository: AvailableCropRepository,
    private val urbaniRepository: UrbaniRepository
) {
    fun saveCrop(req: SaveAvailableCropRequest): SaveAvailableCropResponse {
        // 어반이가 가지고 있는 crop 중 같은 이름이 있는지 확인
        if(availableCropRepository.findByNameAndUrbaniId(req.name, req.urbaniId) != null) throw AlreadyExistException()
        val urbani = urbaniRepository.findByIdOrNull(req.urbaniId) ?: throw NotFoundException()

        val savedCrop = availableCropRepository.save(
            AvailableCrop(
                req.name,
                req.price,
                req.description,
                req.amount,
                false,
                urbani
            )
        )

        return SaveAvailableCropResponse(
            savedCrop.id!!,
            savedCrop.name,
            savedCrop.price,
            savedCrop.description,
            savedCrop.amount,
            savedCrop.status
        )
    }

    /**
     * false: decrease
     * true: increase
     */
    fun updateCropAmount(req: UpdateCropAmountRequest): AvailableCrop {
        val crop = availableCropRepository.findByIdOrNull(req.id) ?: throw NotFoundException()
        crop.apply {
            if(!req.code) decreaseAmount(req.amount)
            else increaseAmount(req.amount)
        }

        return crop
    }
    fun updateCropInfo() {}

    fun updateCropStatus(req: UpdateCropStatusRequest): AvailableCrop {
        val crop = availableCropRepository.findByIdOrNull(req.id) ?: throw NotFoundException()
         crop.apply {
             status = req.status
         }

        return crop
    }

    // TODO test
    fun getAvailableCrops(id: Long): Set<AvailableCrop> {
        urbaniRepository.findById(id) ?: throw NotFoundException()
        return availableCropRepository.findAllByUrbaniId(id)
    }

    // TODO nullable 로 처리하고 있는데 이 부분은 null 을 반환할지 exception 을 던질지 확인해야함
    fun getAvailableCropInfoWithId(id: Long): AvailableCrop {
        val crop = availableCropRepository.findByIdOrNull(id) ?: throw NotFoundException()
        return crop
    }
}