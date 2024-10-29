package com.codepath.articlesearch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.articlesearch.databinding.ActivityMainBinding
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException
import org.w3c.dom.Text

// Create JSON with relaxed parsing rules
fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

private const val TAG = "MainActivity/"
private const val PREFS_NAME = "ArticleSearchPrefs"
private const val KEY_CACHE_DATA = "cacheData"
// Use correct BuildConfig from your app package
private const val SEARCH_API_KEY = BuildConfig.API_KEY
private const val ARTICLE_SEARCH_URL =
    "https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=${SEARCH_API_KEY}"

class MainActivity : AppCompatActivity() {
    private val articles = mutableListOf<DisplayArticle>()
    private lateinit var articlesRecyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var connection: NetworkConnection
    private lateinit var offlineIcon: TextView
    private lateinit var dbstatus: ToggleButton
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        articlesRecyclerView = findViewById(R.id.articles)
        val articleAdapter = ArticleAdapter(this, articles)
        articlesRecyclerView.adapter = articleAdapter
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        offlineIcon = findViewById(R.id.connectionStatusIcon)
        dbstatus = findViewById(R.id.toggleButton)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        articlesRecyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            articlesRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        dbToggle(articleAdapter)
        swipe()
        getConnection(articleAdapter)
    }

    // checks database toggle setting
    private fun dbToggle(articleAdapter: ArticleAdapter){
        val isCached = sharedPreferences.getBoolean(KEY_CACHE_DATA, true)
        dbstatus.isChecked = isCached

        dbstatus.setOnCheckedChangeListener { _, isChecked->
            if(connection.isOnline()){
                if (!isChecked) {
                    loadArticleFromNetwork(articleAdapter, db = false)
                }
            }
                    lifecycleScope.launch(IO) {
                        (application as ArticleApplication).db.articleDao().deleteAll()
                
            }

        }
    }
        // checks network connection and loads from network or database
        private fun getConnection(articleAdapter: ArticleAdapter){
            dbstatus.isChecked = sharedPreferences.getBoolean(KEY_CACHE_DATA, true)
            connection = NetworkConnection(this) { isOnline ->
                runOnUiThread{
            if (isOnline) {
                offlineIcon.visibility = TextView.INVISIBLE
                loadArticleFromNetwork(articleAdapter, dbstatus.isChecked)
            }
            else {
                offlineIcon.visibility = TextView.VISIBLE
                if (sharedPreferences.getBoolean(KEY_CACHE_DATA, true)){
                    loadArticlesFromDatabase(articleAdapter)
                }
                else{
                    Toast.makeText(this, "There is no database", Toast.LENGTH_SHORT).show()
                }

            }
        }
                }
        connection.startMonitoring()
    }
    // loads from database
    private fun loadArticlesFromDatabase(articleAdapter: ArticleAdapter) {
        lifecycleScope.launch {
            (application as ArticleApplication).db.articleDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    DisplayArticle(
                        entity.headline,
                        entity.articleAbstract,
                        entity.byline,
                        entity.mediaImageUrl
                    )
                }.also { mappedList ->
                    articles.clear()
                    articles.addAll(mappedList)
                    articleAdapter.notifyDataSetChanged()
                }
            }
        }
    }
        // loads from network if no database else loads from database
        fun loadArticleFromNetwork(articleAdapter: ArticleAdapter, db: Boolean) {
            val client = AsyncHttpClient()
            client.get(ARTICLE_SEARCH_URL, object : JsonHttpResponseHandler() {
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String?,
                    throwable: Throwable?
                ) {
                    Log.e(TAG, "Failed to fetch articles: $statusCode")
                }

                override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                    Log.i(TAG, "Successfully fetched articles: $json")
                    try {
                        // TODO: Create the parsedJSON
                        val parsedJson = createJson().decodeFromString(
                            SearchNewsResponse.serializer(),
                            json.jsonObject.toString()
                        )
                        parsedJson.response?.docs?.let { list ->
                            articles.clear()
                            articles.addAll(list.map {
                                DisplayArticle(
                                    headline = it.headline?.main,
                                    abstract = it.abstract,
                                    byline = it.byline?.original,
                                    mediaImageUrl = it.mediaImageUrl
                                )
                            })
                            articleAdapter.notifyDataSetChanged()

                        // TODO: Do something with the returned json (contains article information)

                        // TODO: Save the articles and reload the screen
                            if (db) {
                                lifecycleScope.launch(IO) {
                                    (application as ArticleApplication).db.articleDao().deleteAll()
                                    (application as ArticleApplication).db.articleDao()
                                        .insertAll(list.map {
                                            ArticleEntity(
                                                headline = it.headline?.main,
                                                articleAbstract = it.abstract,
                                                byline = it.byline?.original,
                                                mediaImageUrl = it.mediaImageUrl
                                            )
                                        }
                                        ) }
                            }
                        }
                    } catch (e: JSONException) {
                        Log.e(TAG, "Exception: $e")
                    }

                }
            })
        }

    // swipe to refresh the page by sending a request to network
    private fun swipe(){
        swipeRefreshLayout.setOnRefreshListener {
            Toast.makeText(this, "Page refreshed", Toast.LENGTH_SHORT).show()
            swipeRefreshLayout.isRefreshing = false
        }
    }

 override fun onDestroy() {
    super.onDestroy()
    connection.stopMonitoring() // Stop monitoring network changes
 }
}

