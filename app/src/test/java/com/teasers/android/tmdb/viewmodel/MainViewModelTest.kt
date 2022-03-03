package com.teasers.android.tmdb.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*


@ExperimentalCoroutinesApi
class MainViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val mainCoroutineRule = MainCoroutineRule()


    private lateinit var viewModel: MainViewModel

    private lateinit var fakeMovieRepository: FakeMovieRepository

    @Before
    fun setUp() {
        fakeMovieRepository = FakeMovieRepository()
        viewModel = MainViewModel(fakeMovieRepository)
    }

    @Test
    fun testLoadMovieSuccessWithData() {
        fakeMovieRepository.isMovieListHasData = true
        viewModel.loadMovies("en")
        val value = viewModel.movieLiveData.getOrAwaitValueTest()
        Assert.assertEquals(value.data?.results?.isNotEmpty(), true)
    }

    @Test
    fun testLoadMovieSuccessWithNull() {
        fakeMovieRepository.isMovieListNull = true
        viewModel.loadMovies("en")
        val value = viewModel.movieLiveData.getOrAwaitValueTest()
        Assert.assertNull(value.data)
    }
    @Test
    fun testLoadMoviewithError() {
        fakeMovieRepository.isError = true
        runBlockingTest {
            val result = viewModel.handleMoviesResponse("en")
            Assert.assertEquals(result.message,"Couldn't reach the server , Please check your internet Connection")
        }
    }

    @Test
    fun testLoadMovieResultInError() {
        fakeMovieRepository.isError = true
        viewModel.loadMovies("en")
        val value = viewModel.movieLiveData.getOrAwaitValueTest()
        Assert.assertNull(value.data)

    }


    @Test
    fun testLoadMovieDetailSuccessWithData() {
        fakeMovieRepository.isMovieListHasData = true
        viewModel.loadMovieDetail(1)
        val value = viewModel.movieDetailLiveData.getOrAwaitValueTest()
        Assert.assertNotNull(value.data)
    }

    @Test
    fun testLoadMovieDetailSuccessWithNull() {
        mainCoroutineRule.runBlockingTest {
            fakeMovieRepository.isMovieListNull = true
            viewModel.loadMovieDetail(1)
            val value = viewModel.movieDetailLiveData.getOrAwaitValueTest()
            Assert.assertNull(value.data)
        }
    }

    @Test
    fun testLoadMovieDetailResultInError() {
        fakeMovieRepository.isError = true
        viewModel.loadMovieDetail(1)
        val value = viewModel.movieDetailLiveData.getOrAwaitValueTest()
        Assert.assertNull(value.data)

    }
}