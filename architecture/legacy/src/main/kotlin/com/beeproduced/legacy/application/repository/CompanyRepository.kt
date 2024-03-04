package com.beeproduced.legacy.application.repository

import com.beeproduced.bee.persistent.jpa.repository.BaseDataRepository
import com.beeproduced.bee.persistent.selection.DataSelection
import com.beeproduced.legacy.application.dao.CompanyDao
import com.beeproduced.legacy.application.dao.CompanyMemberDao
import com.beeproduced.legacy.application.dao.PersonDao
import com.beeproduced.legacy.application.model.Company
import com.beeproduced.legacy.application.model.CompanyId
import com.beeproduced.legacy.application.model.CompanyMember
import com.beeproduced.legacy.application.model.CompanyMemberId
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.util.*

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */
// @Component
// class CompanyRepository(
//     @Qualifier("organisationEntityManager") em: EntityManager
// ) : BaseDataRepository<Company, CompanyId>(em)
//
// @Component
// class CompanyMemberRepository(
//     @Qualifier("organisationEntityManager") em: EntityManager
// ) : BaseDataRepository<CompanyMember, CompanyMemberId>(em)

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
        TODO("Not yet implemented")
    }

    override fun selectById(id: UUID, selection: DataSelection): CompanyDao? {
        TODO("Not yet implemented")
    }

    override fun selectByIdIn(ids: Collection<UUID>, selection: DataSelection): List<CompanyDao> {
        TODO("Not yet implemented")
    }
}

interface CompanyMemberRepository : JpaRepository<CompanyMemberDao, CompanyMemberId>