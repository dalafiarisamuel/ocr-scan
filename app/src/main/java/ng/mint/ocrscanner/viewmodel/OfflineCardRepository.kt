package ng.mint.ocrscanner.viewmodel

import kotlinx.coroutines.flow.Flow
import ng.mint.ocrscanner.dao.OfflineCardDao
import ng.mint.ocrscanner.model.OfflineCard
import javax.inject.Inject

class OfflineCardRepository @Inject constructor(private val dao: OfflineCardDao) {

    fun getDataListLive(): Flow<MutableList<OfflineCard>> = dao.getDataListLive()

    suspend fun getDataList(): MutableList<OfflineCard> = dao.getDataList()

    suspend fun insertSingle(data: OfflineCard) = dao.insertSingle(data)

    suspend fun delete(data: OfflineCard) = dao.delete(data)

    suspend fun cleanTable() = dao.cleanTable()

    suspend fun getCount(): Long = dao.getCount()
}