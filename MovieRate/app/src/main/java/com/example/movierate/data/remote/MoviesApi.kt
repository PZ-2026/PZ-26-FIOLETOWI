package com.example.movierate.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("/api/movies")
    suspend fun getMovies(): Response<List<MovieDto>>

    @GET("/api/movies/top-rated")
    suspend fun getTopRatedMovies(@Query("limit") limit: Int = 10): Response<List<MovieDto>>

    @GET("/api/movies/newest")
    suspend fun getNewestMovies(@Query("limit") limit: Int = 10): Response<List<MovieDto>>

    @GET("/api/movies/by-genre")
    suspend fun getMoviesByGenre(
        @Query("genre") genre: String,
        @Query("limit") limit: Int = 10
    ): Response<List<MovieDto>>

    @GET("/api/movies/{id}/cast")
    suspend fun getMovieCast(@Path("id") movieId: Long): Response<List<CastMemberDto>>

    @GET("/api/movies/search")
    suspend fun searchMovies(
        @Query("q") query: String? = null,
        @Query("type") type: String? = null,
        @Query("year") year: Int? = null
    ): Response<List<MovieDto>>

    @GET("/api/movies/{id}/rating")
    suspend fun getUserRating(
        @Path("id") movieId: Long,
        @Query("userId") userId: Long
    ): Response<RatingResponse>

    @POST("/api/movies/{id}/rate")
    suspend fun rateMovie(
        @Path("id") movieId: Long,
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Response<RatingResponse>

    @DELETE("/api/movies/{id}/rating")
    suspend fun deleteRating(
        @Path("id") movieId: Long,
        @Query("userId") userId: Long
    ): Response<RatingResponse>

    @GET("/api/movies/{id}/reviews")
    suspend fun getMovieReviews(@Path("id") movieId: Long): Response<List<ReviewResponse>>

    @POST("/api/movies/{id}/reviews")
    suspend fun addReview(
        @Path("id") movieId: Long,
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Response<Unit>

    @DELETE("/api/movies/{id}/reviews/{reviewId}")
    suspend fun deleteReview(
        @Path("id") movieId: Long,
        @Path("reviewId") reviewId: Long,
        @Query("userId") userId: Long
    ): Response<Unit>

    @PUT("/api/movies/{id}/reviews/{reviewId}")
    suspend fun updateReview(
        @Path("id") movieId: Long,
        @Path("reviewId") reviewId: Long,
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Response<Unit>

    @GET("/api/movies/rated")
    suspend fun getUserRatedMovies(@Query("userId") userId: Long): Response<List<MovieReportItemRequest>>

    @GET("/api/movies/reviewed")
    suspend fun getUserReviewedMovies(@Query("userId") userId: Long): Response<List<MovieReportItemRequest>>
}
