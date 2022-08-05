package bagus2x.myhabit.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.core.content.getSystemService
import bagus2x.myhabit.domain.repository.ConnectivityRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DefaultConnectivityRepository(
    private val context: Context
) : ConnectivityRepository {

    override fun observe(): Flow<Boolean> = callbackFlow {
        val networkCallback: ConnectivityManager.NetworkCallback =
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(true)
                }

                override fun onLost(network: Network) {
                    trySend(false)
                }
            }

        val connManager = context.getSystemService<ConnectivityManager>()
        requireNotNull(connManager)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            connManager.registerNetworkCallback(request, networkCallback)
        }
        awaitClose {
            connManager.unregisterNetworkCallback(networkCallback)
        }
    }
}
