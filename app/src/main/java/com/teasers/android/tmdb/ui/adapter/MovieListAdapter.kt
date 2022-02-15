package com.teasers.android.tmdb.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teasers.android.tmdb.R
import com.teasers.android.tmdb.data.model.Movie
import com.teasers.android.tmdb.databinding.ItemMovieBinding

import com.teasers.android.tmdb.ui.adapter.MovieListAdapter.*
import com.teasers.android.tmdb.util.ApiConstants.BASE_POSTER_PATH
import kotlinx.android.synthetic.main.item_movie.view.*
import javax.inject.Inject


class MovieListAdapter @Inject constructor() : RecyclerView.Adapter<MovieViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null

    private val differCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val itemBinding = ItemMovieBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_movie,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movieItem = differ.currentList[position]
        holder.itemBinding.movie = movieItem

        holder.itemView.apply {
            Glide.with(this).load(BASE_POSTER_PATH + movieItem.poster_path)
                .into(ivMovieImage)

            setOnClickListener {
                onItemClickListener?.let { it(movieItem.id) }
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }
}


