package com.beeproduced.legacy.application.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */

@Embeddable
data class CompanyMemberId(
    @Column(name = "company_id")
    val companyId: CompanyId,
    @Column(name = "person_id")
    val personId: PersonId
) : Serializable

data class CompanyMember(
    val companyId: CompanyId,
    val personId: PersonId,
    val company: Company? = null,
    val person: Person? = null,
)