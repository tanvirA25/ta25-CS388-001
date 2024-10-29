package com.codepath.articlesearch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

// gets realtime network updates and changes
class NetworkConnection(private val context: Context, private  val onNetworkChanges: (Boolean)-> Unit) {
        private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            // monitors the network
            fun startMonitoring() {
                val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                context.registerReceiver(networkReceiver, intentFilter)
                onNetworkChanges(isOnline())
            }
            // stops monitoring
            fun stopMonitoring() {
                context.unregisterReceiver(networkReceiver)
            }
            // check if network is online
            private fun isOnline(): Boolean {
                val active = connectivityManager.activeNetwork ?: return false
                val networkCapabilities =
                    connectivityManager.getNetworkCapabilities(active) ?: return false
                return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
    
            private val networkReceiver = object  : BroadcastReceiver(){
                override fun onReceive(context: Context?, intent: Intent?) {
                    onNetworkChanges(isOnline())


        }
    }
}