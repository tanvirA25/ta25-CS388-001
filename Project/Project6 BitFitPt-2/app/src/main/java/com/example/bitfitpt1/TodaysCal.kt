import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TodaysCal @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val linePaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 5f
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val pointPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        isAntiAlias = true
    }

    private var calorieData: List<Pair<String, Int>> = listOf()

    fun setCaloriesData(data: List<Pair<String, Int>>) {
        calorieData = data
        invalidate() // Redraw the view with new data
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (calorieData.isEmpty()) return

        val maxCalories = calorieData.maxOf { it.second }
        val stepX = width / (calorieData.size - 1).toFloat()
        val paddingY = 100f
        val graphHeight = height - 2 * paddingY

        // Draw the line graph
        for (i in 0 until calorieData.size - 1) {
            val startX = i * stepX
            val startY = height - paddingY - (calorieData[i].second / maxCalories.toFloat()) * graphHeight
            val endX = (i + 1) * stepX
            val endY = height - paddingY - (calorieData[i + 1].second / maxCalories.toFloat()) * graphHeight

            // Draw the line between points
            canvas.drawLine(startX, startY, endX, endY, linePaint)
        }

        // Draw the points and labels
        calorieData.forEachIndexed { index, (label, calories) ->
            val x = index * stepX
            val y = height - paddingY - (calories / maxCalories.toFloat()) * graphHeight

            // Draw the point
            canvas.drawCircle(x, y, 10f, pointPaint)

            // Draw the calorie value
            canvas.drawText(calories.toString(), x, y - 20, textPaint)

            // Draw the label
            canvas.drawText(label, x - 20, height - 20f, textPaint)
        }
    }
}
