package com.teasers.android.tmdb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teasers.android.tmdb.data.Repository
import com.teasers.android.tmdb.data.model.MovieDetail
import com.teasers.android.tmdb.data.model.PaginatedListResponse
import com.teasers.android.tmdb.util.ResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private val movieMutableLiveData: MutableLiveData<ResponseHandler<PaginatedListResponse>> =
        MutableLiveData()
    val movieLiveData: LiveData<ResponseHandler<PaginatedListResponse>> = movieMutableLiveData


    private val movieDetailMutableLiveData: MutableLiveData<ResponseHandler<MovieDetail>> =
        MutableLiveData()
    val movieDetailLiveData: LiveData<ResponseHandler<MovieDetail>> = movieDetailMutableLiveData

    var moviePage = 1
    var paginatedListResponse: PaginatedListResponse? = null

    init {
        loadMovies("en-US")
    }

    fun loadMovies(lang: String) {
        viewModelScope.launch {
            movieMutableLiveData.value = ResponseHandler.Loading()
            movieMutableLiveData.value = handleMoviesResponse(lang)
        }
    }

    private suspend fun handleMoviesResponse(lang: String): ResponseHandler<PaginatedListResponse> {
        try {
            val response = repository.getAllMovies(lang, moviePage)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    moviePage++
                    if (paginatedListResponse == null) {
                        paginatedListResponse = resultResponse
                    } else {
                        val oldMoviesResult = paginatedListResponse?.results
                        val newMovieResult = resultResponse.results
                        oldMoviesResult?.addAll(newMovieResult)
                    }
                    return ResponseHandler.Success(paginatedListResponse ?: resultResponse)
                } ?: return ResponseHandler.Error("Response body is null")
            }
            return ResponseHandler.Error(response.message())
        } catch (e: Exception) {
            return ResponseHandler.Error("Couldn't reach the server , Please check your internet Connection")
        }

    }


    fun loadMovieDetail(id: Int) {
        viewModelScope.launch {
            movieDetailMutableLiveData.value = ResponseHandler.Loading()
            movieDetailMutableLiveData.value = handleMovieDetailResponse(id)

        }
    }

    private suspend fun handleMovieDetailResponse(id: Int): ResponseHandler<MovieDetail> {
        try {
            val response = repository.getMovieDetail(id)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    return ResponseHandler.Success(resultResponse)
                } ?: return ResponseHandler.Error("Response body is null")
            }
            return ResponseHandler.Error(response.message())
        } catch (e: Exception) {
            return ResponseHandler.Error("Couldn't reach the server , Please check your internet Connection")
        }
    }
}