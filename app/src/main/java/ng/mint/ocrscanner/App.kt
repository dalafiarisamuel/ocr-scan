package ng.mint.ocrscanner

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import ng.mint.ocrscanner.worker.OfflineCardWorker
import ng.mint.ocrscanner.worker.getJobTimeRequestConstraints
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    override fun onCreate() {
        super.onCreate()

        val offlinePeriodicWorker =
            PeriodicWorkRequestBuilder<OfflineCardWorker>(
                repeatInterval = 30, repeatIntervalTimeUnit = TimeUnit.MINUTES,
                flexTimeInterval = 5, flexTimeIntervalUnit = TimeUnit.MINUTES
            ).setConstraints(getJobTimeRequestConstraints()).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "offlinePeriodicWork",
            ExistingPeriodicWorkPolicy.KEEP, offlinePeriodicWorker
        )
    }
}