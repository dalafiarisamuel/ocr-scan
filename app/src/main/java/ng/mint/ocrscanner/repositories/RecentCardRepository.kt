package ng.mint.ocrscanner.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ng.mint.ocrscanner.model.RecentCard

interface RecentCardRepository {

    fun getRecentCardDataListLive(): Flow<MutableList<RecentCard>>

    fun getRecentCardDataListLiveData(): LiveData<MutableList<RecentCard>>

    suspend fun insertSingleRecentCard(recentCard: RecentCard)

    suspend fun delete(recentCard: RecentCard)

    suspend fun cleanTable()

    suspend fun getCount(): Long
}