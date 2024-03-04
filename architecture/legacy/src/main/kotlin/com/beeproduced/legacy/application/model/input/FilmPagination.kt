package com.beeproduced.legacy.application.model.input

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2023-10-02
 */
data class FilmPagination(
    val first: Int?,
    val after: Int?,
    val last: Int?,
    val before: Int?
)