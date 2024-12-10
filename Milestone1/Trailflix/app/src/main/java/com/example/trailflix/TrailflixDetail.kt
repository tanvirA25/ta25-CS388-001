package com.example.trailflix

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.flixter.MOVIE_EXTRA
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import okhttp3.Headers
import org.json.JSONArray

private const val TAG = "DetailActivity"

class TrailflixDetail : AppCompatActivity() {

    private lateinit var description: TextView
    private lateinit var content: TrailflixItem
    private lateinit var rating: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_detail)

        val movie = intent.getStringExtra(MOVIE_EXTRA)
        content = Gson().fromJson(movie, TrailflixItem::class.java)

        val id = content.id.toString()

        fetchTrailer("movie", id)
        fetchTrailer("tv", id)
        fetchActors("movie", id)
        fetchActors("tv", id)
        fetchDirectors("movie", id)
        fetchDirectors("tv", id)
        fetchWriters("movie", id)
        fetchWriters("tv", id)


        description = findViewById(R.id.description)
        description.text = content.description.toString()

        rating = findViewById(R.id.ratingText)
        rating.text = content.vote.toString()

        val add = findViewById<ImageView>(R.id.add)
        add.setOnClickListener {
            val type = content.media_type
            if (type != null) {
                addToWishlist(content, type)
            }
        }


    }

    private fun addToWishlist(content: TrailflixItem, type:String){
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val repository = TrailflixRepository(this)
        lifecycleScope.launch {
            try {
                val isAdded = if (type == "movie") {
                    repository.addToMovieList(content.id.toString(), content.title.toString())
                } else if (type == "tv") {
                    repository.addToTvShowList(content.id.toString(), content.name ?: "Unknown Title")
                } else {
                    false
                }

                if (isAdded) {
                    Toast.makeText(this@TrailflixDetail, "Added to $type list!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@TrailflixDetail, "$type already exists in the list!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("TrailflixDetail", "Failed to add $type to list: ${e.message}", e)
                Toast.makeText(this@TrailflixDetail, "Failed to add to $type list.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchTrailer(type: String, movieId: String) {
        val webView = findViewById<WebView>(R.id.webView) // Ensure your layout includes this WebView
        val titleView = findViewById<TextView>(R.id.movie_title)
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = BuildConfig.api_key

        val url = "https://api.themoviedb.org/3/$type/$movieId/videos"


        client.get(url, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val resultsJSON = json.jsonObject.getJSONArray("results")
                if (resultsJSON.length() > 0) {
                    val trailerKey = resultsJSON.getJSONObject(0).getString("key")
                    val videoUrl = "https://www.youtube.com/embed/$trailerKey"

                    // Configure WebView
                    webView.settings.javaScriptEnabled = true
                    webView.setBackgroundColor(Color.TRANSPARENT)
                    webView.settings.loadWithOverviewMode = true
                    webView.settings.useWideViewPort = true
                    webView.loadUrl(videoUrl)

                    // Use `title` or `name` based on the type
                    titleView.text = if (type == "movie") content.title else content.name
                } else {
                    Log.e(TAG, "No trailers found for $type.")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                Log.e(TAG, "Error loading $type trailers: $errorResponse", t)
            }
        })
    }
    private fun fetchActors(type: String, movieId: String) {
        val recyclerView = findViewById<RecyclerView>(R.id.actor)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = BuildConfig.api_key

        val url = "https://api.themoviedb.org/3/$type/$movieId/credits"

        client.get(url, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val castJSON: JSONArray = json.jsonObject.getJSONArray("cast")
                val actors = mutableListOf<Actor>()

                for (i in 0 until castJSON.length()) {
                    val actor = castJSON.getJSONObject(i)
                    if (actor.getString("known_for_department") == "Acting") {
                        val name = actor.optString("name", "Unknown")
                        val profilePath = actor.optString("profile_path", null)

                        Log.d(TAG, "Actor Name: $name, Profile Path: $profilePath")

                        actors.add(
                            Actor(
                                actorName = name,
                                actorImageUrl = profilePath
                            )
                              )
                    }
                }

                if (actors.isNotEmpty()) {
                    recyclerView.adapter = ActorAdapter(actors)
                } else {
                    Log.e(TAG, "No actors found.")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                Log.e(TAG, "Error loading actors: $errorResponse", t)
            }
        })
    }
    private fun fetchDirectors(type: String, movieId: String) {
        val recyclerView = findViewById<RecyclerView>(R.id.director) // Ensure a separate RecyclerView for directors
        val label = findViewById<TextView>(R.id.directorText) // Label for "Director" or "Executive Producer"

        // Set label text based on type

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = BuildConfig.api_key

        val url = "https://api.themoviedb.org/3/$type/$movieId/credits"


        client.get(url, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val crewJSON: JSONArray = json.jsonObject.getJSONArray("crew")
                val directors = mutableListOf<Actor>()

                for (i in 0 until crewJSON.length()) {
                    val crewMember = crewJSON.getJSONObject(i)
                    val job = if (type == "tv") "Executive Producer" else "Director"

                    if (crewMember.getString("job") == job) { // Filter by job
                        val name = crewMember.optString("name", "Unknown")
                        val profilePath = crewMember.optString("profile_path", null)

                        Log.d(TAG, "$job Name: $name, Profile Path: $profilePath")

                        directors.add(
                            Actor(
                                actorName = name,
                                actorImageUrl = profilePath
                            )
                        )
                    }
                }

                if (directors.isNotEmpty()) {
                    recyclerView.adapter = ActorAdapter(directors)
                } else {
                    Log.e(TAG, "No $label.text found.")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                Log.e(TAG, "Error loading $label.text: $errorResponse", t)
            }
        })
    }
    private fun fetchWriters(type: String, movieId: String) {
        val writerTextView = findViewById<TextView>(R.id.writers) // TextView for displaying writers

        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = BuildConfig.api_key

        val url = "https://api.themoviedb.org/3/$type/$movieId/credits"

        client.get(url, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val crewJSON: JSONArray = json.jsonObject.getJSONArray("crew")
                val writers = mutableListOf<String>()

                for (i in 0 until crewJSON.length()) {
                    val crewMember = crewJSON.getJSONObject(i)
                    if (crewMember.getString("job") == "Writer") { // Filter by "Writer" job
                        val name = crewMember.optString("name", "Unknown")
                        writers.add(name)
                    }
                }

                if (writers.isNotEmpty()) {
                    // Join all writer names into a single string
                    val writersText = writers.joinToString(separator = ", ")
                    writerTextView.text = writersText // Update the TextView with writer names
                } else {
                    writerTextView.text = "No Writers Found" // Fallback if no writers found
                    Log.e(TAG, "No writers found.")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                Log.e(TAG, "Error loading writers: $errorResponse", t)
            }
        })
    }


}

