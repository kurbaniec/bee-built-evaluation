package com.beeproduced.legacy.application.service

import com.beeproduced.bee.functional.result.AppResult
import com.beeproduced.bee.functional.persistent.transactional.TransactionalResult
import com.beeproduced.legacy.application.repository.AddressRepository
import com.beeproduced.legacy.application.model.Address
import com.beeproduced.legacy.application.model.input.CreateAddressInput
import com.beeproduced.legacy.application.service.mapper.AddressMapper
import com.beeproduced.legacy.application.utils.logFor
import com.github.michaelbull.result.Ok
import org.springframework.stereotype.Service
import java.util.*

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */
@Service
class AddressService(
    private val repository: AddressRepository,
    private val mapper: AddressMapper
) {
    private val logger = logFor<AddressRepository>()

    @TransactionalResult(
        exceptionDescription = "Could not create address",
    )
    fun create(create: CreateAddressInput): AppResult<Address> {
        logger.debug("create({})", create)
        val addressDao = mapper.toDao(create)
        repository.save(addressDao)
        val address = mapper.toModel(addressDao)
        return Ok(address)
    }
}