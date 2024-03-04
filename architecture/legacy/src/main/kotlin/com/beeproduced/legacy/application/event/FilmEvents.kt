package com.beeproduced.legacy.application.event

import com.beeproduced.bee.persistent.jpa.repository.extensions.PaginationResult
import com.beeproduced.bee.buzz.manager.EventManager
import com.beeproduced.bee.buzz.requestHandler
import com.beeproduced.bee.functional.result.AppResult
import com.beeproduced.legacy.application.service.FilmService
import com.beeproduced.legacy.application.model.Film
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */
@Configuration
class FilmEvents(
    private val eventManager: EventManager,
    private val service: FilmService,
) {
    @PostConstruct
    private fun register() {
        eventManager.register(requestHandler(::create))
        eventManager.register(requestHandler(::update))
        eventManager.register(requestHandler(::getAll))
        eventManager.register(requestHandler(::getRecentlyAdded))
    }

    private fun create(request: CreateFilm): AppResult<Film>
        = service.create(request.create, request.selection)
    private fun update(request: UpdateFilm): AppResult<Film>
        = service.update(request.update, request.selection)
    private fun getAll(request: GetAllFilms): AppResult<Collection<Film>>
        = service.getAll(request.selection)
    private fun getRecentlyAdded(request: GetRecentlyAddedFilms): AppResult<PaginationResult<Film, String>>
        = service.getRecentlyAdded(request.pagination, request.selection)
}