package com.beeproduced.legacy.application.repository

import com.beeproduced.bee.persistent.selection.DataSelection
import com.beeproduced.legacy.application.dao.AddressDao
import com.beeproduced.legacy.application.dao.CompanyDao
import com.beeproduced.legacy.application.dao.CompanyMemberDao
import com.beeproduced.legacy.application.dao.PersonDao
import com.beeproduced.legacy.application.model.CompanyId
import com.beeproduced.legacy.application.model.CompanyMemberId
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */

@Repository
interface CompanyRepository : JpaRepository<CompanyDao, CompanyId>, CompanyRepositoryCustom {
    fun existsAllByIdIn(id: Collection<UUID>): Boolean
}

interface CompanyRepositoryCustom {
    fun select(selection: DataSelection): List<CompanyDao>
    fun selectById(id: UUID, selection: DataSelection): CompanyDao?
    fun selectByIdIn(ids: Collection<UUID>, selection: DataSelection): List<CompanyDao>
}

@Repository
class CompanyRepositoryCustomImpl : CompanyRepositoryCustom {
    @PersistenceContext
    lateinit var em: EntityManager
    override fun select(selection: DataSelection): List<CompanyDao> {
        val query = buildQuery(selection)
        return em.createQuery(query).resultList
    }

    override fun selectById(id: UUID, selection: DataSelection): CompanyDao? {
        return selectByIdIn(listOf(id), selection).firstOrNull()
    }

    override fun selectByIdIn(ids: Collection<UUID>, selection: DataSelection): List<CompanyDao> {
        if (ids.isEmpty()) return emptyList()
        val query = buildQuery(selection) { query, root, cb ->
            if (ids.isNotEmpty()) {
                val personIdPath = root.get<CompanyId>("id")
                val inClause = cb.`in`(personIdPath)
                ids.forEach { inClause.value(it) }
                query.where(inClause)
            }
        }
        return em.createQuery(query).resultList
    }

    private fun buildQuery(
        selection: DataSelection,
        where: (query: CriteriaQuery<CompanyDao>, root: Root<CompanyDao>, cb: CriteriaBuilder)->Unit = {_, _, _ -> }
    ): CriteriaQuery<CompanyDao> {
        val cb: CriteriaBuilder = em.criteriaBuilder
        val query: CriteriaQuery<CompanyDao> = cb.createQuery(CompanyDao::class.java)
        val companyRoot: Root<CompanyDao> = query.from(CompanyDao::class.java)

        if (selection.contains("**{employees}")) {
            // Fetch employees eagerly
            val memberOfFetch = companyRoot.fetch<CompanyDao, CompanyMemberDao>("employees", JoinType.LEFT)
            // For each member, fetch the person eagerly
            memberOfFetch.fetch<CompanyMemberDao, PersonDao>("person", JoinType.LEFT)
        }

        if (selection.contains("**{address}")) {
            companyRoot.fetch<CompanyDao, AddressDao>("address", JoinType.LEFT)
        }

        where(query, companyRoot, cb)

        query.select(companyRoot)
        return query
    }
}

@Repository
interface CompanyMemberRepository : JpaRepository<CompanyMemberDao, CompanyMemberId>