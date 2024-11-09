package com.example.pumpkinsmasher

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.ethereallab.halloweenchallenge.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class Tap : BaseChallengeActivity() {

    private var counter = 0
    private val threshold = 100
    private val challengeGroup = "halloween2024"
    private val challengeCode = "ta25"
    private lateinit var job: Job
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private var punkinId: String? = null

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

                    getNewPumpkin(challengeGroup, challengeCode, currentSteps, sensorData) { result, error ->
                        if (error == null && result !=null) {
                                punkinId = parsePumpkinId(result)
                                sendNotification("New pumpkin generated! Tap to play.")
                        }
                    }
                }
            }
        }
    }

    private fun parsePumpkinId(result: String?): String {
        // Example assuming result is JSON and contains a field called "id"
        return JSONObject(result).optString("id", "unknown_id")
    }

    private fun sendNotification(message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "PumpkinChannel"

        // Create NotificationChannel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Pumpkin Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }


        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra("Pumpkin_data", punkinId)  // Pass the latest pumpkin data
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build and show the notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Pumpkin Smasher")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()  // Stop the coroutine when the activity is destroyed
    }
}
