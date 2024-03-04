package com.beeproduced.legacy.application.dao

import com.beeproduced.legacy.application.model.CompanyId
import com.beeproduced.legacy.application.model.FilmId
import com.beeproduced.legacy.application.model.PersonId
import com.beeproduced.legacy.application.utils.UUIDSetConverter
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "films")
class FilmDao(
    @Id
    @GeneratedValue
    val id: FilmId?,
    var title: String,
    // Reserved keyword http://www.h2database.com/html/advanced.html#keywords
    @Column(name = "film_year")
    var year: Int,
    var synopsis: String,
    var runtime: Int,
    @Column(name = "studio_ids", columnDefinition = "TEXT")
    @Convert(converter = UUIDSetConverter::class)
    var studioIds: MutableSet<CompanyId>,
    @Column(name = "director_ids", columnDefinition = "TEXT")
    @Convert(converter = UUIDSetConverter::class)
    var directorIds: MutableSet<PersonId>,
    @Column(name = "cast_ids", columnDefinition = "TEXT")
    @Convert(converter = UUIDSetConverter::class)
    var castIds: MutableSet<PersonId>,
    @Column(name = "added_on")
    var addedOn: Instant
)