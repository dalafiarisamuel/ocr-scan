package ng.mint.ocrscanner.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ng.mint.ocrscanner.model.OfflineCard

class FakeOfflineCardsRepository : OfflineCardRepository {

    private val allOfflineCard = mutableListOf<OfflineCard>()
    private val flowAllOfflineCards = MutableStateFlow(allOfflineCard)

    private suspend fun refreshFlow() = flowAllOfflineCards.emit(allOfflineCard)

    override fun getDataListLive(): Flow<MutableList<OfflineCard>> = flowAllOfflineCards

    override suspend fun getDataList(): MutableList<OfflineCard> = allOfflineCard

    override suspend fun insertSingle(data: OfflineCard) {
        allOfflineCard.add(data)
        refreshFlow()
    }

    override suspend fun delete(data: OfflineCard) {
        allOfflineCard.remove(data)
        refreshFlow()
    }

    override suspend fun cleanTable() {
        allOfflineCard.clear()
        refreshFlow()
    }

    override suspend fun getCount(): Long = allOfflineCard.size.toLong()
}