package tech.ericwathome.core.remote

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
    context: Context,
) : ConnectionObserver {
    private val connectivityManager = context.getSystemService<ConnectivityManager>()
    private val networkRequest: NetworkRequest =
        NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

    override val hasNetworkConnection: Flow<Boolean>
        get() =
            callbackFlow {
                trySend(isNetworkAvailable())

                val networkCallback =
                    object : ConnectivityManager.NetworkCallback() {
                        override fun onUnavailable() {
                            super.onUnavailable()
                            trySend(isNetworkAvailable())
                        }

                        override fun onLost(network: Network) {
                            super.onLost(network)
                            trySend(isNetworkAvailable())
                        }

                        override fun onAvailable(network: Network) {
                            super.onAvailable(network)
                            trySend(isNetworkAvailable())
                        }
                    }

                connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)

                awaitClose {
                    connectivityManager?.unregisterNetworkCallback(networkCallback)
                }
            }

    private fun isNetworkAvailable(): Boolean {
        val activeNetwork = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}