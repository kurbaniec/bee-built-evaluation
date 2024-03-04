package com.beeproduced.legacy.application.model

import java.util.*

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */

typealias PersonId = UUID

data class Person(
    val id: PersonId,
    val firstname: String,
    val lastname: String,
    val memberOf: Set<CompanyMember>?,
    val addressId: AddressId?,
    val address: Address?
)