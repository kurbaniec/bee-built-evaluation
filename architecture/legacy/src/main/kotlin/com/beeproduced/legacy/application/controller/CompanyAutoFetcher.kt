package com.beeproduced.legacy.application.controller

import com.beeproduced.legacy.application.dto.CompanyDto
import com.beeproduced.legacy.application.dto.FilmDto
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import graphql.schema.DataFetchingEnvironment
import org.dataloader.DataLoader
import java.util.concurrent.CompletableFuture

@DgsComponent
public class CompanyAutoFetcher {
  @DgsData(
    parentType = "Film",
    field = "studios",
  )
  public fun filmStudios(dfe: DataFetchingEnvironment): CompletableFuture<List<CompanyDto>?> {
    val data = dfe.getSource<FilmDto>()
    if (!data.studios.isNullOrEmpty()) return CompletableFuture.completedFuture(data.studios)
    val dataLoader: DataLoader<String, CompanyDto> = dfe.getDataLoader("Company")
    val ids = data.studioIds
    return dataLoader.loadMany(ids)
  }
}
