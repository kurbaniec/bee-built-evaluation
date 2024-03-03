package com.beeproduced.example.application.media.service

import com.beeproduced.bee.buzz.manager.EventManager
import com.beeproduced.bee.functional.result.AppResult
import com.beeproduced.bee.persistent.extensions.graphql.schema.toDataSelection
import com.beeproduced.example.application.graphql.dto.AddFilm
import com.beeproduced.example.application.graphql.dto.EditFilm
import com.beeproduced.example.application.organisation.service.setContext
import com.beeproduced.service.media.events.CreateFilm
import com.beeproduced.service.media.events.GetRecentlyAddedFilms
import com.beeproduced.service.media.events.UpdateFilm
import com.beeproduced.utils.logFor
import com.github.michaelbull.result.*
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.relay.Connection
import graphql.schema.DataFetchingEnvironment

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */
@DgsComponent
class MediaController(
    val eventManager: EventManager,
    val mapper: MediaMapper
) {
    private val logger = logFor<MediaController>()

    @DgsQuery
    fun recentlyAddedFilms(
        @InputArgument("last") last: Int?,
        @InputArgument("before") before: String?,
        @InputArgument("first") first: Int?,
        @InputArgument("after") after: String?,
        dfe: DataFetchingEnvironment
    ): AppResult<Connection<FilmDto>> {
        return Ok(mapper.toPagination(first, after, last, before))
            .onSuccess { dfe.setContext(dfe) }
            .map { input -> GetRecentlyAddedFilms(input, dfe.selectionSet.toDataSelection()) }
            .andThen(eventManager::send)
            .map(mapper::toDto)
            .onFailure { e -> logger.error(e.stackTraceToString()) }
    }

    @DgsMutation
    fun addFilm(
        input: AddFilm,
        dfe: DataFetchingEnvironment
    ): AppResult<FilmDto> {
        return Ok(mapper.toEntity(input))
            .onSuccess { dfe.setContext(dfe) }
            .map { create -> CreateFilm(create, dfe.selectionSet.toDataSelection()) }
            .andThen(eventManager::send)
            .map(mapper::toDto)
            .onFailure { e -> logger.error(e.stackTraceToString()) }
    }

    @DgsMutation
    fun editFilm(
        input: EditFilm,
        dfe: DataFetchingEnvironment
    ): AppResult<FilmDto> {
        return Ok(mapper.toEntity(input))
            .onSuccess { dfe.setContext(dfe) }
            .map { edit -> UpdateFilm(edit, dfe.selectionSet.toDataSelection()) }
            .andThen(eventManager::send)
            .map(mapper::toDto)
            .onFailure { e -> logger.error(e.stackTraceToString()) }
    }
}