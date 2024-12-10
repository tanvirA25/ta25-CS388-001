package com.example.flixter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
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
    private val context : Context,
    private val movies: List<TrailflixItem>,
    private val mListener: OnListFragmentInteractionListener
)
    : RecyclerView.Adapter<TrailflixAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_trailflix, parent, false)
        return MovieViewHolder(view)
    }


    inner class MovieViewHolder(val mView: View) : RecyclerView.ViewHolder(mView){
        var mItem: TrailflixItem? = null
        val mMovieTitle: TextView = mView.findViewById<View>(R.id.movie_title) as TextView as TextView
        val mMovieImage : ImageView = mView.findViewById<View>(R.id.movie_image) as ImageView


        override fun toString(): String {
            return mMovieTitle.toString()
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]


        holder.mItem = movie
        holder.mMovieTitle.text = movie.title?: movie.name?: "Unkown Title"


        Glide.with(holder.mView)
            .load("https://image.tmdb.org/t/p/w500/" + movie.movieImageUrl)
            .apply(RequestOptions().transform(RoundedCorners(20)))
            .into(holder.mMovieImage)


        holder.mMovieImage.setOnClickListener {
            val gson = Gson()
            val Json = gson.toJson(movie)

            val intent = Intent(context, TrailflixDetail::class.java)
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