package ng.mint.ocrscanner.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ng.mint.ocrscanner.model.RecentCard

interface RecentCardRepository {

    fun getRecentCardDataListLive(): Flow<MutableList<RecentCard>>

    fun getRecentCardDataListLiveData(): LiveData<MutableList<RecentCard>>

    suspend fun insertSingleRecentCard(recentCard: RecentCard)

    suspend fun deleteRecentCard(recentCard: RecentCard)

    suspend fun cleanRecentCardTable()

    suspend fun getRecentCardsCount(): Long
}