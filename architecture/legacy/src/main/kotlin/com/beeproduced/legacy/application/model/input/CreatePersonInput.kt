package com.beeproduced.legacy.application.model.input

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */
data class CreatePersonInput(
    val firstname: String,
    val lastname: String,
    val address: CreateAddressInput? = null
)