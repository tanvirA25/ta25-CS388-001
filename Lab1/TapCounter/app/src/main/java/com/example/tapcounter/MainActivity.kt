package com.example.tapcounter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

        val textView = findViewById<TextView>(R.id.textView)
        var counter = 0

        button.setOnClickListener{
            Toast.makeText(it.context, "Clicked Button", Toast.LENGTH_SHORT).show()
            counter++
            textView.text = counter.toString()
        }

        val upgradeButton = findViewById<Button>(R.id.upgradeBtn)

        button.setOnClickListener{
            counter++
            textView.text = counter.toString()

            if (counter >= 100){
                upgradeButton.visibility = View.VISIBLE
                upgradeButton.setOnClickListener {
                    button.text  = "Add 2"

                    button.setOnClickListener {
                        counter+=2
                        textView.text = counter.toString()
                    }
                    upgradeButton.visibility = View.INVISIBLE
                }
            }
        }







    }
}