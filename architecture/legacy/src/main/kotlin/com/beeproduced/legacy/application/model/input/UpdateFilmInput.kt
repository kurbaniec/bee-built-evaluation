package com.beeproduced.legacy.application.model.input

import com.beeproduced.legacy.application.model.FilmId
import com.beeproduced.legacy.application.model.CompanyId
import com.beeproduced.legacy.application.model.PersonId

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-10-02
 */
data class UpdateFilmInput(
    val id: FilmId,
    val title: String?,
    val year: Int?,
    val synopsis: String?,
    val runtime: Int?,
    val studios: Collection<CompanyId>?,
    val directors: Collection<PersonId>?,
    val cast: Collection<PersonId>?
)