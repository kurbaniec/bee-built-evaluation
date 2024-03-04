package com.beeproduced.legacy.application.model

import java.util.*

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */

typealias CompanyId = UUID

data class Company(
    val id: CompanyId,
    val name: String,
    val employees: Set<CompanyMember>?,
    val addressId: AddressId?,
    val address: Address?
)