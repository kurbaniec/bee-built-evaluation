package com.beeproduced.legacy.application.service.mapper

import com.beeproduced.legacy.application.dao.CompanyMemberDao
import com.beeproduced.legacy.application.model.CompanyMember
import com.beeproduced.legacy.application.utils.CycleAvoidingMappingContext
import org.hibernate.Hibernate
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-04
 */

@Component
class CompanyMemberMapper(
    @Lazy private val personMapper: PersonMapper,
    @Lazy private val companyMapper: CompanyMapper
) {

    fun toModel(
        daos: Collection<CompanyMemberDao>,
        context: CycleAvoidingMappingContext
    ): Set<CompanyMember> {
        if (daos.isEmpty()) return emptySet()
        if (daos.any {
                context.hasVisited(CompanyMember::class.java, Pair(it.companyId, it.personId))
            }) return emptySet()
        for (dao in daos)
            context.addVisited(CompanyMember::class.java, Pair(dao.companyId, dao.personId))
        return daos.mapTo(HashSet()) { toModel(it, context) }
    }

    fun toModel(
        dao: CompanyMemberDao,
        context: CycleAvoidingMappingContext
    ): CompanyMember {
        val person = if (Hibernate.isInitialized(dao.person) && dao.person != null)
            personMapper.toModel(requireNotNull(dao.person), context)
        else null
        val company = if (Hibernate.isInitialized(dao.company) && dao.company != null)
            companyMapper.toModel(requireNotNull(dao.company), context)
        else null

        return CompanyMember(
            companyId = dao.companyId,
            personId = dao.personId,
            company = company,
            person = person
        )
    }
}