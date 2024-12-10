package com.example.trailflix

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.flixter.TrailflixAdapter
import com.example.trailflix.databinding.FragmentTopratedBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray

class TopRatedFragment : Fragment(), OnListFragmentInteractionListener {

    private var _binding: FragmentTopratedBinding? = null
    private val binding get() = _binding!! // Access to the binding object

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopratedBinding.inflate(inflater, container, false)

        binding.searchIcon.setOnClickListener {
            (activity as? MainActivity)?.searchPage()
        }

        // Set up RecyclerViews
        binding.trendingMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.trendingTvShows.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Load data for movies and TV shows
        loadTopRatedData(
            "https://api.themoviedb.org/3/movie/top_rated",
            binding.progressBar,
            binding.trendingMovies,
            "Movies"
        )
        loadTopRatedData(
            "https://api.themoviedb.org/3/tv/top_rated",
            binding.progressBar,
            binding.trendingTvShows,
            "TV Shows"
        )

        return binding.root
    }

    private fun loadTopRatedData(
        url: String,
        progressBar: ProgressBar,
        recyclerView: RecyclerView,
        contentType: String
    ) {
        progressBar.visibility = View.VISIBLE

        val client = AsyncHttpClient()
        val params = RequestParams()
        val apiKey = BuildConfig.api_key
        params["api_key"] = apiKey

        client.get(url, params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                progressBar.visibility = View.GONE

                val resultsJSON: JSONArray = json.jsonObject.getJSONArray("results")
                val gson = Gson()
                val arrayMovietype = object : TypeToken<List<TrailflixItem>>() {}.type
                val models: List<TrailflixItem> =
                    gson.fromJson(resultsJSON.toString(), arrayMovietype)

                recyclerView.adapter =
                    TrailflixAdapter(requireContext(), models, this@TopRatedFragment)

                Log.d("TopRatedFragment", "$contentType loaded successfully")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                progressBar.visibility = View.GONE
                Log.e("TopRatedFragment", "Error loading $contentType: $errorResponse", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }

    override fun onItemClick(item: TrailflixItem) {
        Log.d("TopRatedFragment", "Movie clicked: id=${item.id}, title=${item.title}")

        val intent = Intent(requireContext(), TrailflixDetail::class.java).apply {
            putExtra("CONTENT_ID", item.id.toString()) // Pass movieId or tvShowId
            putExtra("CONTENT_TYPE", "movie") // Pass type based on the item
        }
        startActivity(intent)
    }
}
