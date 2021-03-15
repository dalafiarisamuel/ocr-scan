package ng.mint.ocrscanner.viewmodel

import ng.mint.ocrscanner.dao.RecentCardDao
import ng.mint.ocrscanner.model.RecentCard
import javax.inject.Inject


class CardsRepository @Inject constructor(private val dao: RecentCardDao) {

    fun getRecentCardDataListLive() = dao.getDataListLive()

    suspend fun insertSingleRecentCard(recentCard: RecentCard) =
        dao.insertSingle(recentCard)

    suspend fun delete(recentCard: RecentCard) = dao.delete(recentCard)

    suspend fun cleanTable() = dao.cleanTable()

    suspend fun getCount(): Long = dao.getCount()
}