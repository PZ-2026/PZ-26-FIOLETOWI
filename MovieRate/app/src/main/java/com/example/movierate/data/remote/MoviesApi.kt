package com.example.movierate.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    @GET("/api/movies")
    suspend fun getMovies(): Response<List<MovieDto>>

    @GET("/api/movies/top-rated")
    suspend fun getTopRatedMovies(@Query("limit") limit: Int = 10): Response<List<MovieDto>>
}
