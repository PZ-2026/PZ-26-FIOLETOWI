package com.example.movierate.data.remote

data class AdminUserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val role: String,
    val blocked: Boolean,
    val createdAt: String
)
