package com.codepath.articlesearch

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

private const val TAG = "PopularDetailActivity"

class PopularDetailActivity : AppCompatActivity() {
    private lateinit var mediaImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var bylineTextView: TextView
    private lateinit var abstractTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        mediaImageView = findViewById(R.id.mediaImage)
        titleTextView = findViewById(R.id.mediaTitle)
        bylineTextView = findViewById(R.id.mediaByline)
        abstractTextView = findViewById(R.id.mediaAbstract)

        val article = intent.getSerializableExtra(POPULAR_EXTRA) as? PopularArticle
        if (article != null) {
            // Set title, byline, and abstract information
            titleTextView.text = article.title
            bylineTextView.text = article.byline
            abstractTextView.text = article.abstract

            // Load the media image
            Glide.with(this)
                .load(article.mediaImageUrl)
                .into(mediaImageView)
        } else {
            Log.e(TAG, "Error: PopularArticle data is null")
        }

    }
}