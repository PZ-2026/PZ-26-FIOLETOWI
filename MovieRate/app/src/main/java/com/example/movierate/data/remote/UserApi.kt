package com.example.movierate.data.remote

import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("/api/users/{id}/stats")
    suspend fun getUserStats(@Path("id") userId: Long): Response<UserStatsResponse>

    @GET("/api/users/{id}/genres")
    suspend fun getUserGenres(@Path("id") userId: Long): Response<List<GenreStatResponse>>

    @GET("/api/users/{id}/activity")
    suspend fun getUserActivity(@Path("id") userId: Long): Response<List<ActivityResponse>>
}
