package com.example.flixter

import android.graphics.Movie
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

private const val TAG = "DetailActivity"

class DetailActivity : AppCompatActivity(){

    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var image: ImageView
    private lateinit var release: TextView
    private lateinit var movies: Flixter
    private lateinit var vote: TextView



override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.detail_activity)

        title = findViewById(R.id.title)
        description = findViewById(R.id.description)
        image = findViewById(R.id.image)
        release = findViewById(R.id.release)
        vote = findViewById(R.id.vote)

    val movie = intent.getStringExtra(MOVIE_EXTRA)

    movies = Gson().fromJson(movie, Flixter::class.java)

    val id = movies.id.toString()
    title.text = movies.title
    title.setTextColor(ContextCompat.getColor(this, R.color.black))
    description.text = movies.description.toString()
    release.text = "Release Date "+movies.release.toString()
    vote.text = "Average Vote "+movies.vote
    Glide.with(this)
        .load("https://image.tmdb.org/t/p/w500/"+movies.movieImageUrl)
         .apply(RequestOptions().transform(RoundedCorners(20)))
        .into(image)






    }



}
