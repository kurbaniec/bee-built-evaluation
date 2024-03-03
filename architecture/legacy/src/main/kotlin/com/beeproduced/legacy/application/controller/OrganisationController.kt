package com.beeproduced.legacy.application.controller

import com.beeproduced.bee.persistent.extensions.graphql.schema.toDataSelection
import com.beeproduced.bee.buzz.manager.EventManager
import com.beeproduced.bee.functional.result.AppResult
import com.beeproduced.legacy.application.controller.mapper.OrganisationMapper
import com.beeproduced.legacy.application.dto.CompanyDto
import com.beeproduced.legacy.application.dto.PersonDto
import com.beeproduced.legacy.application.event.GetAllCompanies
import com.beeproduced.legacy.application.event.GetAllPersons
import com.beeproduced.utils.logFor
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onFailure
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import graphql.schema.DataFetchingEnvironment

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-26
 */

@DgsComponent
class OrganisationController(
    private val eventManager: EventManager,
    private val mapper: OrganisationMapper
) {
    private val logger = logFor<OrganisationController>()

    @DgsQuery
    fun persons(dfe: DataFetchingEnvironment): AppResult<Collection<PersonDto>> {
        logger.debug("persons()")
        return eventManager.send(
            GetAllPersons(dfe.selectionSet.toDataSelection())
        )
            .map(mapper::toPersonDtos)
            .onFailure { e -> logger.error(e.stackTraceToString()) }
    }

    @DgsQuery
    fun companies(dfe: DataFetchingEnvironment): AppResult<Collection<CompanyDto>> {
        logger.debug("companies()")
        return eventManager.send(
            GetAllCompanies(dfe.selectionSet.toDataSelection())
        )
            .map(mapper::toCompanyDtos)
            .onFailure { e -> logger.error(e.stackTraceToString()) }
    }
}