package com.example.movierate.data.remote

data class AddListItemRequest(
    val movieId: Long,
    val position: Int? = null
)
