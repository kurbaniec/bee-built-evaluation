package com.beeproduced.legacy.application.repository

import com.beeproduced.bee.persistent.jpa.repository.extensions.PaginationResult
import com.beeproduced.bee.persistent.selection.DataSelection
import com.beeproduced.legacy.application.dao.*
import com.beeproduced.legacy.application.model.FilmId
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

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
        if (first == null && last == null)
            throw IllegalArgumentException("[first] or [last] must be given!")
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

// @Component
// class FilmRepository(
//     @Qualifier("mediaEntityManager") em: EntityManager
// ) : BaseDataRepository<Film, FilmId>(em) {
//
//     companion object {
//         private fun decodeCursor(c: String): Instant {
//             val tmp = String(Base64.getDecoder().decode(c))
//             val data = tmp.split("X")
//             val seconds = data.first().toLong()
//             val nano = data.last().toLong()
//             return Instant.ofEpochSecond(seconds, nano)
//         }
//
//         private fun encodeCursor(v: Film): String {
//             val c = v.addedOn
//             val tmp = "${c.epochSecond}X${c.nano}"
//             return Base64.getEncoder().encodeToString(tmp.toByteArray())
//         }
//     }
//
//     private val recentlyAddedPagination = Pagination<Film, Instant, String, Any?>(
//         repository = this,
//         orderBy = ColumnSpec(EntitySpec(Film::class.java), Film::addedOn.name),
//         cursor = Cursor(
//             decode = ::decodeCursor,
//             encode = ::encodeCursor
//         ),
//     )
//
//     fun recentlyAdded(
//         first: Int?, after: String?, last: Int?, before: String?,
//         selection: DataSelection
//     ): PaginationResult<Film, String> {
//         return if (first != null) recentlyAddedPagination.forward(first, after, null, selection)
//         else if (last != null) recentlyAddedPagination.backward(last, before, null, selection)
//         else throw PaginationException("Invalid parameters")
//     }
//
//
// }