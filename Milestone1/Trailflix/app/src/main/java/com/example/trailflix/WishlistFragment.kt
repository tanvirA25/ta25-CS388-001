package com.example.trailflix

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flixter.TrailflixAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.trailflix.databinding.FragmentWishlistBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers

class WishlistFragment : Fragment(), OnListFragmentInteractionListener {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private val firestore = FirebaseFirestore.getInstance()
    private val apiClient = AsyncHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)

        // Set up RecyclerViews
        binding.trendingMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.trendingTvShows.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Load wishlist data
        loadWishlistData("movieList", binding.trendingMovies, "movie")
        loadWishlistData("tvShowList", binding.trendingTvShows, "tv")

        return binding.root
    }

    private fun loadWishlistData(collection: String, recyclerView: RecyclerView, contentType: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.e("WishlistFragment", "User is not logged in")
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        firestore.collection(collection)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                binding.progressBar.visibility = View.GONE

                // Check if the Firestore collection is empty
                if (documents.isEmpty) {
                    if (contentType == "movie") {
                        binding.trendingMovies.visibility = View.GONE // Hide RecyclerView
                        binding.movieText.visibility = View.GONE // Hide the header (if exists)
                    } else if (contentType == "tv") {
                        binding.trendingTvShows.visibility = View.GONE
                        binding.tvshowsText.visibility = View.GONE
                    }
                } else {
                    // Extract movie or TV show IDs and fetch details from TMDB
                    val items = mutableListOf<TrailflixItem>()
                    for (document in documents) {
                        val id = document.getString("movieId") ?: document.getString("tvShowId")
                        if (id != null) {
                            fetchDetails(id, contentType) { item ->
                                if (item != null) {
                                    items.add(item)
                                    recyclerView.adapter =
                                        TrailflixAdapter(requireContext(), items, this)
                                }
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Log.e("WishlistFragment", "Error loading wishlist from Firestore", e)
            }
    }
    private fun fetchDetails(id: String, contentType: String, callback: (TrailflixItem?) -> Unit) {
        val url = "https://api.themoviedb.org/3/$contentType/$id"
        val params = RequestParams()
        params["api_key"] = BuildConfig.api_key

        apiClient.get(url, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val gson = Gson()
                    val itemType = object : TypeToken<TrailflixItem>() {}.type
                    val item: TrailflixItem = gson.fromJson(json.jsonObject.toString(), itemType)
                    callback(item)
                } catch (e: Exception) {
                    Log.e("WishlistFragment", "Error parsing TMDB response", e)
                    callback(null)
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, t: Throwable?) {
                Log.e("WishlistFragment", "Error fetching details from TMDB", t)
                callback(null)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(item: TrailflixItem) {
        val intent = Intent(requireContext(), TrailflixDetail::class.java).apply {
            putExtra("CONTENT_ID", item.id.toString())
            putExtra("CONTENT_TYPE", if (item.name != null) "tv" else "movie")
        }
        startActivity(intent)
    }
}
