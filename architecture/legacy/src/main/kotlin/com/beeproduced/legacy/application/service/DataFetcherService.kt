package com.beeproduced.legacy.application.service

import com.beeproduced.bee.functional.result.AppResult
import com.beeproduced.bee.persistent.selection.DataSelection
import com.beeproduced.legacy.application.model.Company
import com.beeproduced.legacy.application.model.CompanyId
import com.beeproduced.legacy.application.model.Person
import com.beeproduced.legacy.application.model.PersonId
import com.github.michaelbull.result.binding
import org.springframework.stereotype.Service

/**
 * Note: This mimics data loaders at service level which works but is not great.
 * The old codebase used such functionality while migrating from REST to GraphQL.
 * The function must be explicitly called and does not allow fine-grained control at which depth level
 * it is invoked.
 * For a better architecture, optimised for GraphQL please use data loaders as described in the thesis.
 *
 * @author Kacper Urbaniec
 * @version 2024-03-04
 */
@Service
class DataFetcherService(
    private val personService: PersonService,
    private val companyService: CompanyService,
) {
    data class TransientData(
        val persons: Map<PersonId, Person>,
        val companies: Map<CompanyId, Company>
    )

    fun loadTransientData(
        selection: DataSelection,
        personIds: Set<PersonId> = emptySet(),
        companyIds: Set<CompanyId> = emptySet(),
    ): AppResult<TransientData> = binding {
        val companies = mutableMapOf<CompanyId, Company>()
        val persons = mutableMapOf<PersonId, Person>()

        if (selection.contains("**{studios}")) {
            val fetchedCompanies = companyService.getByIds(companyIds, selection).bind()
            fetchedCompanies.forEach { c -> companies[c.id] = c }
        }

        if (selection.contains("**{directors,cast}")) {
            val fetchedPersons = personService.getByIds(personIds, selection).bind()
            fetchedPersons.forEach { p -> persons[p.id] = p }
        }

        TransientData(persons, companies)
    }
}