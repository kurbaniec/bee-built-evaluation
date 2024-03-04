package com.beeproduced.legacy.application.model

import java.time.Instant
import java.util.*

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-09-27
 */

typealias FilmId = UUID

class Film(
    val id: FilmId,
    val title: String,
    val year: Int,
    val synopsis: String,
    val runtime: Int,
    val studioIds: Set<CompanyId>,
    val directorIds: Set<PersonId>,
    val castIds: Set<PersonId>,
    val addedOn: Instant
)