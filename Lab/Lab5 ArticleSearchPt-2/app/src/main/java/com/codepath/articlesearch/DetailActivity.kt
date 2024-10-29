package com.codepath.articlesearch

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

private const val TAG = "DetailActivity"

class DetailActivity : AppCompatActivity() {
    private lateinit var mediaImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var bylineTextView: TextView
    private lateinit var abstractTextView: TextView
    private lateinit var connectionStatusIcon: TextView  // Offline indicator
    private lateinit var connection: NetworkConnection
    private lateinit var dbstatus: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Find the views for the screen
        mediaImageView = findViewById(R.id.mediaImage)
        titleTextView = findViewById(R.id.mediaTitle)
        bylineTextView = findViewById(R.id.mediaByline)
        abstractTextView = findViewById(R.id.mediaAbstract)
        connectionStatusIcon = findViewById(R.id.connectionStatusIcon) // Offline indicator view

        // Get the extra from the Intent
        val article = intent.getSerializableExtra(ARTICLE_EXTRA) as DisplayArticle

        // Set the title, byline, and abstract information from the article
        titleTextView.text = article.headline
        bylineTextView.text = article.byline
        abstractTextView.text = article.abstract

        // Load the media image
        Glide.with(this)
            .load(article.mediaImageUrl)
            .into(mediaImageView)

        // Check the network status on activity start
        updateNetworkStatus()

        // Register network callback to monitor changes in network state
    }

    // Function to check and update network and change the UI for offline
    private fun updateNetworkStatus() {
        connection = NetworkConnection(this) { isOnline ->
            if (isOnline) {
                connectionStatusIcon.visibility = View.INVISIBLE
            }
            else {
                connectionStatusIcon.visibility = View.VISIBLE
            }
    }
        connection.startMonitoring()
}
    override fun onDestroy() {
        super.onDestroy()
        connection.stopMonitoring() // Stop monitoring network changes
    }
}
