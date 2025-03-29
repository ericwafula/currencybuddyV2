package tech.ericwathome.core.remote

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import tech.ericwathome.core.domain.LocationObserver
import java.util.Currency
import java.util.Locale

class DefaultLocationObserver(
    private val context: Context,
    private val interval: Long
) : LocationObserver {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    override val getLocationCode: Flow<String>
        get() = callbackFlow {
            val locationManager = context.getSystemService<LocationManager>()!!
            var isGpsEnabled = false
            var isNetworkEnabled = false

            while (!isGpsEnabled && !isNetworkEnabled) {
                isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                if (!isGpsEnabled && !isNetworkEnabled) {
                    delay(1000)
                }
            }

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                close()
            } else {
                client.lastLocation.addOnSuccessListener {
                    it?.let { location ->
                        trySend(getLocationCurrencyCode(location))
                    }
                }

                val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval)
                    .build()

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        super.onLocationResult(result)
                        result.locations.lastOrNull()?.let { location ->
                            trySend(getLocationCurrencyCode(location))
                        }
                    }
                }

                client.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
            }
        }

    private fun getLocationCurrencyCode(location: Location): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val countryCode = addresses[0].countryCode
                val currency = Currency.getInstance(countryCode)
                currency.currencyCode
            } else {
                "USD" // fallback
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "USD" // fallback on error
        }
    }
}