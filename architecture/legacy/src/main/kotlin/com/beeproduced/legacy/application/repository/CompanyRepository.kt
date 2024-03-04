package com.beeproduced.legacy.application.repository

import com.beeproduced.bee.persistent.jpa.repository.BaseDataRepository
import com.beeproduced.legacy.application.model.Company
import com.beeproduced.legacy.application.model.CompanyId
import com.beeproduced.legacy.application.model.CompanyMember
import com.beeproduced.legacy.application.model.CompanyMemberId
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */
@Component
class CompanyRepository(
    @Qualifier("organisationEntityManager") em: EntityManager
) : BaseDataRepository<Company, CompanyId>(em)

@Component
class CompanyMemberRepository(
    @Qualifier("organisationEntityManager") em: EntityManager
) : BaseDataRepository<CompanyMember, CompanyMemberId>(em)