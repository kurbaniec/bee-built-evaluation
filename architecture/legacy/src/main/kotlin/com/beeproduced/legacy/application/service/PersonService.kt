package com.beeproduced.legacy.application.service

import com.beeproduced.bee.persistent.selection.DataSelection
import com.beeproduced.bee.functional.result.AppResult
import com.beeproduced.bee.functional.result.errors.BadRequestError
import com.beeproduced.bee.functional.persistent.transactional.TransactionalResult
import com.beeproduced.bee.persistent.selection.SimpleSelection
import com.beeproduced.legacy.application.repository.PersonRepository
import com.beeproduced.legacy.application.model.Address
import com.beeproduced.legacy.application.model.Person
import com.beeproduced.legacy.application.model.PersonId
import com.beeproduced.legacy.application.model.input.CreateAddressInput
import com.beeproduced.legacy.application.model.input.CreatePersonInput
import com.beeproduced.legacy.application.service.mapper.PersonMapper
import com.beeproduced.legacy.application.utils.logFor
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.map
import org.springframework.stereotype.Service

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */
@Service
class PersonService(
    private val repository: PersonRepository,
    private val addressService: AddressService,
    private val mapper: PersonMapper
) {
    private val logger = logFor<PersonRepository>()

    @TransactionalResult(
        exceptionDescription = "Could not create person",
    )
    fun create(
        create: CreatePersonInput,
        selection: DataSelection
    ): AppResult<Person> {
        logger.debug("create({}, {})", create, selection)
        return createAddress(create.address).map { address ->
            val personDao = mapper.toDao(create, address)
            repository.save(personDao)
            mapper.toModel(personDao)
        }
    }

    private fun createAddress(createAddressInput: CreateAddressInput?): AppResult<Address?> {
        if (createAddressInput == null) return Ok(null)
        return addressService.create(createAddressInput)
    }

    @TransactionalResult(
        exceptionDescription = "Could not fetch all persons",
        readOnly = true
    )
    fun getAll(selection: DataSelection): AppResult<List<Person>> {
        logger.debug("getAll({})", selection)
        val personDaos = repository.select(selection)
        val persons = personDaos.map(mapper::toModel)
        return Ok(persons)
    }

    @TransactionalResult(
        exceptionDescription = "Could not fetch all persons",
        readOnly = true
    )
    fun getByIds(ids: Collection<PersonId>, selection: DataSelection): AppResult<List<Person>> {
        logger.debug("getByIds({}, {})", ids, selection)
        val uniqueIds = ids.toSet()
        val personDaos = repository.selectByIdIn(uniqueIds, selection)
        if (personDaos.count() == uniqueIds.count()) {
            val persons = personDaos.map(mapper::toModel)
            return Ok(persons)
        }
        return Err(BadRequestError("Could not find all persons"))
    }

    @TransactionalResult(
        exceptionDescription = "Could not check persons",
        readOnly = true
    )
    fun exists(ids: Collection<PersonId>): AppResult<Unit> {
        return if (repository.existsAllByIdIn(ids)) Ok(Unit)
        else Err(BadRequestError("Could not find some persons"))
    }
}