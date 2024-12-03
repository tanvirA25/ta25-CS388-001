package com.example.trailflix

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val guestbutton = findViewById<Button>(R.id.guestButton)

        guestbutton.setOnClickListener(){
            val homeIntent = Intent(this, TrendingActivity::class.java)
            startActivity(homeIntent)
        }

        // Add login-specific logic here, such as handling input, button clicks, etc.
    }
}
