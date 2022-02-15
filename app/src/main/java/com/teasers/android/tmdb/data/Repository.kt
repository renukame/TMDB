package com.teasers.android.tmdb.data


import com.teasers.android.tmdb.data.model.MovieDetail
import com.teasers.android.tmdb.data.model.PaginatedListResponse

import retrofit2.Response


interface Repository {

    suspend fun getAllMovies(lang: String?, page: Int?): Response<PaginatedListResponse>

    suspend fun getMovieDetail(movieId: Int): Response<MovieDetail>
}