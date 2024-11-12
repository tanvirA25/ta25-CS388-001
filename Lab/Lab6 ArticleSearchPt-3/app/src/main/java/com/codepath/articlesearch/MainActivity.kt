package com.codepath.articlesearch

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codepath.articlesearch.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.serialization.json.Json


fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

private const val TAG = "MainActivity/"


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = ActivityMainBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)

            val fragmentManager: FragmentManager = supportFragmentManager

            // define your fragments here
            val bestSellerBooksFragment: Fragment = BestSellerBooksFragment()
            val articleListFragment: Fragment = ArticleListFragment()
            val popularListFragment: Fragment = PopularListFragment()

            val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

            // handle navigation selection
            bottomNavigationView.setOnItemSelectedListener { item ->
                lateinit var fragment: Fragment
                when (item.itemId) {
                    R.id.BestSeller_icon -> fragment = bestSellerBooksFragment
                    R.id.Article_Search_icon -> fragment = articleListFragment
                    R.id.Home_icon -> fragment = popularListFragment
                }
                replaceFragment(fragment)
                true
            }

            // Set default selection
            bottomNavigationView.selectedItemId = R.id.Home_icon
        }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.article_frame_layout, fragment)
        fragmentTransaction.commit()
    }

}
