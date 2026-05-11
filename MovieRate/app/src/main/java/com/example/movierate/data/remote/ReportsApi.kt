package com.example.movierate.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportsApi {
    @POST("/api/reports/movies")
    suspend fun generateMovieReport(@Body request: MovieReportRequest): Response<ResponseBody>
}
