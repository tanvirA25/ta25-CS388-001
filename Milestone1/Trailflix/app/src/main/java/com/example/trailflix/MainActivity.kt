package com.example.trailflix

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.trailflix.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "trailflix-database"
        ).build()

        // Check if the activity is started in guest mode
        val isGuest = intent.getBooleanExtra("isGuest", false)

        if (!isGuest) {
            // Regular authentication check
            if (auth.currentUser == null) {
                // User is not logged in, navigate to LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
                finish() // Finish MainActivity so the user can't go back to it
                return
            }
        }

        setupBottomNavigation()

        // Load the TrendingFragment for guest users directly
        if (isGuest) {
            replaceFragment(TrendingFragment())
        } else {
            replaceFragment(TrendingFragment()) // Default fragment for logged-in users
        }
    }

    fun searchPage(){
        replaceFragment(SearchFragment())
    }


    private fun setupBottomNavigation(){
        binding.bottomNavigation.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.nav_trending->{
                    replaceFragment(TrendingFragment())
                    true
                }
                R.id.nav_profile->{
                    replaceFragment(ProfileFragment())
                    true
                }
                R.id.nav_toprated->{
                    replaceFragment(TopRatedFragment())
                    true
                }
                R.id.nav_wishlist->{
                    replaceFragment(WishlistFragment())
                    true
                }
                else->false
            }
        }

        }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .commit()
    }
    fun logout(){
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }
    }
