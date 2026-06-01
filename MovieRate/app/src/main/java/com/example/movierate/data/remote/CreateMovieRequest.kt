package com.example.movierate.data.remote

data class CreateMovieRequest(
    val title: String,
    val description: String?,
    val releaseYear: Int,
    val type: String,
    val genreIds: List<Long>? = null,
    val imageUrl: String? = null
)
