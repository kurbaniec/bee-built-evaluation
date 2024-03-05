package com.beeproduced.legacy.application.controller

import com.beeproduced.bee.buzz.manager.EventManager
import com.beeproduced.bee.functional.extensions.com.github.michaelbull.result.getDataFetcher
import com.beeproduced.bee.persistent.extensions.graphql.schema.toDataSelection
import com.beeproduced.legacy.application.controller.mapper.MediaMapper
import com.beeproduced.legacy.application.dto.FilmDto
import com.beeproduced.legacy.application.event.CreateFilm
import com.beeproduced.legacy.application.event.GetRecentlyAddedFilms
import com.beeproduced.legacy.application.event.UpdateFilm
import com.beeproduced.legacy.application.graphql.dto.AddFilm
import com.beeproduced.legacy.application.graphql.dto.EditFilm
import com.beeproduced.legacy.application.utils.logFor
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onFailure
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.execution.DataFetcherResult
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
        @InputArgument("before") before: Int?,
        @InputArgument("first") first: Int?,
        @InputArgument("after") after: Int?,
        dfe: DataFetchingEnvironment
    ): DataFetcherResult<List<FilmDto>> {
        return Ok(mapper.toPagination(first, after, last, before))
            .map { input -> GetRecentlyAddedFilms(input, dfe.selectionSet.toDataSelection()) }
            .andThen(eventManager::send)
            .map(mapper::toDto)
            .onFailure { e -> logger.error(e.stackTraceToString()) }
            .getDataFetcher()
    }

    @DgsMutation
    fun addFilm(
        input: AddFilm,
        dfe: DataFetchingEnvironment
    ): DataFetcherResult<FilmDto> {
        return Ok(mapper.toEntity(input))
            .map { create -> CreateFilm(create, dfe.selectionSet.toDataSelection()) }
            .andThen(eventManager::send)
            .map(mapper::toDto)
            .onFailure { e -> logger.error(e.stackTraceToString()) }
            .getDataFetcher()
    }

    @DgsMutation
    fun editFilm(
        input: EditFilm,
        dfe: DataFetchingEnvironment
    ): DataFetcherResult<FilmDto> {
        return Ok(mapper.toEntity(input))
            .map { edit -> UpdateFilm(edit, dfe.selectionSet.toDataSelection()) }
            .andThen(eventManager::send)
            .map(mapper::toDto)
            .onFailure { e -> logger.error(e.stackTraceToString()) }
            .getDataFetcher()
    }
}