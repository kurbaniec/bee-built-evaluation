package com.beeproduced.legacy.application.dao

import com.beeproduced.legacy.application.model.*
import jakarta.persistence.*

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-04
 */
@Entity
@IdClass(CompanyMemberId::class)
@Table(name = "company_members")
class CompanyMemberDao(
    @Id
    @Column(name = "company_id")
    val companyId: CompanyId,
    @Id
    @Column(name = "person_id")
    val personId: PersonId,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", referencedColumnName = "id", insertable = false, updatable = false)
    var company: CompanyDao? = null,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", referencedColumnName = "id", insertable = false, updatable = false)
    var person: PersonDao? = null,
)