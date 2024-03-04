package com.beeproduced.legacy.application.service.mapper

import com.beeproduced.legacy.application.dao.FilmDao
import com.beeproduced.legacy.application.model.Film
import com.beeproduced.legacy.application.model.input.CreateFilmInput
import org.springframework.stereotype.Component
import java.time.Instant

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-04
 */

@Component
class FilmMapper {

    fun toDao(input: CreateFilmInput): FilmDao {
        return FilmDao(
            id = null,
            title = input.title,
            year = input.year,
            synopsis = input.synopsis,
            runtime = input.runtime,
            studioIds = input.studios.toMutableSet(),
            directorIds = input.directors.toMutableSet(),
            castIds = input.cast.toMutableSet(),
            addedOn = Instant.now()
        )
    }

    fun toModel(dao: FilmDao): Film {
        return Film(
            id = requireNotNull(dao.id),
            title = dao.title,
            year = dao.year,
            synopsis = dao.synopsis,
            runtime = dao.runtime,
            studioIds = dao.studioIds,
            directorIds = dao.directorIds,
            castIds = dao.castIds,
            addedOn = dao.addedOn
        )
    }
}