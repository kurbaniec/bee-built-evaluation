package com.beeproduced.legacy.application.service

import com.beeproduced.bee.persistent.jpa.repository.extensions.PaginationResult
import com.beeproduced.bee.persistent.selection.DataSelection
import com.beeproduced.bee.buzz.manager.EventManager
import com.beeproduced.bee.functional.result.AppResult
import com.beeproduced.bee.functional.result.errors.BadRequestError
import com.beeproduced.bee.functional.extensions.com.github.michaelbull.result.andThenOnSuccess
import com.beeproduced.bee.functional.persistent.transactional.TransactionalResult
import com.beeproduced.bee.persistent.selection.EmptySelection
import com.beeproduced.legacy.application.event.CompaniesExist
import com.beeproduced.legacy.application.event.PersonsExist
import com.beeproduced.legacy.application.repository.FilmRepository
import com.beeproduced.legacy.application.model.Film
import com.beeproduced.legacy.application.model.FilmId
import com.beeproduced.legacy.application.model.input.CreateFilmInput
import com.beeproduced.legacy.application.model.input.FilmPagination
import com.beeproduced.legacy.application.model.input.UpdateFilmInput
import com.beeproduced.legacy.application.model.CompanyId
import com.beeproduced.legacy.application.model.PersonId
import com.beeproduced.legacy.application.service.mapper.FilmMapper
import com.beeproduced.legacy.application.utils.logFor
import com.github.michaelbull.result.*
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */
@Service
class FilmService(
    private val eventManager: EventManager,
    private val repository: FilmRepository,
    private val mapper: FilmMapper
) {
    private val logger = logFor<FilmRepository>()

    @TransactionalResult(
        exceptionDescription = "Could not create film",
    )
    fun create(
        create: CreateFilmInput,
        selection: DataSelection
    ): AppResult<Film> {
        logger.debug("create({}, {})", create, selection)
        return organisationIdsExist(
            create.studios, create.directors + create.cast
        ).map {
            val filmDao = mapper.toDao(create)
            repository.save(filmDao)
            mapper.toModel(filmDao)
        }
    }

    fun organisationIdsExist(
        companyIds: Collection<CompanyId>?,
        personIds: Collection<PersonId>?
    ): AppResult<Unit> {
        val companyResult = if (companyIds.isNullOrEmpty()) Ok(Unit)
        else eventManager.send(CompaniesExist(companyIds))
        return companyResult.andThen {
            if (personIds.isNullOrEmpty()) Ok(Unit)
            else eventManager.send(PersonsExist(personIds))
        }
    }

    @TransactionalResult(
        exceptionDescription = "Could not update film",
    )
    fun update(
        update: UpdateFilmInput,
        selection: DataSelection
    ): AppResult<Film> {
        logger.debug("update({}, {})", update, selection)
        return repository.selectById(update.id, selection)
            .toResultOr { BadRequestError("No film with id ${update.id} found") }
            .andThenOnSuccess {
                val personIds = mutableListOf<PersonId>()
                    .also { update.directors?.let(it::addAll) }
                    .also { update.cast?.let(it::addAll) }
                organisationIdsExist(update.studios, personIds)
            }.map { film ->
                if (update.title != null) film.title = update.title
                if (update.year != null) film.year = update.year
                if (update.synopsis != null) film.synopsis = update.synopsis
                if (update.runtime != null) film.runtime = update.runtime
                if (update.studios != null) film.studioIds = update.studios.toMutableSet()
                if (update.directors != null) film.directorIds = update.directors.toMutableSet()
                if (update.cast != null) film.castIds = update.cast.toMutableSet()
                mapper.toModel(film)
            }
    }

    @TransactionalResult(
        exceptionDescription = "Could not fetch all films",
        readOnly = true
    )
    fun getRecentlyAdded(
        pagination: FilmPagination,
        selection: DataSelection
    ): AppResult<List<Film>> {
        val (first, after, last, before) = pagination
        val filmDaos = repository.recentlyAdded(first, after, last, before, selection)
        val films = filmDaos.map(mapper::toModel)
        // TODO: DataLoader call...
        return Ok(films)
    }

    @TransactionalResult(
        exceptionDescription = "Could not fetch all films",
        readOnly = true
    )
    fun getAll(selection: DataSelection): AppResult<List<Film>> {
        logger.debug("getAll({})", selection)
        val filmDaos = repository.select(selection)
        val films = filmDaos.map(mapper::toModel)
        return Ok(films)
    }

    @TransactionalResult(
        exceptionDescription = "Could not fetch all films",
        readOnly = true
    )
    fun getByIds(ids: Collection<FilmId>, selection: DataSelection): AppResult<List<Film>> {
        logger.debug("getByIds({}, {})", ids, selection)
        val uniqueIds = ids.toSet()
        val filmDaos = repository.selectByIdIn(uniqueIds, selection)
        if (filmDaos.count() == uniqueIds.count()) {
            val films = filmDaos.map(mapper::toModel)
            return Ok(films)
        }
        return Err(BadRequestError("Could not find all persons"))
    }
}