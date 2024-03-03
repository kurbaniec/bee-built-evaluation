package com.beeproduced.legacy.application.controller

import com.beeproduced.bee.persistent.extensions.graphql.schema.toDataSelection
import com.beeproduced.bee.persistent.selection.EmptySelection
import com.beeproduced.bee.buzz.manager.EventManager
import com.beeproduced.legacy.application.controller.mapper.OrganisationMapper
import com.beeproduced.legacy.application.dto.CompanyDto
import com.beeproduced.legacy.application.dto.PersonDto
import com.beeproduced.legacy.application.event.GetCompaniesByIds
import com.beeproduced.legacy.application.event.GetPersonsByIds
import com.beeproduced.utils.logFor
import com.github.michaelbull.result.getOrElse
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onFailure
import com.netflix.graphql.dgs.DgsDataLoader
import graphql.GraphQLContext
import graphql.schema.DataFetchingEnvironment
import org.dataloader.BatchLoaderEnvironment
import org.dataloader.DataLoader
import org.dataloader.MappedBatchLoaderWithContext
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-10-02
 */

const val ContextName = "context"
fun DataFetchingEnvironment.setContext(context: DataFetchingEnvironment) {
    // Based on: https://github.com/spring-projects/spring-graphql/issues/199#issuecomment-986849470
    graphQlContext.put(ContextName, context)
}

fun BatchLoaderEnvironment.context(): DataFetchingEnvironment {
    return getContext<GraphQLContext>().get(ContextName)
}


const val PersonDataLoaderName = "Person"
fun DataFetchingEnvironment.getPersonDataLoader(): DataLoader<String, PersonDto> {
    return getDataLoader(PersonDataLoaderName)
}

@DgsDataLoader(name = PersonDataLoaderName)
class PersonDataLoader(
    private val eventManager: EventManager,
    private val mapper: OrganisationMapper
) : MappedBatchLoaderWithContext<String, PersonDto> {
    private val logger = logFor<PersonDataLoader>()

    override fun load(
        keys: Set<String>,
        environment: BatchLoaderEnvironment
    ): CompletionStage<Map<String, PersonDto>> {
        val ids = keys.map { UUID.fromString(it) }
        val context = environment.context()
        val selection = context.selectionSet.toDataSelection().subSelect(
            "**{directors,cast}"
        ) ?: EmptySelection()

        return CompletableFuture.supplyAsync {
            eventManager
                .send(GetPersonsByIds(ids, selection))
                .map(mapper::toPersonDtos)
                .map { persons -> persons.associateBy { it.id } }
                .onFailure { e -> logger.error(e.stackTraceToString()) }
                .getOrElse { null }
        }
    }
}

const val CompanyDataLoaderName = "Company"
fun DataFetchingEnvironment.getCompanyDataLoader(): DataLoader<String, CompanyDto> {
    return getDataLoader(CompanyDataLoaderName)
}

@DgsDataLoader(name = CompanyDataLoaderName)
class CompanyDataLoader(
    private val eventManager: EventManager,
    private val mapper: OrganisationMapper
) : MappedBatchLoaderWithContext<String, CompanyDto> {
    private val logger = logFor<PersonDataLoader>()

    override fun load(
        keys: Set<String>,
        environment: BatchLoaderEnvironment
    ): CompletionStage<Map<String, CompanyDto>> {
        val ids = keys.map { UUID.fromString(it) }
        val context = environment.context()
        val selection = context.selectionSet.toDataSelection().subSelect(
            "**{studios}"
        ) ?: EmptySelection()

        return CompletableFuture.supplyAsync {
            eventManager
                .send(GetCompaniesByIds(ids, selection))
                .map(mapper::toCompanyDtos)
                .map { companies -> companies.associateBy { it.id } }
                .onFailure { e -> logger.error(e.stackTraceToString()) }
                .getOrElse { null }
        }
    }
}