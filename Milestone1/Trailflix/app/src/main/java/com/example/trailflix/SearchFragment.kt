package com.example.trailflix

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.flixter.TrailflixAdapter
import com.example.trailflix.databinding.FragmentSearchBinding
import com.example.trailflix.databinding.FragmentTrendingBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray

class SearchFragment: Fragment(), OnListFragmentInteractionListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        // Set up RecyclerViews
        binding.trendingMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.trendingTvShows.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        binding.trendingMovies.visibility = View.GONE
        binding.trendingTvShows.visibility = View.GONE
        binding.movieText.visibility = View.GONE
        binding.tvshowsText.visibility = View.GONE

        // Set up search action
        binding.searchEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                queries()
                true
            } else {
                false
            }
        }

        return binding.root
    }



    private fun queries(){
        val query = binding.searchEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            loadSearchData("https://api.themoviedb.org/3/search/multi", binding.progressBar, query)

            // Hide the keyboard
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
        } else {
            Toast.makeText(context, "Please enter a search query", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadSearchData(url: String, progressBar: ProgressBar, query: String) {
        progressBar.visibility = View.VISIBLE

        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = BuildConfig.api_key
        params["query"] = query

        client.get(url, params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                progressBar.visibility = View.GONE
                try {
                    val resultsJSON: JSONArray = json.jsonObject.getJSONArray("results")
                    val gson = Gson()
                    val arrayMovietype = object : TypeToken<List<TrailflixItem>>() {}.type
                    val models: List<TrailflixItem> = gson.fromJson(resultsJSON.toString(), arrayMovietype)

                    processSearchResults(models)
                    Log.d("SearchFragment", "Loaded successfully")
                } catch (e: Exception) {
                    Log.e("SearchFragment", "Error parsing results", e)
                    Toast.makeText(context, "Failed to parse results", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, t: Throwable?) {
                progressBar.visibility = View.GONE
                Log.e("SearchFragment", "Error loading: $errorResponse", t)
                Toast.makeText(context, "Failed to fetch results", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun processSearchResults(items: List<TrailflixItem>) {
        val movies = mutableListOf<TrailflixItem>()
        val tvShows = mutableListOf<TrailflixItem>()

        for (item in items) {
            when (item.media_type) {
                "movie" -> movies.add(item)
                "tv" -> tvShows.add(item)
                "person" -> {
                    item.knownFor?.let { knownFor ->
                        for (knownItem in knownFor) {
                            when (knownItem.media_type) {
                                "movie" -> movies.add(knownItem)
                                "tv" -> tvShows.add(knownItem)
                            }
                        }
                    }
                }
            }
        }

        updateRecyclerViews(movies, tvShows)
    }


    private fun updateRecyclerViews(movies: List<TrailflixItem>, tvShows: List<TrailflixItem>) {
        if (movies.isNotEmpty()) {
            binding.trendingMovies.adapter = TrailflixAdapter(requireContext(), movies, this)
            binding.trendingMovies.visibility = View.VISIBLE
            binding.movieText.visibility = View.VISIBLE
        } else {
            binding.trendingMovies.visibility = View.GONE
            binding.movieText.visibility = View.GONE
        }

        if (tvShows.isNotEmpty()) {
            binding.trendingTvShows.adapter = TrailflixAdapter(requireContext(), tvShows, this)
            binding.trendingTvShows.visibility = View.VISIBLE
            binding.tvshowsText.visibility = View.VISIBLE
        } else {
            binding.trendingTvShows.visibility = View.GONE
            binding.tvshowsText.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(item: TrailflixItem) {
        Toast.makeText(context, "Selected: ${item.title ?: item.name}", Toast.LENGTH_SHORT).show()
    }
}