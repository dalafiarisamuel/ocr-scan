package ng.mint.ocrscanner.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ng.mint.ocrscanner.model.OfflineCard

@Dao
interface OfflineCardDao : BaseDao<OfflineCard> {

    @Query("SELECT * FROM OfflineCard ORDER BY `_id` DESC")
    override fun getDataListLive(): Flow<MutableList<OfflineCard>>

    @Query("SELECT * FROM OfflineCard ORDER BY `_id` DESC")
    suspend fun getDataList(): MutableList<OfflineCard>

    @Insert
    override suspend fun insertSingle(data: OfflineCard)

    @Delete
    override suspend fun delete(data: OfflineCard)

    @Query("DELETE FROM OfflineCard")
    override suspend fun cleanTable()

    @Query("SELECT COUNT(*) FROM OfflineCard")
    override suspend fun getCount(): Long
}