package com.beeproduced.legacy.application.event

import com.beeproduced.bee.persistent.jpa.repository.extensions.PaginationResult
import com.beeproduced.bee.persistent.selection.DataSelection
import com.beeproduced.bee.buzz.Request
import com.beeproduced.legacy.application.model.Film
import com.beeproduced.legacy.application.model.input.CreateFilmInput
import com.beeproduced.legacy.application.model.input.FilmPagination
import com.beeproduced.legacy.application.model.input.UpdateFilmInput

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-26
 */

data class CreateFilm(
    val create: CreateFilmInput,
    val selection: DataSelection
): Request<Film>

data class UpdateFilm(
    val update: UpdateFilmInput,
    val selection: DataSelection
): Request<Film>

data class GetAllFilms(
    val selection: DataSelection
): Request<Collection<Film>>

data class GetRecentlyAddedFilms(
    val pagination: FilmPagination,
    val selection: DataSelection
): Request<PaginationResult<Film, String>>