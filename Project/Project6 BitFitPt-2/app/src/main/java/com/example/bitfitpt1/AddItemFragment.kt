package com.example.bitfitpt1

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class AddItemFragment : Fragment(R.layout.add_item) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AndroidThreeTen.init(requireActivity())


        val currentDate = LocalDate.now()
        val date_format = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val formatDate = currentDate.format(date_format)



        val foodInput = view.findViewById<EditText>(R.id.foodInput)
        val calorieInput = view.findViewById<EditText>(R.id.calorieInput)
        foodInput.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        calorieInput.inputType = InputType.TYPE_CLASS_NUMBER  // Setting to accept only numbers

        view.findViewById<Button>(R.id.recordButton).setOnClickListener {
            val foodName = foodInput.text.toString()
            val calorie = calorieInput.text.toString().toIntOrNull()

            if (foodName.isBlank() || calorie == null) {
                Toast.makeText(requireContext(), "One of the fields is missing", Toast.LENGTH_SHORT).show()
            } else {
                // Create an intent to pass back the data to MainActivity
                lifecycleScope.launch(IO) {
                    val newItem = ItemEntity(name = foodName, calorie = calorie, date = formatDate)
                    (requireActivity().application as ItemApplication).db.itemDao().insert(newItem)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Item added", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()

                    }
                }
                    }

                }


            }


        }

