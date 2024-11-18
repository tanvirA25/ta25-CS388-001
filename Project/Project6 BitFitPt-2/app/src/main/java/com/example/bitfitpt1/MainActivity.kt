package com.example.bitfitpt1

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load the initial fragment
        if (savedInstanceState == null) {
            replaceFragment(MainFragment())
        }


        // Set up BottomNavigationView to switch between fragments
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(MainFragment())
                    true
                }
                R.id.add -> {
                    replaceFragment(AddItemFragment())
                    true
                }

                R.id.dashboard-> {
                    replaceFragment(Dashboard())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // Ensure this ID matches the container in activity_main.xml
            .addToBackStack(null) // Allows back navigation to previous fragment
            .commit()
    }
}
