package com.teasers.android.tmdb.data

import com.teasers.android.tmdb.data.model.MovieDetail
import com.teasers.android.tmdb.data.model.PaginatedListResponse
import com.teasers.android.tmdb.data.remote.TMDBApi
import retrofit2.Response
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val tmdbApi: TMDBApi) :Repository {

    override suspend fun getAllMovies(lang: String?, page: Int?): Response<PaginatedListResponse> {
        return tmdbApi.getAllPopularMovies(lang, page)
    }

    override suspend fun getMovieDetail(movieId: Int): Response<MovieDetail> {
        return tmdbApi.getMovieDetail(movieId, "en")
    }
}