package com.example.bitfitpt1

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class Dashboard : Fragment(R.layout.dashboard) {

    private lateinit var dateText: TextView
    private lateinit var graphContainer: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AndroidThreeTen.init(requireActivity())

        graphContainer = view.findViewById(R.id.barGraph)
        dateText = view.findViewById(R.id.date)

        date()
        loadFromDatabase()
    }

    private fun date() {
        val currentDate = LocalDate.now()
        val dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val formattedDate = currentDate.format(dateFormat)
        dateText.text = "Date: $formattedDate"
    }

    private fun loadFromDatabase() {
        // Launch a coroutine to collect data updates
        lifecycleScope.launch {
            (requireActivity().application as ItemApplication).db.itemDao().getAllItems()
                .collect { databaseList ->
                    // Map database entities to Items
                    val items = databaseList.map { entity ->
                        Item(entity.name, entity.calorie, entity.date)
                    }

                    liveData(items)
                }
        }
    }
            private fun liveData(items: List<Item>) {

                // Calculate total, average, max, and min calories
                val totalCal = items.sumOf { it.calorie }
                val averageCal = if (items.isNotEmpty()) totalCal / items.size else 0
                val maxCal = items.maxOfOrNull { it.calorie } ?: 0
                val minCal = items.minOfOrNull { it.calorie } ?: 0

                // Update the text views with the calculated values
                view?.findViewById<TextView>(R.id.average)?.text =
                    "Average Calorie: $averageCal"
                view?.findViewById<TextView>(R.id.min)?.text = "$minCal"
                view?.findViewById<TextView>(R.id.max)?.text = "$maxCal"

                // Group items by date and calculate min/max for each date
                val groupedByDate = items.groupBy { it.date }
                val dateCalories = groupedByDate.mapValues { (_, items) ->
                    val minCalorie = items.minOfOrNull { it.calorie } ?: 0
                    val maxCalorie = items.maxOfOrNull { it.calorie } ?: 0
                    minCalorie to maxCalorie
                }

                // Update the bar graph
                populateBarGraph(dateCalories)
            }



    private fun populateBarGraph(dateCalories: Map<String, Pair<Int, Int>>) {
        graphContainer.removeAllViews() // Clear old bars

        val maxYAxisValue = 500 // Maximum y-axis value
        val yAxisStep = 50     // Step size for Y-axis labels
        val barWidth = 100     // Fixed bar width
        val graphHeight = 1000 // Fixed graph height in pixels

        // Create a container for the entire graph (Y-axis + bars + X-axis labels)
        val graphLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
        }

        // Create a container for the graph (Y-axis + bars)
        val graphContentLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        // Create Y-axis with labels
        val yAxisContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                graphHeight
            )
        }

        for (i in maxYAxisValue downTo 0 step yAxisStep) {
            val yAxisLabel = TextView(requireContext()).apply {
                text = i.toString()
                textSize = 15f
                setTextColor(Color.BLACK)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0,
                    1f // Equal space between labels
                )
            }

            yAxisContainer.addView(yAxisLabel)
        }

        graphContentLayout.addView(yAxisContainer) // Add Y-axis to the graph content container

        // Create a vertical container for bars with gridlines
        val barGraphContainer = RelativeLayout(requireContext()).apply {
            layoutParams = RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                graphHeight
            )
        }

        // Add horizontal gridlines to the barGraphContainer
        for (i in 0..maxYAxisValue step yAxisStep) {
            val yPosition = graphHeight - (i.toFloat() / maxYAxisValue * graphHeight).toInt()

            val gridLine = View(requireContext()).apply {
                layoutParams = RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2 // Thickness of the gridline
                ).apply {
                    topMargin = yPosition
                }
                setBackgroundColor(Color.LTGRAY) // Set gridline color
            }

            barGraphContainer.addView(gridLine)
        }

        // Create a container for bars
        val barRowContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        // Reverse the `dateCalories` map for reversed bar rendering
        val reversedDateCalories = dateCalories.entries.reversed()
        for ((date, minMax) in reversedDateCalories) {
            val (minCalorie, maxCalorie) = minMax
            // Normalize bar heights to fit the maxYAxisValue
            val normalizedMaxBarHeight =
                (maxCalorie.toFloat() / maxYAxisValue * graphHeight).toInt()
            val normalizedMinBarHeight =
                (minCalorie.toFloat() / maxYAxisValue * graphHeight).toInt()

            // Create Max Bar
            val maxBar = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(barWidth, normalizedMaxBarHeight).apply {
                    setMargins(50, graphHeight - normalizedMaxBarHeight, 8, 0) // Adjust margins dynamically
                }
                setBackgroundColor(Color.RED)
            }

            // Create Min Bar
            val minBar = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(barWidth, normalizedMinBarHeight).apply {
                    setMargins(50, graphHeight - normalizedMinBarHeight, 8, 0)
                }
                setBackgroundColor(Color.BLUE)
            }

            // Create a container for the bars
            val barContainer = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.BOTTOM
                setPadding(0, 0, 16, 16) // Adjust padding dynamically if needed
            }

            barContainer.addView(maxBar)
            barContainer.addView(minBar)

            barRowContainer.addView(barContainer)
        }

        // Add bars to the graph container
        barGraphContainer.addView(barRowContainer)

        // Add graph content to the main graph layout
        graphContentLayout.addView(barGraphContainer)
        graphLayout.addView(graphContentLayout)

        // Create a container for X-axis labels (dates)
        val xAxisContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        for ((date, _) in reversedDateCalories) {
            val dateLabel = TextView(requireContext()).apply {
                text = date
                textSize = 15f
                setTextColor(Color.BLACK)
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, // Wrap content for the width of the date label
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(80, 0, 50, 10) // Add margins for spacing
                }
                gravity = android.view.Gravity.CENTER // Center the text within the label
            }

            xAxisContainer.addView(dateLabel)
        }

        // Add X-axis container to the main graph layout
        graphLayout.addView(xAxisContainer)

        // Add the entire graph layout to the main container
        graphContainer.addView(graphLayout)
    }
}