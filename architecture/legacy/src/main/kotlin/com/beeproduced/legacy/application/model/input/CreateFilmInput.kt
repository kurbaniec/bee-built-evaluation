package com.beeproduced.legacy.application.model.input

import com.beeproduced.legacy.application.model.CompanyId
import com.beeproduced.legacy.application.model.PersonId

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */
data class CreateFilmInput(
    val title: String,
    val year: Int,
    val synopsis: String,
    val runtime: Int,
    val studios: Collection<CompanyId>,
    val directors: Collection<PersonId>,
    val cast: Collection<PersonId>
)