package ng.mint.ocrscanner.viewmodel

import ng.mint.ocrscanner.database.Database
import ng.mint.ocrscanner.model.RecentCard


class CardsRepository(private val db: Database) {

    private val recentDao get() = db.recentCardDao()

    fun getRecentCardDataListLive() = recentDao.getDataListLive()

    suspend fun insertSingleRecentCard(recentCard: RecentCard) = recentDao.insertSingle(recentCard)

    suspend fun delete(recentCard: RecentCard) = recentDao.delete(recentCard)

    suspend fun cleanTable() = recentDao.cleanTable()

    suspend fun getCount(): Long = recentDao.getCount()
}