package com.beeproduced.legacy.application.repository

import com.beeproduced.bee.persistent.selection.DataSelection
import com.beeproduced.legacy.application.dao.PersonDao
import com.beeproduced.legacy.application.model.PersonId
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
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

class PersonRepositoryCustomImpl : PersonRepositoryCustom {
    @PersistenceContext
    lateinit var em: EntityManager
    override fun select(selection: DataSelection): List<PersonDao> {
        TODO("Not yet implemented")
    }

    override fun selectByIdIn(ids: Collection<UUID>, selection: DataSelection): List<PersonDao> {
        TODO("Not yet implemented")
    }
}