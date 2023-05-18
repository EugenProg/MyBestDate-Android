package com.bestDate.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.NetworkRequest

class NetworkStateListener(context: Context) {
    var statusChanged: ((NetworkStatus) -> Unit)? = null

    init {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.requestNetwork(getNetworkRequest(), getNetworkCallback())
        currentStatus =
            if (connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) != null)
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
                currentStatus = NetworkStatus.AVAILABLE
                statusChanged?.invoke(NetworkStatus.AVAILABLE)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                currentStatus = NetworkStatus.LOST
                statusChanged?.invoke(NetworkStatus.LOST)
            }
        }
    }

    companion object {
        var currentStatus: NetworkStatus = NetworkStatus.AVAILABLE
    }
}

enum class NetworkStatus {
    AVAILABLE, LOST
}