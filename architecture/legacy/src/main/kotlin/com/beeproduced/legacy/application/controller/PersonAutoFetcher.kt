package com.beeproduced.legacy.application.controller

import com.beeproduced.legacy.application.dto.FilmDto
import com.beeproduced.legacy.application.dto.PersonDto
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import graphql.schema.DataFetchingEnvironment
import java.util.concurrent.CompletableFuture
import kotlin.String
import kotlin.collections.List
import org.dataloader.DataLoader

@DgsComponent
public class PersonAutoFetcher {
  @DgsData(
    parentType = "Film",
    field = "directors",
  )
  public fun filmDirectors(dfe: DataFetchingEnvironment): CompletableFuture<List<PersonDto>?> {
    val data = dfe.getSource<FilmDto>()
    if (!data.directors.isNullOrEmpty()) return CompletableFuture.completedFuture(data.directors)
    val dataLoader: DataLoader<String, PersonDto> = dfe.getDataLoader("Person")
    val ids = data.directorIds
    return dataLoader.loadMany(ids)
  }

  @DgsData(
    parentType = "Film",
    field = "cast",
  )
  public fun filmCast(dfe: DataFetchingEnvironment): CompletableFuture<List<PersonDto>?> {
    val data = dfe.getSource<FilmDto>()
    if (!data.cast.isNullOrEmpty()) return CompletableFuture.completedFuture(data.cast)
    val dataLoader: DataLoader<String, PersonDto> = dfe.getDataLoader("Person")
    val ids = data.castIds
    return dataLoader.loadMany(ids)
  }
}
