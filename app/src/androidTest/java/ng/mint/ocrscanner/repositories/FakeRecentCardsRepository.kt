package ng.mint.ocrscanner.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ng.mint.ocrscanner.model.RecentCard

class FakeRecentCardsRepository : RecentCardRepository {

    private var allRecentCards = mutableListOf<RecentCard>()
    private val observableRecentCards = MutableLiveData(allRecentCards)
    private val flowAllRecentCards = MutableStateFlow(allRecentCards)

    private suspend fun refreshList() {
        observableRecentCards.postValue(allRecentCards)
        flowAllRecentCards.emit(allRecentCards)
    }

    override fun getRecentCardDataListLive(): Flow<MutableList<RecentCard>> = flowAllRecentCards

    override fun getRecentCardDataListLiveData(): LiveData<MutableList<RecentCard>> =
        observableRecentCards

    override suspend fun insertSingleRecentCard(recentCard: RecentCard) {
        val recentCards = allRecentCards.firstOrNull { it.bin == recentCard.bin }
        if (recentCards == null) {
            allRecentCards.add(recentCard)
            refreshList()
        }
    }

    override suspend fun deleteRecentCard(recentCard: RecentCard) {
        allRecentCards.remove(recentCard)
        refreshList()
    }

    override suspend fun cleanRecentCardTable() {
        allRecentCards.clear()
        refreshList()
    }

    override suspend fun getRecentCardsCount(): Long = allRecentCards.size.toLong()
}