
package com.codepath.articlesearch

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "PopularListFragment"
private const val SEARCH_API_KEY = BuildConfig.API_KEY
private const val POPULAR_URL =
    "https://api.nytimes.com/svc/mostpopular/v2/viewed/1.json?api-key=${SEARCH_API_KEY}"

class PopularListFragment : Fragment() {

    private val popularArticles = mutableListOf<PopularArticle>()
    private lateinit var popularArticlesRecyclerView: RecyclerView
    private lateinit var popularAdapter: PopularAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_article_list, container, false)

        // Set up RecyclerView and Adapter
        popularArticlesRecyclerView = view.findViewById(R.id.article_recycler_view)
        popularArticlesRecyclerView.layoutManager = LinearLayoutManager(context)
        popularAdapter = PopularAdapter(view.context, popularArticles)
        popularArticlesRecyclerView.adapter = popularAdapter
        retainInstance = true

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchPopularArticles()
    }

    private fun fetchPopularArticles() {
            val client = AsyncHttpClient()

            client.get(POPULAR_URL, object : JsonHttpResponseHandler() {
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String?,
                    throwable: Throwable?
                ) {
                    Log.e(TAG, "Failed to fetch popular articles: $statusCode")
                }


                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                        Log.i(TAG, "Successfully fetched articles: $json")
                        try {
                            val parsedJson = createJson().decodeFromString(
                                PopularNewsResponse.serializer(),
                                json.jsonObject.toString()
                            )
                            parsedJson.results?.let { list ->
                                popularArticles.addAll(list)
                                popularAdapter.notifyDataSetChanged()
                            }
                        } catch (e: JSONException) {
                            Log.e(TAG, "Exception: $e")
                    }
                }
            })
    }
}
