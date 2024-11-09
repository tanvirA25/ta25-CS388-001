package com.example.pumpkinsmasher

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ethereallab.halloweenchallenge.R

class GameActivity : BaseChallengeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game)

        val challengeGroup = "halloween2024"
        val challengeCode = "ta25"
        val pumpkinid = intent.getStringExtra("Pumpkin_id")?: ""


        startPumpkinSession(challengeGroup, challengeCode, pumpkinid) { result, error ->
            if (error != null) {
                Toast.makeText(this, "Failed to start pumpkin: $error", Toast.LENGTH_SHORT).show()
                Log.e("GameActivity", "Error starting pumpkin: $error")
            } else {
                Toast.makeText(this, "Pumpkin started: $result", Toast.LENGTH_SHORT).show()
                Log.d("GameActivity", "Pumpkin started: $result")

        }
    }



}
}