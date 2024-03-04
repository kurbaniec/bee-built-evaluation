package com.beeproduced.legacy.application.model.input

import com.beeproduced.legacy.application.model.PersonId

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */
data class CreateCompanyInput(
    val name: String,
    val address: CreateAddressInput? = null,
    val employees: Collection<PersonId>? = null
)