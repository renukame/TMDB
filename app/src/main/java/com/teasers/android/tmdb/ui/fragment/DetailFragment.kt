package com.teasers.android.tmdb.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.teasers.android.tmdb.R
import com.teasers.android.tmdb.databinding.FragmentDetailBinding
import com.teasers.android.tmdb.util.ApiConstants
import com.teasers.android.tmdb.util.ResponseHandler
import com.teasers.android.tmdb.viewmodel.MainViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.detailProgressBar
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.item_movie.view.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {

    private val TAG = DetailFragment::class.simpleName
    private val args: DetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    lateinit var mainViewModel: MainViewModel

    lateinit var mBinding: FragmentDetailBinding

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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = args.movieId

        mainViewModel.loadMovieDetail(movieId)
        initObserver()

    }

    private fun showProgressBar() {
        detailProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        detailProgressBar.visibility = View.INVISIBLE
    }

    private fun initObserver() {
        mainViewModel.movieDetailLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is ResponseHandler.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        mBinding.details = moviesResponse
                        Glide.with(this)
                            .load(ApiConstants.BASE_BACKDROP_PATH + moviesResponse.backdrop_path)
                            .into(movie_backdrop)
                        Glide.with(this)
                            .load(ApiConstants.BASE_POSTER_PATH + moviesResponse.poster_path)
                            .into(movie_poster)

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


}