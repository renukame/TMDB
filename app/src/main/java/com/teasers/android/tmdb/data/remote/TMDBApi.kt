package com.teasers.android.tmdb.data.remote

import com.teasers.android.tmdb.data.model.MovieDetail
import com.teasers.android.tmdb.data.model.PaginatedListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {

    @GET("movie/popular")
    suspend fun getAllPopularMovies(
        @Query("language") lang: String? = null,
        @Query("page") page: Int? = null
    ): Response<PaginatedListResponse>


    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("language") lang: String? = null
    ): Response<MovieDetail>

}
