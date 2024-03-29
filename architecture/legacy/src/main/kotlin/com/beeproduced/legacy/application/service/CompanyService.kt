package com.beeproduced.legacy.application.service

import com.beeproduced.bee.persistent.selection.DataSelection
import com.beeproduced.bee.persistent.selection.EmptySelection
import com.beeproduced.bee.functional.result.AppResult
import com.beeproduced.bee.functional.result.errors.BadRequestError
import com.beeproduced.bee.functional.extensions.com.github.michaelbull.result.andThenToPair
import com.beeproduced.bee.functional.persistent.transactional.TransactionalResult
import com.beeproduced.legacy.application.dao.CompanyMemberDao
import com.beeproduced.legacy.application.model.*
import com.beeproduced.legacy.application.repository.CompanyMemberRepository
import com.beeproduced.legacy.application.repository.CompanyRepository
import com.beeproduced.legacy.application.model.input.CreateAddressInput
import com.beeproduced.legacy.application.model.input.CreateCompanyInput
import com.beeproduced.legacy.application.service.mapper.CompanyMapper
import com.beeproduced.legacy.application.utils.logFor
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.map
import org.springframework.stereotype.Service
import java.util.*

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */
@Service
class CompanyService(
    private val repository: CompanyRepository,
    private val memberRepository: CompanyMemberRepository,
    private val personService: PersonService,
    private val addressService: AddressService,
    private val mapper: CompanyMapper
) {
    private val logger = logFor<CompanyService>()

    @TransactionalResult(
        exceptionDescription = "Could not create company",
    )
    fun create(
        create: CreateCompanyInput,
        selection: DataSelection,
    ): AppResult<Company> {
        logger.debug("create({}, {})", create, selection)
        return createAddress(create.address).map { address ->
            val companyDao = mapper.toDao(create, address)
            repository.save(companyDao)
            mapper.toModel(companyDao)
        }.andThenToPair {
            getEmployees(create.employees)
        }.map { (company, employees) ->
            assignPersonsToCompany(company, employees, selection)
        }
    }

    private fun getEmployees(personIds: Collection<PersonId>?): AppResult<List<Person>?> {
        if (personIds == null) return Ok(null)
        return personService.getByIds(personIds, EmptySelection())
    }

    private fun createAddress(createAddressInput: CreateAddressInput?): AppResult<Address?> {
        if (createAddressInput == null) return Ok(null)
        return addressService.create(createAddressInput)
    }

    private fun assignPersonsToCompany(
        company: Company,
        employees: List<Person>?,
        selection: DataSelection,
    ): Company {
        if (employees == null) return company
        // Persist Person ↔ Company relation
        for (employee in employees)
            memberRepository.save(CompanyMemberDao(company.id, employee.id))
        // Check if data needs to be re-fetched with employees loaded
        if (selection.contains("**{employees}")) {
            val companyWithEmployees = repository
                .selectById(company.id, selection)
                ?.let(mapper::toModel)
            return companyWithEmployees ?: company
        }
        return company
    }

    @TransactionalResult(
        exceptionDescription = "Could not fetch all companies",
        readOnly = true
    )
    fun getAll(selection: DataSelection): AppResult<List<Company>> {
        logger.debug("getAll({})", selection)
        val companyDaos = repository.select(selection)
        val companies = companyDaos.map(mapper::toModel)
        return Ok(companies)
    }

    @TransactionalResult(
        exceptionDescription = "Could not fetch all companies",
        readOnly = true
    )
    fun getByIds(ids: Collection<PersonId>, selection: DataSelection): AppResult<List<Company>> {
        logger.debug("getByIds({}, {})", ids, selection)
        val uniqueIds = ids.toSet()
        val companyDaos = repository.selectByIdIn(uniqueIds, selection)
        if (companyDaos.count() == uniqueIds.count()) {
            val companies = companyDaos.map(mapper::toModel)
            return Ok(companies)
        }
        return Err(BadRequestError("Could not find all persons"))
    }

    @TransactionalResult(
        exceptionDescription = "Could not check companies",
        readOnly = true
    )
    fun exists(ids: Collection<CompanyId>): AppResult<Unit> {
        return if (repository.existsAllByIdIn(ids)) Ok(Unit)
        else Err(BadRequestError("Could not find some companies"))
    }
}