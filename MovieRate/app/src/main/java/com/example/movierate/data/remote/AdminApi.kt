package com.example.movierate.data.remote

import retrofit2.Response
import retrofit2.http.*

interface AdminApi {
    // Users
    @GET("/api/admin/users")
    suspend fun getUsers(): Response<List<AdminUserResponse>>

    @PUT("/api/admin/users/{id}/role")
    suspend fun updateUserRole(@Path("id") userId: Long, @Body body: Map<String, String>): Response<Unit>

    @PUT("/api/admin/users/{id}/block")
    suspend fun toggleUserBlock(@Path("id") userId: Long): Response<Unit>

    @DELETE("/api/admin/users/{id}")
    suspend fun deleteUser(@Path("id") userId: Long): Response<Unit>

    // Movies
    @POST("/api/admin/movies")
    suspend fun createMovie(@Body request: CreateMovieRequest): Response<Unit>

    @PUT("/api/admin/movies/{id}")
    suspend fun updateMovie(@Path("id") movieId: Long, @Body request: CreateMovieRequest): Response<Unit>

    @DELETE("/api/admin/movies/{id}")
    suspend fun deleteMovie(@Path("id") movieId: Long): Response<Unit>

    // Reviews
    @GET("/api/admin/reviews")
    suspend fun getReviews(): Response<List<ReviewResponse>>

    @PUT("/api/admin/reviews/{id}/approve")
    suspend fun approveReview(@Path("id") reviewId: Long): Response<Unit>

    @DELETE("/api/admin/reviews/{id}")
    suspend fun deleteReview(@Path("id") reviewId: Long): Response<Unit>
}
