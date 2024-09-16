package com.example.simplecounter

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.simplecounter.R.drawable
import com.example.simplecounter.R.drawable.paw

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)

        val textView = findViewById<TextView>(R.id.textView)
        val goalNum = findViewById<TextView>(R.id.goalNum)
        var counter: Int = 98
        var goal: Int = 0
        var y: Int = 100

        button.setOnClickListener {
            Toast.makeText(it.context, "Clicked Button", Toast.LENGTH_SHORT).show()
            counter++
            textView.text = counter.toString()

            val upgradeButton = findViewById<Button>(R.id.upgradeBtn)
            val customButton = findViewById<Button>(R.id.customBtn)


                button.setOnClickListener {
                    counter++
                    textView.text = counter.toString()

                    if (counter == y) {
                        goal++
                        Toast.makeText(it.context, "Goal $goal is reached", Toast.LENGTH_SHORT).show()
                        y+=50
                        goalNum.text = y.toString()

                    }

                    if (counter >= 100) {

                        upgradeButton.visibility = View.VISIBLE
                        customButton.visibility = View.VISIBLE

                        customButton.setOnClickListener {
                            Toast.makeText(it.context, "Button Changed", Toast.LENGTH_SHORT).show()
                            button.setBackgroundResource(R.drawable.paw)
                            button.text = ""
                            customButton.visibility = View.INVISIBLE
                        }

                        upgradeButton.setOnClickListener {
                            Toast.makeText(it.context, "Taps Upgraded by 2", Toast.LENGTH_SHORT)
                                .show()

                            button.setOnClickListener {

                                counter += 2
                                textView.text = counter.toString()

                                if (counter == y) {
                                    goal++
                                    Toast.makeText(it.context, "Goal $goal is reached", Toast.LENGTH_SHORT).show()
                                    y+=50
                                    goalNum.text = y.toString()

                                }
                            }
                            upgradeButton.visibility = View.INVISIBLE

                        }


                    }
                }
            }
        }
    }