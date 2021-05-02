package ng.mint.ocrscanner.repositories

import kotlinx.coroutines.flow.Flow
import ng.mint.ocrscanner.model.OfflineCard

interface OfflineCardRepository {

    fun getDataListLive(): Flow<MutableList<OfflineCard>>

    suspend fun getDataList(): MutableList<OfflineCard>

    suspend fun insertSingle(data: OfflineCard)

    suspend fun delete(data: OfflineCard)

    suspend fun cleanTable()

    suspend fun getCount(): Long
}