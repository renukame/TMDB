package com.teasers.android.tmdb.viewmodel

import com.teasers.android.tmdb.data.Repository
import com.teasers.android.tmdb.data.model.Genre
import com.teasers.android.tmdb.data.model.Movie
import com.teasers.android.tmdb.data.model.MovieDetail
import com.teasers.android.tmdb.data.model.PaginatedListResponse
import retrofit2.Response

class FakeMovieRepository : Repository {


    var isMovieListHasData: Boolean = false
    var isMovieListNull: Boolean = false
    var isError: Boolean = false


    lateinit var response: Response<PaginatedListResponse>

    lateinit var detailResponse: Response<MovieDetail>

    override suspend fun getAllMovies(lang: String?, page: Int?): Response<PaginatedListResponse> {
        val movieList = Movie(
            634649, 1,
            "/iQFcwSGbZXMkeyKrxbPnwnRo5fl.jpg", false,
            "Peter Parker is unmasked and", "2021-12-15",
            listOf(28, 12, 878), "Spider-Man: No Way Home", "en",
            "Spider-Man: No Way Home", "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            3f, 7616, false, 8.4f
        )
        when {
            isMovieListHasData -> {
                response = Response.success(PaginatedListResponse(1, mutableListOf(movieList), 100, 100))
            }
            isMovieListNull -> {
                response = Response.success(null)
            }
            isError -> {
                response = Response.error(400, null)
            }
        }
        return response
    }

    override suspend fun getMovieDetail(movieId: Int): Response<MovieDetail> {
        val movieList = MovieDetail(
            false, "/iQFcwSGbZXMkeyKrxbPnwnRo5fl.jpg", Any(),
            634649, listOf<Genre>(), "", 1, "", "", "",
            "Peter Parker is unmasked and", 3.14,
            "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg", listOf(), listOf(),
            "2021-12-15",
            28, 12, listOf(), "Spider-Man: No Way Home", "en",
            "Spider-Man: No Way Home", false,
            3f, 7616
        )
        when {
            isMovieListHasData -> {
                detailResponse = Response.success(movieList)
            }
            isMovieListNull -> {
                detailResponse = Response.success(null)
            }
            isError -> {
                detailResponse = Response.error(400, null)
            }
        }
        return detailResponse
    }
}