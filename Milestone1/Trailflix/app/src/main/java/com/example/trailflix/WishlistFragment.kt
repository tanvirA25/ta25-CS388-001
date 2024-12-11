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
        loadWatchListData("movieList", binding.trendingMovies, "movie")
        loadWatchListData("tvShowList", binding.trendingTvShows, "tv")

        return binding.root
    }

    private fun loadWatchListData(collection: String, recyclerView: RecyclerView, contentType: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User is not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        firestore.collection(collection)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                binding.progressBar.visibility = View.GONE

                if (documents.isEmpty) {
                    // Hide only the specific RecyclerView and header
                    if (contentType == "movie") {
                        binding.trendingMovies.visibility = View.GONE
                        binding.movieText.visibility = View.GONE
                    } else if (contentType == "tv") {
                        binding.trendingTvShows.visibility = View.GONE
                        binding.tvshowsText.visibility = View.GONE
                    }
                    recyclerView.adapter = null // Clear the adapter to avoid residual data
                } else {
                    val items = mutableListOf<TrailflixItem>()
                    for (document in documents) {
                        val id = document.getString("movieId") ?: document.getString("tvShowId")
                        if (id != null) {
                            fetchDetails(id, contentType) { item ->
                                if (item != null) {
                                    items.add(item)

                                    // Set the adapter with updated items
                                    recyclerView.adapter = TrailflixAdapter(requireContext(), items, this, true)
                                    recyclerView.visibility = View.VISIBLE
                                    if (contentType == "movie") binding.movieText.visibility = View.VISIBLE
                                    if (contentType == "tv") binding.tvshowsText.visibility = View.VISIBLE
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

    override fun onDeleteItem(item: TrailflixItem) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User is not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val collection = if (item.name != null) "tvShowList" else "movieList"
        val idField = if (item.name != null) "tvShowId" else "movieId"

        firestore.collection(collection)
            .whereEqualTo("userId", userId)
            .whereEqualTo(idField, item.id.toString())
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(requireContext(), "Item not found in Firestore", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in documents) {
                        firestore.collection(collection).document(document.id).delete()
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show()

                                // Reload only the affected list
                                if (collection == "movieList") {
                                    loadWatchListData("movieList", binding.trendingMovies, "movie")
                                } else if (collection == "tvShowList") {
                                    loadWatchListData("tvShowList", binding.trendingTvShows, "tv")
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("WishlistFragment", "Error deleting item", e)
                                Toast.makeText(requireContext(), "Failed to delete item", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("WishlistFragment", "Error finding item to delete", e)
                Toast.makeText(requireContext(), "Failed to find item", Toast.LENGTH_SHORT).show()
            }
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
