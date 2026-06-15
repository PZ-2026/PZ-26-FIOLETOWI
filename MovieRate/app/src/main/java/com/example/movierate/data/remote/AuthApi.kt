package com.example.movierate.data.remote

import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("/api/auth/status")
    suspend fun status(@Query("userId") userId: Long): Response<AuthResponse>

    @PUT("/api/auth/profile")
    suspend fun updateProfile(@Query("userId") userId: Long, @Body request: UpdateProfileRequest): Response<AuthResponse>
}
