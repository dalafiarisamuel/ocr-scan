package ng.mint.ocrscanner.repositories

import kotlinx.coroutines.flow.Flow
import ng.mint.ocrscanner.dao.OfflineCardDao
import ng.mint.ocrscanner.model.OfflineCard

class FakeOfflineCardsRepository(private val dao: OfflineCardDao) : OfflineCardRepository {

    override fun getDataListLive(): Flow<MutableList<OfflineCard>> = dao.getDataListLive()

    override suspend fun getDataList(): MutableList<OfflineCard> = dao.getDataList()

    override suspend fun insertSingle(data: OfflineCard) {
        dao.insertSingle(data)
    }

    override suspend fun delete(data: OfflineCard) {
        dao.delete(data)
    }

    override suspend fun cleanTable() {
        dao.cleanTable()
    }

    override suspend fun getCount(): Long = dao.getCount()
}