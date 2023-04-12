package com.embrace.quizapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object Utils {

    const val FINAL_SCORE: String = ""
    const val HIGH_SCORE_FILED = "highestScore"

    /***
     *To Check Internet connectivity
     */
    fun isConnectionOn(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val connection = connectivityManager.getNetworkCapabilities(network)
        return connection != null && (connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || connection.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR
        ))
    }

}