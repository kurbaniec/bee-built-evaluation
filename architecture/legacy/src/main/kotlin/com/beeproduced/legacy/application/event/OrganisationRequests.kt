package com.beeproduced.legacy.application.event

import com.beeproduced.bee.persistent.selection.DataSelection
import com.beeproduced.bee.buzz.Request
import com.beeproduced.legacy.application.model.Company
import com.beeproduced.legacy.application.model.CompanyId
import com.beeproduced.legacy.application.model.Person
import com.beeproduced.legacy.application.model.PersonId
import com.beeproduced.legacy.application.model.input.CreateCompanyInput
import com.beeproduced.legacy.application.model.input.CreatePersonInput

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-26
 */

data class CreateCompany(
    val create: CreateCompanyInput,
    val selection: DataSelection,
) : Request<Company>

data class GetAllCompanies(
    val selection: DataSelection
) : Request<Collection<Company>>

data class GetCompaniesByIds(
    val ids: Collection<CompanyId>,
    val selection: DataSelection
) : Request<Collection<Company>>

data class CompaniesExist(
    val ids: Collection<CompanyId>
) : Request<Unit>

data class CreatePerson(
    val create: CreatePersonInput,
    val selection: DataSelection,
) : Request<Person>

data class GetAllPersons(
    val selection: DataSelection
) : Request<Collection<Person>>

data class GetPersonsByIds(
    val ids: Collection<PersonId>,
    val selection: DataSelection
) : Request<Collection<Person>>

data class PersonsExist(
    val ids: Collection<PersonId>
) : Request<Unit>