package com.example.newnetworkstate

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData

/**
 * A LiveData class which wraps the network connection status
 * Requires Permission: ACCESS_NETWORK_STATE
 * See https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
 * See https://developer.android.com/reference/android/net/ConnectivityManager
 * See https://developer.android.com/reference/android/net/ConnectivityManager#CONNECTIVITY_ACTION
 * @param connectivityManager
 * @author Nav Singh
 */
class ConnectivityLiveData private constructor(private val connectivityManager: ConnectivityManager) :
    LiveData<Boolean>() {

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    constructor(application: Application) : this(
        application.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
    )

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network?) {
            postValue(true)
        }

        override fun onLost(network: Network?) {
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()

        //check availability
        val networkAvailability =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        networkAvailability?.let {
            if (
                networkAvailability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkAvailability.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            ) postValue(true) else postValue(false)
        } ?: run {
            postValue(false)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) connectivityManager.registerDefaultNetworkCallback(
            networkCallback
        )
        else {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}