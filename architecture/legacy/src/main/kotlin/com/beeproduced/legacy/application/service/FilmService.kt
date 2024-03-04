package com.beeproduced.legacy.application.service

import com.beeproduced.bee.buzz.manager.EventManager
import com.beeproduced.bee.functional.extensions.com.github.michaelbull.result.andThenOnSuccess
import com.beeproduced.bee.functional.persistent.transactional.TransactionalResult
import com.beeproduced.bee.functional.result.AppResult
import com.beeproduced.bee.functional.result.errors.BadRequestError
import com.beeproduced.bee.persistent.selection.DataSelection
import com.beeproduced.legacy.application.event.CompaniesExist
import com.beeproduced.legacy.application.event.PersonsExist
import com.beeproduced.legacy.application.model.*
import com.beeproduced.legacy.application.model.input.CreateFilmInput
import com.beeproduced.legacy.application.model.input.FilmPagination
import com.beeproduced.legacy.application.model.input.UpdateFilmInput
import com.beeproduced.legacy.application.repository.FilmRepository
import com.beeproduced.legacy.application.service.mapper.FilmMapper
import com.beeproduced.legacy.application.utils.logFor
import com.github.michaelbull.result.*
import org.springframework.stereotype.Service

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
    private val dataFetcher: DataFetcherService,
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
            }.map { filmDao ->
                if (update.title != null) filmDao.title = update.title
                if (update.year != null) filmDao.year = update.year
                if (update.synopsis != null) filmDao.synopsis = update.synopsis
                if (update.runtime != null) filmDao.runtime = update.runtime
                if (update.studios != null) filmDao.studioIds = update.studios.toMutableSet()
                if (update.directors != null) filmDao.directorIds = update.directors.toMutableSet()
                if (update.cast != null) filmDao.castIds = update.cast.toMutableSet()
                mapper.toModel(filmDao)
            }.andThen { film ->
                loadTransientData(film, selection)
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
        return loadTransientData(films, selection)
    }

    @TransactionalResult(
        exceptionDescription = "Could not fetch all films",
        readOnly = true
    )
    fun getAll(selection: DataSelection): AppResult<List<Film>> {
        logger.debug("getAll({})", selection)
        val filmDaos = repository.select(selection)
        val films = filmDaos.map(mapper::toModel)
        return loadTransientData(films, selection)
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
            return loadTransientData(films, selection)
        }
        return Err(BadRequestError("Could not find all persons"))
    }

    private fun loadTransientData(films: List<Film>, selection: DataSelection): AppResult<List<Film>> = binding {
        logger.debug("loadTransientData({}, {})", films.map { it.id }, selection)
        val directorIds = films.flatMapTo(HashSet()) { it.directorIds }
        val castIds = films.flatMapTo(HashSet()) { it.castIds }
        val personIds = directorIds + castIds
        val studioIds = films.flatMapTo(HashSet()) { it.studioIds }

        val (persons, companies) = dataFetcher
            .loadTransientData(selection, personIds, studioIds)
            .bind()

        films.map { film ->
            val directors = persons.valuesFrom(film.directorIds)
            val cast = persons.valuesFrom(film.castIds)
            val studios = companies.valuesFrom(film.studioIds)

            film.copy(
                directors = directors,
                cast = cast,
                studios = studios
            )
        }
    }

    private fun loadTransientData(film: Film, selection: DataSelection): AppResult<Film> {
        return loadTransientData(listOf(film), selection)
            .map { it.first() }
    }

    private fun <K, V : Any> Map<K, V>.valuesFrom(keys: Collection<K>): Set<V> {
        return keys.mapNotNullTo(HashSet()) { key -> this[key] }
    }
}