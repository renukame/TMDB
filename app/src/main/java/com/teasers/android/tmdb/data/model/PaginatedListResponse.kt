package com.teasers.android.tmdb.data.model

data class PaginatedListResponse(
    val page: Int,
    val results: MutableList<Movie>,
    val total_pages: Int,
    val total_results: Int
)