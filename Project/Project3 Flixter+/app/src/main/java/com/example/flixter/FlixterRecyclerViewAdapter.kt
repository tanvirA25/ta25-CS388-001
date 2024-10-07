package com.example.flixter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FlixterRecyclerViewAdapter {

    class FlixterRecyclerViewAdapter(
        private val movies: List<Flixter>,
        private val mListener: OnListFragmentInteractionListener
    )
        : RecyclerView.Adapter<FlixterRecyclerViewAdapter.MovieViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_flixter, parent, false)
            return MovieViewHolder(view)
        }
        inner class MovieViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
            var mItem: Flixter? = null
            val mMovieTitle: TextView = mView.findViewById<View>(R.id.movie_title) as TextView
            val mMovieDescription : TextView = mView.findViewById<View>(R.id.move_description) as TextView
            val mMovieImage : ImageView = mView.findViewById<View>(R.id.movie_image) as ImageView


            override fun toString(): String {
                return mMovieTitle.toString()
            }
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            val movie = movies[position]


            holder.mItem = movie
            holder.mMovieTitle.text = movie.title
            holder.mMovieDescription.text = movie.description
            Glide.with(holder.mView)
                .load("https://image.tmdb.org/t/p/w500/"+movie.movieImageUrl)
                .placeholder(R.drawable.movies)
                .centerCrop()
                .into(holder.mMovieImage)

            holder.mView.setOnClickListener {
                holder.mItem?.let { movie ->
                    mListener.onItemClick(movie)
                }
            }
        }


        /**
         * Remember: RecyclerView adapters require a getItemCount() method.
         */
        override fun getItemCount(): Int {
            return movies.size
        }
    }
}