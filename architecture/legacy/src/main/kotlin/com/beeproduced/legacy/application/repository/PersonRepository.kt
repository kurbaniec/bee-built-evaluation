package com.beeproduced.legacy.application.repository

import com.beeproduced.bee.persistent.selection.DataSelection
import com.beeproduced.legacy.application.dao.AddressDao
import com.beeproduced.legacy.application.dao.CompanyDao
import com.beeproduced.legacy.application.dao.CompanyMemberDao
import com.beeproduced.legacy.application.dao.PersonDao
import com.beeproduced.legacy.application.model.PersonId
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
interface PersonRepository : JpaRepository<PersonDao, PersonId>, PersonRepositoryCustom {
    fun existsAllByIdIn(id: Collection<UUID>): Boolean
}

interface PersonRepositoryCustom {
    fun select(selection: DataSelection): List<PersonDao>
    fun selectByIdIn(ids: Collection<UUID>, selection: DataSelection): List<PersonDao>
}

@Repository
class PersonRepositoryCustomImpl : PersonRepositoryCustom {
    @PersistenceContext
    lateinit var em: EntityManager
    override fun select(selection: DataSelection): List<PersonDao> {
        val query = buildQuery(selection)
        return em.createQuery(query).resultList
    }

    override fun selectByIdIn(ids: Collection<UUID>, selection: DataSelection): List<PersonDao> {
        if (ids.isEmpty()) return emptyList()
        val query = buildQuery(selection) { query, root, cb ->
            if (ids.isNotEmpty()) {
                val personIdPath = root.get<PersonId>("id")
                val inClause = cb.`in`(personIdPath)
                ids.forEach { inClause.value(it) }
                query.where(inClause)
            }
        }
        return em.createQuery(query).resultList
    }

    private fun buildQuery(
        selection: DataSelection,
        where: (query: CriteriaQuery<PersonDao>, root: Root<PersonDao>, cb: CriteriaBuilder)->Unit = { _, _, _ -> }
    ): CriteriaQuery<PersonDao> {
        val cb: CriteriaBuilder = em.criteriaBuilder
        val query: CriteriaQuery<PersonDao> = cb.createQuery(PersonDao::class.java)
        val personRoot: Root<PersonDao> = query.from(PersonDao::class.java)

        if (selection.contains("**{memberOf}")) {
            // Fetch memberOf eagerly
            val memberOfFetch = personRoot.fetch<PersonDao, CompanyMemberDao>("memberOf", JoinType.LEFT)
            // For each member, fetch the company eagerly
            memberOfFetch.fetch<CompanyMemberDao, CompanyDao>("company", JoinType.LEFT)
        }

        if (selection.contains("**{address}")) {
            personRoot.fetch<PersonDao, AddressDao>("address", JoinType.LEFT)
        }

        where(query, personRoot, cb)

        query.select(personRoot)
        return query
    }
}