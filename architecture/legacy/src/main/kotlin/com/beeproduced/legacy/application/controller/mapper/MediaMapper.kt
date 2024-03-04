package com.beeproduced.legacy.application.controller.mapper

import com.beeproduced.legacy.application.dto.AddFilmDto
import com.beeproduced.legacy.application.dto.EditFilmDto
import com.beeproduced.legacy.application.dto.FilmDto
import com.beeproduced.legacy.application.model.Film
import com.beeproduced.legacy.application.model.input.CreateFilmInput
import com.beeproduced.legacy.application.model.input.FilmPagination
import com.beeproduced.legacy.application.model.input.UpdateFilmInput
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-10-02
 */
@Mapper
abstract class MediaMapper {

    @Mapping(target = "studios", ignore = true)
    @Mapping(target = "directors", ignore = true)
    @Mapping(target = "cast", ignore = true)
    abstract fun toDto(entity: Film): FilmDto

    fun toDto(entities: Collection<Film>): List<FilmDto> {
        return entities.map(::toDto)
    }

    fun toPagination(
        first: Int?,
        after: Int?,
        last: Int?,
        before: Int?
    ): FilmPagination {
        return FilmPagination(
            first = first,
            after = after,
            last = last,
            before = before
        )
    }

    @Mapping(target = "studios", source = "studioIds")
    @Mapping(target = "directors", source = "directorIds")
    @Mapping(target = "cast", source = "castIds")
    abstract fun toEntity(dto: AddFilmDto): CreateFilmInput
    @Mapping(target = "studios", source = "studioIds")
    @Mapping(target = "directors", source = "directorIds")
    @Mapping(target = "cast", source = "castIds")
    abstract fun toEntity(dto: EditFilmDto): UpdateFilmInput
}