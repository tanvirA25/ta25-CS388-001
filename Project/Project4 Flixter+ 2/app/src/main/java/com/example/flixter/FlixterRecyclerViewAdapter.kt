package com.example.flixter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

const val MOVIE_EXTRA = "MOVIE_EXTRA"
    class FlixterRecyclerViewAdapter(
        private val context : Context,
        private val movies: List<Flixter>,
        private val mListener: OnListFragmentInteractionListener
    )
        : RecyclerView.Adapter<FlixterRecyclerViewAdapter.MovieViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_flixter, parent, false)
            return MovieViewHolder(view)
        }


        inner class MovieViewHolder(val mView: View) : RecyclerView.ViewHolder(mView){
            var mItem: Flixter? = null
            val mMovieTitle: TextView = mView.findViewById<View>(R.id.movie_title) as TextView as TextView
            val mMovieImage : ImageView = mView.findViewById<View>(R.id.movie_image) as ImageView


            override fun toString(): String {
                return mMovieTitle.toString()
            }
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            val movie = movies[position]

            holder.mItem = movie
            holder.mMovieTitle.text = movie.title


            Glide.with(holder.mView)
                .load("https://image.tmdb.org/t/p/w500/"+movie.movieImageUrl)
                .placeholder(R.drawable.movies)
                .apply(RequestOptions().transform(RoundedCorners(20)))
                .into(holder.mMovieImage)

            holder.mView.setOnClickListener {
                holder.mItem?.let { movie ->
                    mListener.onItemClick(movie)
                }
            }
            holder.mMovieImage.setOnClickListener {
                val gson = Gson()
                val Json = gson.toJson(movie)

                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(MOVIE_EXTRA, Json)
                context?.startActivity(intent)
            }


        }

        override fun getItemCount(): Int {
            return movies.size
        }


        /**
         * Remember: RecyclerView adapters require a getItemCount() method.
         */

    }
