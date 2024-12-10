package com.example.trailflix

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.flixter.TrailflixAdapter
import com.example.trailflix.databinding.FragmentTrendingBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray

class TrendingFragment : Fragment(), OnListFragmentInteractionListener {

    private var _binding: FragmentTrendingBinding? = null
    private val binding get() = _binding!! // Access to the binding object

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrendingBinding.inflate(inflater, container, false)

        binding.searchIcon.setOnClickListener {
            (activity as? MainActivity)?.searchPage()
        }


        // Set up RecyclerViews
        binding.trendingMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.trendingTvShows.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Load data
        loadTrendingData(
            "https://api.themoviedb.org/3/trending/movie/day",
            binding.progressBar,
            binding.trendingMovies,
            "Movies"
        )
        loadTrendingData(
            "https://api.themoviedb.org/3/trending/tv/day",
            binding.progressBar,
            binding.trendingTvShows,
            "TV Shows"
        )

        return binding.root
    }

    private fun loadTrendingData(url: String, progressBar: ProgressBar, recyclerView: RecyclerView, contentType: String) {
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
                    TrailflixAdapter(requireContext(), models, this@TrendingFragment)

                Log.d("TrendingFragment", "$contentType loaded successfully")
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, t: Throwable?) {
                progressBar.visibility = View.GONE
                Log.e("TrendingFragment", "Error loading $contentType: $errorResponse", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }

    override fun onItemClick(item: TrailflixItem) {
        Toast.makeText(context, "test: " + item.title, Toast.LENGTH_LONG).show()
    }
}
