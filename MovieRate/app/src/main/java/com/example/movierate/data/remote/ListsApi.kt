package com.example.movierate.data.remote

import retrofit2.Response
import retrofit2.http.*

interface ListsApi {
    @GET("/api/lists")
    suspend fun getUserLists(
        @Query("userId") userId: Long,
        @Query("type") type: String? = null
    ): Response<List<UserListResponse>>

    @GET("/api/lists/{listId}/items")
    suspend fun getListItems(@Path("listId") listId: Long): Response<List<UserListItemResponse>>

    @POST("/api/lists")
    suspend fun createList(@Body request: CreateListRequest): Response<Long>

    @POST("/api/lists/{listId}/items")
    suspend fun addMovieToList(
        @Path("listId") listId: Long,
        @Body request: AddListItemRequest
    ): Response<Unit>

    @DELETE("/api/lists/{listId}/items/{movieId}")
    suspend fun removeMovieFromList(
        @Path("listId") listId: Long,
        @Path("movieId") movieId: Long
    ): Response<Unit>

    @DELETE("/api/lists/{listId}")
    suspend fun deleteList(@Path("listId") listId: Long): Response<Unit>

    @PUT("/api/lists/{listId}/rename")
    suspend fun renameList(
        @Path("listId") listId: Long,
        @Body body: Map<String, String>
    ): Response<Unit>
}
