package com.example.trailflix

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trailflix.databinding.ActivityLoginBinding
import com.example.trailflix.fragments.LoginFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default fragment to LoginFragment
        if (savedInstanceState == null) { // Avoid re-adding the fragment on orientation change
            replaceFragment(LoginFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // Use the ID from the FragmentContainerView
            .commit()
    }
}