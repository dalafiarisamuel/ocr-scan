package ng.mint.ocrscanner.viewmodel

import ng.mint.ocrscanner.database.Database
import ng.mint.ocrscanner.model.RecentCard


class CardsRepository(private val db: Database) {

    fun getRecentCardDataListLive() = db.recentCardDao().getDataListLive()

    suspend fun insertSingleRecentCard(recentCard: RecentCard) =
        db.recentCardDao().insertSingle(recentCard)

    suspend fun delete(recentCard: RecentCard) = db.recentCardDao().delete(recentCard)

    suspend fun cleanTable() = db.recentCardDao().cleanTable()

    suspend fun getCount(): Long = db.recentCardDao().getCount()
}