package com.example.trailflix

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.trailflix.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
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
                finish()
                return
            }
        }

        setupBottomNavigation(isGuest)

        // Default fragment loading
        replaceFragment(TrendingFragment())
    }

    fun searchPage() {
        replaceFragment(SearchFragment())
    }

    private fun setupBottomNavigation(isGuest: Boolean) {
        // Remove Wishlist option if the user is a guest or not logged in
        val bottomNavigation: BottomNavigationView = binding.bottomNavigation
        val menu = bottomNavigation.menu

        if (isGuest || auth.currentUser == null) {
            val wishlistItem: MenuItem = menu.findItem(R.id.nav_wishlist)
            wishlistItem.isVisible = false // Hide the Wishlist item
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_trending -> {
                    replaceFragment(TrendingFragment())
                    true
                }
                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                R.id.nav_toprated -> {
                    replaceFragment(TopRatedFragment())
                    true
                }
                R.id.nav_wishlist -> {
                    replaceFragment(WishlistFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    fun logout() {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun scheduleDailyNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set the time for the notification (e.g., 9:00 AM)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        // If the time has already passed, set it for the next day
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Schedule the repeating alarm
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(this, "Daily notification scheduled", Toast.LENGTH_SHORT).show()
    }

}
