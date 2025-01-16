package com.example.kebabapp.utilities

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @POST("api/login")
    @Headers("Content-Type: application/json")
    fun loginUser(
        @Query("email") email: String,
        @Query("password") password: String,
    ): Call<LoginResponse>

    @POST("api/register")
    @Headers("Content-Type: application/json")
    fun registerUser(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("password") password: String,
    ): Call<LoginResponse>

    @GET("api/profile")
    suspend fun getProfileInfo(): Response<ProfileResponse>

    @GET("api/logout")
    fun logoutUser(): Call<LogoutResponse>
}
