package ng.mint.ocrscanner.worker

import androidx.work.Constraints
import androidx.work.NetworkType

fun getJobTimeRequestConstraints(): Constraints {
    return Constraints.Builder().setRequiresBatteryNotLow(true)
        .setRequiresStorageNotLow(true)
        .setRequiredNetworkType(NetworkType.CONNECTED).build()
}
