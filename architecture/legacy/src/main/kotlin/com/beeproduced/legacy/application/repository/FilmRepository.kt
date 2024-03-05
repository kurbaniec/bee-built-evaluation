package com.beeproduced.legacy.application.repository

import com.beeproduced.bee.persistent.selection.DataSelection
import com.beeproduced.legacy.application.dao.FilmDao
import com.beeproduced.legacy.application.model.FilmId
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */

@Repository
interface FilmRepository : JpaRepository<FilmDao, FilmId>, FilmRepositoryCustom

interface FilmRepositoryCustom {
    fun select(selection: DataSelection): List<FilmDao>
    fun selectById(id: UUID, selection: DataSelection): FilmDao?
    fun selectByIdIn(ids: Collection<UUID>, selection: DataSelection): List<FilmDao>
    fun recentlyAdded(
        first: Int?, after: Int?, last: Int?, before: Int?,
        selection: DataSelection
    ): List<FilmDao>
}

@Repository
class FilmRepositoryCustomImpl : FilmRepositoryCustom {
    @PersistenceContext
    lateinit var em: EntityManager
    override fun select(selection: DataSelection): List<FilmDao> {
        val query = buildQuery(selection)
        return em.createQuery(query).resultList
    }

    override fun selectById(id: UUID, selection: DataSelection): FilmDao? {
        return selectByIdIn(listOf(id), selection).firstOrNull()
    }

    override fun selectByIdIn(ids: Collection<UUID>, selection: DataSelection): List<FilmDao> {
        if (ids.isEmpty()) return emptyList()
        val query = buildQuery(selection) { query, root, cb ->
            if (ids.isNotEmpty()) {
                val filmIdPath = root.get<FilmId>("id")
                val inClause = cb.`in`(filmIdPath)
                ids.forEach { inClause.value(it) }
                query.where(inClause)
            }
        }
        return em.createQuery(query).resultList
    }

    override fun recentlyAdded(
        first: Int?,
        after: Int?,
        last: Int?,
        before: Int?,
        selection: DataSelection,
    ): List<FilmDao> {
        require(first != null || last != null) {
            "[first] or [last] must be given!"
        }
        val size = first ?: last ?: 0
        val offset = if (first != null && after != null) size * after
        else if (last != null && before != null) size * before
        else 0

        val query = buildQuery(selection) { query, root, cb ->
            if (first != null)
                query.orderBy(cb.asc(root.get<Instant>("addedOn")))
            else
                query.orderBy(cb.desc(root.get<Instant>("addedOn")))
        }

        val pageQuery = em.createQuery(query)
        pageQuery.maxResults = size
        pageQuery.firstResult = offset

        return pageQuery.resultList
    }

    @Suppress("UNUSED_PARAMETER")
    private fun buildQuery(
        selection: DataSelection,
        where: (query: CriteriaQuery<FilmDao>, root: Root<FilmDao>, cb: CriteriaBuilder)->Unit = { _, _, _ -> }
    ): CriteriaQuery<FilmDao> {
        val cb: CriteriaBuilder = em.criteriaBuilder
        val query: CriteriaQuery<FilmDao> = cb.createQuery(FilmDao::class.java)
        val companyRoot: Root<FilmDao> = query.from(FilmDao::class.java)

        where(query, companyRoot, cb)

        query.select(companyRoot)
        return query
    }
}