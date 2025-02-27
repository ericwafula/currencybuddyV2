package tech.ericwathome.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.core.content.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import tech.ericwathome.core.domain.ConnectionObserver

class DefaultConnectionObserver(
    private val context: Context,
) : ConnectionObserver {
    override fun observe(): Flow<ConnectionObserver.ConnectionState> {
        val connectivityManager = context.getSystemService<ConnectivityManager>()

        return callbackFlow {
            val networkCallback =
                object : ConnectivityManager.NetworkCallback() {
                    override fun onUnavailable() {
                        super.onUnavailable()
                        trySend(ConnectionObserver.ConnectionState.DISCONNECTED)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        trySend(ConnectionObserver.ConnectionState.DISCONNECTED)
                    }

                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        trySend(ConnectionObserver.ConnectionState.CONNECTED)
                    }
                }

            connectivityManager?.registerDefaultNetworkCallback(networkCallback)

            awaitClose {
                connectivityManager?.unregisterNetworkCallback(networkCallback)
            }
        }
    }
}