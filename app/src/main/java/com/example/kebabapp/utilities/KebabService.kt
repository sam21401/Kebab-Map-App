package com.example.kebabapp.utilities

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface KebabService {
    @GET("api/kebab/showAll")
    suspend fun getAllKebabs(): Response<KebabResponse>

    @GET("api/kebab/show/{id}")
    suspend fun getBasicKebabById(
        @Path("id") kebabId: String,
    ): Response<KebabBasicResponse>

    @GET("api/kebab/details/{id}")
    suspend fun getDetailsKebabById(
        @Path("id") kebabId: String,
    ): Response<KebabDetailResponse>

    @GET("api/kebab/filter")
    suspend fun getFilteredKebabs(
        @QueryMap filters: Map<String, String>
    ): Response<KebabResponse>

}
