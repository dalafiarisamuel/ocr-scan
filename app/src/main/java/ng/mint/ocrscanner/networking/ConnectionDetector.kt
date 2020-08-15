package ng.mint.ocrscanner.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionDetector(private val _context: Context) {

    /**
     * Checking for all possible internet providers
     */
    fun isConnectingToInternet(): Boolean {

        val connectivity =
            _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val info = connectivity.allNetworkInfo
        for (i in info.indices) if (info[i]
                .state == NetworkInfo.State.CONNECTED
        ) {
            return true
        }
        return false

    }

}