package tech.ericwathome.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import tech.ericwathome.core.domain.ConnectionObserver

class DefaultConnectionObserver(
    private val context: Context,
) : ConnectionObserver {
    override fun isAvailable(): Flow<Boolean> {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        val networkRequest =
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()

        return callbackFlow {
            val networkCallback =
                object : ConnectivityManager.NetworkCallback() {
                    override fun onUnavailable() {
                        super.onUnavailable()
                        trySend(false)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        trySend(false)
                    }

                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        trySend(true)
                    }
                }

            connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)

            awaitClose {
                connectivityManager?.unregisterNetworkCallback(networkCallback)
            }
        }
    }
}