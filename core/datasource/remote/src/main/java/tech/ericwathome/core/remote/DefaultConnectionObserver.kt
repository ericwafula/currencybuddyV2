package tech.ericwathome.core.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import tech.ericwathome.core.domain.ConnectionObserver
import tech.ericwathome.core.domain.util.DispatcherProvider
import java.net.HttpURLConnection
import java.net.URL

class DefaultConnectionObserver(
    context: Context,
    private val scope: CoroutineScope,
    private val dispatchers: DispatcherProvider,
) : ConnectionObserver {
    private val connectivityManager = context.getSystemService<ConnectivityManager>()
    private val networkRequest: NetworkRequest =
        NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

    private var job: Job? = null

    private val _networkStatus: MutableSharedFlow<Boolean> = MutableSharedFlow()
    override val networkStatus: SharedFlow<Boolean> = _networkStatus.asSharedFlow()

    init {
        observeNetworkStatus()
    }

    private fun observeNetworkStatus() {
        scope.launch {
            callbackFlow {
                emitCurrentNetworkStatus()

                val networkCallback =
                    object : ConnectivityManager.NetworkCallback() {
                        override fun onUnavailable() = emitCurrentNetworkStatus()

                        override fun onLost(network: Network) = emitCurrentNetworkStatus()

                        override fun onAvailable(network: Network) = emitCurrentNetworkStatus()
                    }

                connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)

                awaitClose {
                    connectivityManager?.unregisterNetworkCallback(networkCallback)
                    job?.cancel()
                }
            }.distinctUntilChanged().collect { isConnected ->
                _networkStatus.emit(isConnected)
            }
        }
    }

    private fun ProducerScope<Boolean>.emitCurrentNetworkStatus() {
        job?.cancel()
        job =
            scope.launch {
                val isNetworkAvailable = if (isConnected()) hasInternetAccess() else false
                trySend(isNetworkAvailable)
            }
    }

    private fun isConnected(): Boolean {
        val activeNetwork = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    private suspend fun hasInternetAccess(): Boolean {
        return withContext(dispatchers.io) {
            try {
                val url = URL("https://clients3.google.com/generate_204")
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 1500
                connection.connect()
                val responseCode = connection.responseCode
                connection.disconnect()

                responseCode == 204
            } catch (e: IOException) {
                false
            }
        }
    }
}