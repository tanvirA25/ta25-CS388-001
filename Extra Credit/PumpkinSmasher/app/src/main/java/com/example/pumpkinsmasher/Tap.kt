package com.example.pumpkinsmasher


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.ethereallab.halloweenchallenge.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private val TAG = "response"
class Tap : BaseChallengeActivity() {

    private var counter = 0
    private val threshold = 100
    private val challengeGroup = "halloween2024"
    private val pumpkinId = "KkUukjcbuDKR791mXYOw"
    private val challengeCode = "ta25"
    private val totalClicks = 92
    private lateinit var job: Job
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pumpkin_smasher)

        val counterText = findViewById<TextView>(R.id.counter)

        // Initialize the SensorManager and Sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)  // Example sensor

        // Start the step counter
        autoCount(counterText)

        // Initialize the game with a /start call
    }

    private fun autoCount(counterText: TextView) {
        job = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(100)  // Simulate steps increment every second
                counter++
                counterText.text = counter.toString()

                // Trigger pumpkin generation every 100 steps
                if (counter % threshold == 0) {
                    val currentSteps = counter
                    val sensorData = generateSensorId(sensor)
                    getNewPumpkin(
                        challengeGroup,
                        challengeCode,
                        currentSteps,
                        sensorData
                    ) { result, error ->
                        if (error != null) {
                            Log.e(TAG, "Error fetching new pumpkin: $error")
                            Toast.makeText(
                                this@Tap,
                                "Error fetching new pumpkin: $error",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Parse the response to get a valid pumpkin ID
                            Toast.makeText(
                                this@Tap,
                                "Succesfully retrevied: $result",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                    startPumpkinSession(challengeGroup, challengeCode, pumpkinId) { result, error ->
                        if (error != null) {
                            Log.e(TAG, "Error fetching new pumpkin: $error")
                            Toast.makeText(
                                this@Tap,
                                "Error fetching new pumpkin: $error",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Parse the response to get a valid pumpkin ID
                            Toast.makeText(
                                this@Tap,
                                "Succesfully retrevied: $result",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }
                    smashPumpkin(
                        challengeGroup,
                        challengeCode,
                        pumpkinId,
                        totalClicks
                    ) { result, error ->
                        if (error != null) {
                            Log.e(TAG, "Error fetching new pumpkin: $error")
                            Toast.makeText(
                                this@Tap,
                                "Error fetching new pumpkin: $error",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Parse the response to get a valid pumpkin ID
                            Toast.makeText(
                                this@Tap,
                                "Succesfully retrevied $result",
                                Toast.LENGTH_SHORT
                            ).show()


                        }
                    }
                }
            }


        }

    }




    override fun onDestroy() {
        super.onDestroy()
        job.cancel()  // Stop the coroutine when the activity is destroyed
    }
}
