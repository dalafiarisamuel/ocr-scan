package ng.mint.ocrscanner.repositories

import androidx.lifecycle.LiveData
import ng.mint.ocrscanner.dao.RecentCardDao
import ng.mint.ocrscanner.model.RecentCard
import javax.inject.Inject


class DefaultRecentCardsRepository @Inject constructor(private val dao: RecentCardDao) :
    RecentCardRepository {

    override fun getRecentCardDataListLive() = dao.getDataListLive()

    override fun getRecentCardDataListLiveData(): LiveData<MutableList<RecentCard>> =
        dao.getDataListLiveData()

    override suspend fun insertSingleRecentCard(recentCard: RecentCard) =
        dao.insertSingle(recentCard)

    override suspend fun deleteRecentCard(recentCard: RecentCard) = dao.delete(recentCard)

    override suspend fun cleanRecentCardTable() = dao.cleanTable()

    override suspend fun getRecentCardsCount(): Long = dao.getCount()
}