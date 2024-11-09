package com.example.pumpkinsmasher

import android.hardware.Sensor
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException

abstract class BaseChallengeActivity : AppCompatActivity() {

    companion object {
        private const val BASE_URL = "https://pumpkin-smasher-2024-433fd0b9e948.herokuapp.com/api"
        private const val TAG = "BaseChallengeActivity"
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun generateSensorId(sensor: Sensor?): String {
        if (isRunningOnEmulator()) {
            return "1-Google-1"
        }
        return sensor?.let { "${it.type}-${it.vendor}-${it.version}" } ?: "Unknown-Sensor"
    }
    private fun isRunningOnEmulator(): Boolean {
        return Build.FINGERPRINT.contains("generic") || Build.MODEL.contains("Emulator") || Build.MANUFACTURER.contains("Google")
    }
    private suspend fun performPostRequest(endpoint: String, jsonBody: JSONObject): Pair<String?, String?> {
        val url = "$BASE_URL/$endpoint"
        val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder().url(url).post(requestBody).build()

        Log.d(TAG, "POST Request to $url with body: $jsonBody")

        return withContext(Dispatchers.IO) {
            try {
                client.newCall(request).execute().use { response ->
                    val responseData = response.body?.string()
                    if (response.isSuccessful) {
                        Log.d(TAG, "POST Response from $url: $responseData")
                        Pair(responseData, null)
                    } else {
                        val error = "Error: ${response.message} (code ${response.code})"
                        Log.e(TAG, "POST Error from $url: $error")
                        Pair(null, error)
                    }
                }
            } catch (e: IOException) {
                val errorMessage = "Network error: ${e.localizedMessage}"
                Log.e(TAG, "POST Network Error to $url: $errorMessage")
                Pair(null, errorMessage)
            }
        }
    }

    private suspend fun performGetRequest(endpoint: String): Pair<String?, String?> {
        val url = "$BASE_URL/$endpoint"
        val request = Request.Builder().url(url).get().build()

        Log.d(TAG, "GET Request to $url")

        return withContext(Dispatchers.IO) {
            try {
                client.newCall(request).execute().use { response ->
                    val responseData = response.body?.string()
                    if (response.isSuccessful) {
                        Log.d(TAG, "GET Response from $url: $responseData")
                        Pair(responseData, null)
                    } else {
                        val error = "Error: ${response.message} (code ${response.code})"
                        Log.e(TAG, "GET Error from $url: $error")
                        Pair(null, error)
                    }
                }
            } catch (e: IOException) {
                val errorMessage = "Network error: ${e.localizedMessage}"
                Log.e(TAG, "GET Network Error to $url: $errorMessage")
                Pair(null, errorMessage)
            }
        }
    }


    protected open fun registerUser(challengeGroup: String, challengeCode: String, onResult: (String?, String?) -> Unit) {
        val endpoint = "$challengeGroup/register"
        val jsonBody = JSONObject().apply {
            put("challengeCode", challengeCode)
        }
        Log.d(TAG, "Register User called with challengeGroup: $challengeGroup, challengeCode: $challengeCode")

        CoroutineScope(Dispatchers.Main).launch {
            val result = performPostRequest(endpoint, jsonBody)
            onResult(result.first, result.second)
        }
    }

    protected fun getNewPumpkin(challengeGroup: String, challengeCode: String, currentSteps: Int, sensorData: String, onResult: (String?, String?) -> Unit) {
        val endpoint = "$challengeGroup/pumpkins/new"
        val jsonBody = JSONObject().apply {
            put("challengeCode", challengeCode)
            put("currentSteps", currentSteps)
            put("sensorData", sensorData)
        }
        Log.d(TAG, "Get New Pumpkin called with challengeGroup: $challengeGroup, challengeCode: $challengeCode, currentSteps: $currentSteps, sensorData: $sensorData")

        CoroutineScope(Dispatchers.Main).launch {
            val result = performPostRequest(endpoint, jsonBody)
            onResult(result.first, result.second)
        }
    }

    protected fun startPumpkinSession(challengeGroup: String, challengeCode: String, pumpkinId: String, onResult: (String?, String?) -> Unit) {
        val endpoint = "$challengeGroup/pumpkins/start"
        val jsonBody = JSONObject().apply {
            put("challengeCode", challengeCode)
            put("pumpkinId", pumpkinId)
        }
        Log.d(TAG, "Start Pumpkin Session called with challengeGroup: $challengeGroup, challengeCode: $challengeCode, pumpkinId: $pumpkinId")

        CoroutineScope(Dispatchers.Main).launch {
            val result = performPostRequest(endpoint, jsonBody)
            onResult(result.first, result.second)
        }
    }

    protected fun smashPumpkin(challengeGroup: String, challengeCode: String, pumpkinId: String, totalClicks: Int, onResult: (String?, String?) -> Unit) {
        val endpoint = "$challengeGroup/pumpkins/smash"
        val jsonBody = JSONObject().apply {
            put("challengeCode", challengeCode)
            put("pumpkinId", pumpkinId)
            put("totalClicks", totalClicks)
        }
        Log.d(TAG, "Smash Pumpkin called with challengeGroup: $challengeGroup, challengeCode: $challengeCode, pumpkinId: $pumpkinId, totalClicks: $totalClicks")

        CoroutineScope(Dispatchers.Main).launch {
            val result = performPostRequest(endpoint, jsonBody)
            onResult(result.first, result.second)
        }
    }

    protected fun getScoreboard(challengeGroup: String, challengeCode: String, onResult: (String?, String?) -> Unit) {
        val endpoint = "$challengeGroup/scoreboard?challengeCode=$challengeCode"
        Log.d(TAG, "Get Scoreboard called with challengeGroup: $challengeGroup, challengeCode: $challengeCode")

        CoroutineScope(Dispatchers.Main).launch {
            val result = performGetRequest(endpoint)
            onResult(result.first, result.second)
        }
    }

    protected fun getLast100Pumpkins(challengeGroup: String, challengeCode: String, onResult: (String?, String?) -> Unit) {
        val endpoint = "$challengeGroup/pumpkins/last100?challengeCode=$challengeCode"
        Log.d(TAG, "Get Last 100 Pumpkins called with challengeGroup: $challengeGroup, challengeCode: $challengeCode")

        CoroutineScope(Dispatchers.Main).launch {
            val result = performGetRequest(endpoint)
            onResult(result.first, result.second)
        }
    }
}