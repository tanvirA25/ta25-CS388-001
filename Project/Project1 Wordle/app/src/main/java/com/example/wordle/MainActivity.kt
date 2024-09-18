package com.example.wordle

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        fun checkGuess(guessWord: String, actualWord: String): String {

            var answer = ""

            for (i in 0..3) {
                answer += if (guessWord[i] == actualWord[i]) {
                    'O'
                } else if (guessWord[i] in actualWord) {
                    '+'
                } else {
                    'X'
                }

            }
            return answer

        }

        val actualWord = FourLetterWordList.getRandomFourLetterWord()

        val guessNum1 = findViewById<TextView>(R.id.guessNum1)
        val guessNum2 = findViewById<TextView>(R.id.guessNum2)
        val guessNum3 = findViewById<TextView>(R.id.guessNum3)
        val guessWord1 = findViewById<TextView>(R.id.guessWord1)
        val guessWord2 = findViewById<TextView>(R.id.guessWord2)
        val guessWord3 = findViewById<TextView>(R.id.guessWord3)
        val checkNum1 = findViewById<TextView>(R.id.check1)
        val checkNum2 = findViewById<TextView>(R.id.check2)
        val checkNum3 = findViewById<TextView>(R.id.check3)
        val checkWord1 = findViewById<TextView>(R.id.checkWord1)
        val checkWord2 = findViewById<TextView>(R.id.checkWord2)
        val checkWord3 = findViewById<TextView>(R.id.checkWord3)
        val actualWordView = findViewById<TextView>(R.id.actualWordView)
        val userInput = findViewById<EditText>(R.id.user_input)
        val star = findViewById<ImageView>(R.id.star)
        userInput.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

        val button = findViewById<Button>(R.id.button)

        var guessAmount = 3
        var count = 1

        button.setOnClickListener {

            val userGuess = userInput.text.toString().uppercase()
            println(actualWord)


            var checkWord: String

            if (guessAmount == 3) {
                guessNum1.text = "Guess #$count"
                guessWord1.text = userGuess
                checkWord = checkGuess(userGuess, actualWord)
                checkNum1.text = "Guess #$count Check"
                checkWord1.text = checkWord
                if (checkWord == "OOOO"){
                    Toast.makeText(it.context, "Guess is Correct!", Toast.LENGTH_SHORT).show()
                    star.visibility = View.VISIBLE
                    actualWordView.text = actualWord
                    button.text = "RESET"
                    button.setOnClickListener {
                        recreate()
                    }

                }


            }

            if (guessAmount == 2) {
                guessNum2.text = "Guess #$count"
                guessWord2.text = userGuess
                checkWord = checkGuess(userGuess, actualWord)
                checkNum2.text = "Guess #$count Check"
                checkWord2.text = checkWord
                if (checkWord == "OOOO"){
                    Toast.makeText(it.context, "Guess is Correct!", Toast.LENGTH_SHORT).show()
                    star.visibility = View.VISIBLE
                    actualWordView.text = actualWord
                    button.text = "RESET"
                    button.setOnClickListener {
                        recreate()
                    }
                }

            }
            if (guessAmount == 1) {

                guessNum3.text = "Guess #$count"
                guessWord3.text = userGuess
                checkWord = checkGuess(userGuess, actualWord)
                checkNum3.text = "Guess #$count Check"
                checkWord3.text = checkWord
                if (checkWord == "OOOO"){
                    Toast.makeText(it.context, "Guess is Correct!", Toast.LENGTH_SHORT).show()
                    star.visibility = View.VISIBLE
                    actualWordView.text = actualWord
                    button.text = "RESET"
                    button.setOnClickListener {
                        recreate()
                    }
                }
                else{
                    Toast.makeText(it.context, "No more guesses try again", Toast.LENGTH_LONG).show()
                    actualWordView.text = actualWord
                    button.text = "RESET"
                    button.setOnClickListener {
                        recreate()
                    }

                }


            }



            guessAmount--
            count++
            userInput.setText("")

            }

        }



    }
