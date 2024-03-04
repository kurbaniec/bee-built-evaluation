package com.beeproduced.legacy.application.repository

import com.beeproduced.bee.persistent.jpa.repository.extensions.PaginationResult
import com.beeproduced.bee.persistent.selection.DataSelection
import com.beeproduced.legacy.application.dao.FilmDao
import com.beeproduced.legacy.application.model.FilmId
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
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
        first: Int?, after: String?, last: Int?, before: String?,
        selection: DataSelection
    ): PaginationResult<FilmDao, String>
}

class FilmRepositoryCustomImpl : FilmRepositoryCustom {
    @PersistenceContext
    lateinit var em: EntityManager
    override fun select(selection: DataSelection): List<FilmDao> {
        TODO("Not yet implemented")
    }

    override fun selectById(id: UUID, selection: DataSelection): FilmDao? {
        TODO("Not yet implemented")
    }

    override fun selectByIdIn(ids: Collection<UUID>, selection: DataSelection): List<FilmDao> {
        TODO("Not yet implemented")
    }

    override fun recentlyAdded(
        first: Int?,
        after: String?,
        last: Int?,
        before: String?,
        selection: DataSelection,
    ): PaginationResult<FilmDao, String> {
        TODO("Not yet implemented")
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