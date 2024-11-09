package com.example.pumpkinsmasher

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ethereallab.halloweenchallenge.R

class MainActivity : BaseChallengeActivity()

{ override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val challengeCode = findViewById<EditText>(R.id.codeInput)
        val challengeGroup = findViewById<EditText>(R.id.groupInput)
        challengeGroup.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        challengeCode.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

        findViewById<Button>(R.id.button).setOnClickListener {
            val code = challengeCode.text.toString()
            val group = challengeGroup.text.toString()
        if (group == "halloween2024" ) {
            registerUser(group, code) { result, error ->
                if (error != null) {
                    if (error == "Error: Conflict (code 409)"){
                        Toast.makeText(this, "User Already Exist", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Tap::class.java)
                        startActivity(intent)
                        }
                    else {
                        Toast.makeText(this, "Registration failed: $error", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Registration successfull: $result", Toast.LENGTH_SHORT).show()
                    setContentView(R.layout.pumpkin_smasher)
                    startActivity(intent)
                }
            }
        }
            else
        {
            Toast.makeText(this, "Group is invalid:", Toast.LENGTH_SHORT)
                .show()
        }


        }

    }

}
