package com.example.kebabapp.utilities

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface KebabService {
    @GET("api/kebab/showAll")
    suspend fun getAllKebabs(): Response<KebabResponse>

    @GET("api/kebab/show/{id}")
    suspend fun getBasicKebabById(@Path("id") kebabId: String): Response<KebabBasicResponse>

    @GET("api/kebab/details/{id}")
    suspend fun getDetailsKebabById(@Path("id") kebabId: String): Response<KebabDetailResponse>
}
