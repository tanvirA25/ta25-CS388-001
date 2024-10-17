package com.example.flixter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject





class FlixterFragment : Fragment(), OnListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_flixter_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val upcomingRecycler = view.findViewById<View>(R.id.upcoming) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        upcomingRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        nowPlaying(progressBar, recyclerView)
        upcomingMovies(progressBar, upcomingRecycler)
        return view
    }

    /*
     * Updates thea RecyclerView adapter with new data.  This is where the
     * networking magic happens!
     */
    private fun nowPlaying(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView, ) {
        progressBar.show()

        // Create and set up an AsyncHTTPClient() here
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = BuildConfig.api_key
        client[
            "https://api.themoviedb.org/3/movie/popular?&api_key=${BuildConfig.api_key}",
            params,
            object : JsonHttpResponseHandler() {

                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JSON
                ) {
                    // The wait for a response is over
                    progressBar.hide()


                    val resultsJSON: JSONArray = json.jsonObject.getJSONArray("results")

                    val gson = Gson()

                    val arrayMovietype = object : TypeToken<List<Flixter>>() {}.type

                    val models: List<Flixter> = gson.fromJson(resultsJSON.toString(), arrayMovietype) // Fix me!
                    recyclerView.adapter =
                        FlixterRecyclerViewAdapter(
                            requireContext(), models, this@FlixterFragment)

                    // Look for this in Logcat:
                    Log.d("FlixterFragment", "response successful")


                }


                /*
                 * The onFailure function gets called when
                 * HTTP response status is "4XX" (eg. 401, 403, 404)
                 */
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    t: Throwable?
                ) {
                    // The wait for a response is over
                    progressBar.hide()

                    // If the error is not null, log it!
                    t?.message?.let {
                        Log.e("FlixterFragment", errorResponse)
                    }
                }
            }]
    }
    private fun upcomingMovies(progressBar: ContentLoadingProgressBar, upcomingRecycler: RecyclerView, ) {
        progressBar.show()
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = BuildConfig.api_key
        client[
            "https://api.themoviedb.org/3/movie/upcoming?&api_key=${BuildConfig.api_key}",
            params,
            object : JsonHttpResponseHandler()
            {

                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JSON
                ) {
                    // The wait for a response is over
                    progressBar.hide()


                    val resultsJSON : JSONArray = json.jsonObject.getJSONArray("results")

                    val gson = Gson()

                    val upMovietype = object : TypeToken<List<Flixter>>() {}.type

                    val upcomingMovies : List<Flixter> = gson.fromJson(resultsJSON.toString(), upMovietype)

                    upcomingRecycler.adapter =
                        UpComingAdapter(requireContext(), upcomingMovies,
                            this@FlixterFragment)

                    // Look for this in Logcat:
                    Log.d("FlixterFragment", "response successful for upcoming movies")


                }
                /*
                 * The onFailure function gets called when
                 * HTTP response status is "4XX" (eg. 401, 403, 404)
                 */
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    t: Throwable?
                ) {
                    // The wait for a response is over
                    progressBar.hide()

                    // If the error is not null, log it!
                    t?.message?.let {
                        Log.e("FlixterFragment", errorResponse)
                    }
                }
            }]


    }

    /*
     * What happens when a particular book is clicked.
     */
    override fun onItemClick(item: Flixter) {
        Toast.makeText(context, "test: " + item.title, Toast.LENGTH_LONG).show()

    }

}

