package com.example.trailflix

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class AuthenticationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState:  Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val curentUser = auth.currentUser


        if (curentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
        else {
            startActivity(Intent(this, LoginActivity::class.java))

        }

        finish()

    }
}
