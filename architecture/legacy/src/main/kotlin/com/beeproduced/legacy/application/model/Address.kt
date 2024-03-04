package com.beeproduced.legacy.application.model

import java.util.*

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */

typealias AddressId = UUID

data class Address(
    val id: AddressId,
    val addressLine1: String,
    val addressLine2: String?,
    val zipCode: String,
    val city: String
)