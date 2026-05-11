package com.example.movierate.data.remote

data class AuthResponse(
    val message: String,
    val username: String,
    val email: String,
    val role: String,
    val createdAt: String? = null
)
