package com.teasers.android.tmdb.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teasers.android.tmdb.R
import com.teasers.android.tmdb.databinding.FragmentListBinding
import com.teasers.android.tmdb.ui.adapter.MovieListAdapter
import com.teasers.android.tmdb.util.ResponseHandler
import com.teasers.android.tmdb.viewmodel.MainViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {

    private val TAG = ListFragment::class.simpleName

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    lateinit var mainViewModel: MainViewModel


    lateinit var mBinding: FragmentListBinding


    lateinit var movieListAdapter: MovieListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        mainViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()


        movieListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("movieId", it)
            }
            findNavController().navigate(
                R.id.action_list_to_detail,
                bundle
            )
        }
        initObserver()


    }

    private fun initObserver(){
        mainViewModel.movieLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is ResponseHandler.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        movieListAdapter.differ.submitList(moviesResponse.results)
                        isLastPage = mainViewModel.moviePage == moviesResponse.total_pages
                    }
                }
                is ResponseHandler.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is ResponseHandler.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {
        listProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar() {
        listProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isScrolling
            if (shouldPaginate) {
                mainViewModel.loadMovies("en-US")
                isScrolling = false
            } else {
                rvMoviesList.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setUpRecyclerView() {
        movieListAdapter = MovieListAdapter()
        rvMoviesList.apply {
            adapter = movieListAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@ListFragment.scrollListener)
        }

    }

}