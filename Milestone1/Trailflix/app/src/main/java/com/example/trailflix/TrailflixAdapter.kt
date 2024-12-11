package com.example.flixter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.trailflix.OnListFragmentInteractionListener
import com.example.trailflix.R
import com.example.trailflix.TrailflixDetail

import com.example.trailflix.TrailflixItem

const val MOVIE_EXTRA = "MOVIE_EXTRA"
class TrailflixAdapter(
    private val context: Context,
    private val movies: List<TrailflixItem>,
    private val mListener: OnListFragmentInteractionListener,
    private val showDeleteButton: Boolean

) : RecyclerView.Adapter<TrailflixAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_trailflix, parent, false)
        return MovieViewHolder(view)
    }

    inner class MovieViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mMovieTitle: TextView = mView.findViewById(R.id.movie_title)
        val mMovieImage: ImageView = mView.findViewById(R.id.movie_image)
        val deleteButton: Button = mView.findViewById(R.id.deleteButton)

        fun bind(movie: TrailflixItem) {
            mMovieTitle.text = movie.title ?: movie.name ?: "Unknown Title"

            Glide.with(mView)
                .load("https://image.tmdb.org/t/p/w500/" + movie.movieImageUrl)
                .apply(RequestOptions().transform(RoundedCorners(20)))
                .into(mMovieImage)

            mMovieImage.setOnClickListener {
                val gson = Gson()
                val json = gson.toJson(movie)

                val intent = Intent(context, TrailflixDetail::class.java)
                intent.putExtra(MOVIE_EXTRA, json)
                context.startActivity(intent)
            }

            // Show or hide delete button based on the flag
            if (showDeleteButton) {
                deleteButton.visibility = View.VISIBLE
                deleteButton.setOnClickListener {
                    mListener.onDeleteItem(movie)
                }
            } else {
                deleteButton.visibility = View.GONE
            }
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size
}