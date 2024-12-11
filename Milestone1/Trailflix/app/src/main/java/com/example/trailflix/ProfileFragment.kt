package com.example.trailflix

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.trailflix.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "watchlist_notification_channel"
        const val NOTIFICATION_ID = 100
        const val PREFS_NAME = "TrailflixPrefs"
        const val PREFS_NOTIFICATION_KEY = "NotificationEnabled"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.email.text = currentUser.email
            binding.logoutButton.text = "LOGOUT"
            binding.logoutButton.setOnClickListener {
                (requireActivity() as? MainActivity)?.logout()
            }
        } else {
            binding.email.text = "Guest Profile"
            binding.logoutButton.text = "LOGIN"
            binding.logoutButton.setOnClickListener {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
        }

        setupNotificationSwitch(currentUser)

        return binding.root
    }

    private fun setupNotificationSwitch(currentUser: FirebaseUser?) {
        val notificationSwitch = binding.swOnOff
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Load saved switch state
        val isNotificationEnabled = sharedPreferences.getBoolean(PREFS_NOTIFICATION_KEY, false)
        notificationSwitch.isChecked = isNotificationEnabled

        if (currentUser == null) {
            notificationSwitch.isChecked = false
            notificationSwitch.isEnabled = false
            Toast.makeText(
                requireContext(),
                "Login to enable notifications.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean(PREFS_NOTIFICATION_KEY, isChecked)
            editor.apply()

            if (isChecked) {
                checkWatchlistContent(currentUser.uid) { hasContent ->
                    if (hasContent) {
                        scheduleDailyNotification()
                        sendWatchlistNotification("Notifications enabled for daily watchlist updates!")
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No content in your watchlist.",
                            Toast.LENGTH_SHORT
                        ).show()
                        notificationSwitch.isChecked = false
                        cancelDailyNotification()
                    }
                }
            } else {
                cancelDailyNotification()
                Toast.makeText(requireContext(), "Notifications disabled.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkWatchlistContent(userId: String, callback: (Boolean) -> Unit) {
        firestore.collection("movieList")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { movieDocuments ->
                if (!movieDocuments.isEmpty) {
                    callback(true)
                } else {
                    firestore.collection("tvShowList")
                        .whereEqualTo("userId", userId)
                        .get()
                        .addOnSuccessListener { tvDocuments ->
                            callback(!tvDocuments.isEmpty)
                        }
                        .addOnFailureListener {
                            callback(false)
                        }
                }
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    private fun scheduleDailyNotification() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun cancelDailyNotification() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun sendWatchlistNotification(contentText: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
                return
            }
        }

        val channelId = "watchlist_notification_channel"
        val notificationId = 100

        // Create a NotificationChannel for Android O and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Watchlist Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for Watchlist Updates"
            }
            val notificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.baseline_movie_24) // Use an appropriate drawable resource
            .setContentTitle("Watchlist Notification")
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(notificationId, builder.build())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
