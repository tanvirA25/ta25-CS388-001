package com.example.bitfitpt1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddItem : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item)

        val foodInput = findViewById<EditText>(R.id.foodInput)
        val calorieInput = findViewById<EditText>(R.id.calorieInput)
        foodInput.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        calorieInput.inputType = InputType.TYPE_CLASS_NUMBER  // Setting to accept only numbers

        findViewById<Button>(R.id.recordButton).setOnClickListener {
            val foodName = foodInput.text.toString()
            val calorie = calorieInput.text.toString().toIntOrNull()

            if (foodName.isBlank() || calorie == null) {
                Toast.makeText(this, "One of the fields is missing", Toast.LENGTH_SHORT).show()
            } else {
                // Create an intent to pass back the data to MainActivity
                lifecycleScope.launch(IO) {
                    val newItem = ItemEntity(name = foodName, calorie = calorie)
                    (application as ItemApplication).db.itemDao().insert(newItem)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AddItem, "Item added", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                    }

                }
            }
        }

