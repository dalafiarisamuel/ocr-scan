package ng.mint.ocrscanner.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ng.mint.ocrscanner.networking.RequestHandler
import ng.mint.ocrscanner.showBinNotification
import ng.mint.ocrscanner.toRecentCard
import ng.mint.ocrscanner.repositories.DefaultRecentCardsRepository
import ng.mint.ocrscanner.repositories.DefaultOfflineCardRepository
import javax.inject.Singleton

@HiltWorker
class OfflineCardWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
    @Singleton private val requestHandler: RequestHandler,
    @Singleton private val offlineCardRepo: DefaultOfflineCardRepository,
    @Singleton private val cardRepository: DefaultRecentCardsRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {

        if (isStopped) Result.success(
            Data.Builder().putString("status", "Job was cancelled").build()
        )

        val job = async(Dispatchers.IO) {

            val list = offlineCardRepo.getDataList()
            list.forEach {

                val request = requestHandler.getCardDetail(it.bin ?: "")
                if (request.isSuccessful) {
                    when (val cardResponse = request.body()) {
                        null -> offlineCardRepo.delete(it)
                        else -> {
                            cardRepository.insertSingleRecentCard(
                                cardResponse.toRecentCard(
                                    it.bin ?: ""
                                )
                            )
                            appContext.showBinNotification(it.bin ?: "")
                            offlineCardRepo.delete(it)
                        }
                    }

                } else when (request.code()) {
                    in 400..500 -> offlineCardRepo.delete(it)
                }
            }

        }


        job.await()
        Result.success()
    }
}