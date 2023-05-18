package com.bestDate.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

object NetworkStateListener {
    var currentStatus: NetworkStatus = NetworkStatus.AVAILABLE

    var statusChanged: ((NetworkStatus) -> Unit)? = null
    var cameOnline: (() -> Unit)? = null

    fun init(context: Context) {
        val connectionManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectionManager.requestNetwork(getNetworkRequest(), getNetworkCallback())
        currentStatus =
            if (connectionManager.getNetworkCapabilities(connectionManager.activeNetwork) != null)
                NetworkStatus.AVAILABLE else NetworkStatus.LOST
    }

    private fun getNetworkRequest(): NetworkRequest {
        return NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
    }

    private fun getNetworkCallback(): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if (currentStatus != NetworkStatus.AVAILABLE) {
                    currentStatus = NetworkStatus.AVAILABLE
                    statusChanged?.invoke(NetworkStatus.AVAILABLE)
                    cameOnline?.invoke()
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                if (currentStatus != NetworkStatus.LOST) {
                    statusChanged?.invoke(NetworkStatus.LOST)
                    currentStatus = NetworkStatus.LOST
                }
            }
        }
    }
}

enum class NetworkStatus {
    AVAILABLE, LOST
}