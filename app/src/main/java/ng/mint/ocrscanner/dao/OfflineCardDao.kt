package ng.mint.ocrscanner.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ng.mint.ocrscanner.model.OfflineCard
import ng.mint.ocrscanner.model.RecentCard

@Dao
interface OfflineCardDao : BaseDao<OfflineCard> {

    @Query("SELECT * FROM OfflineCard ORDER BY `_id` DESC")
    override fun getDataListLive(): Flow<MutableList<OfflineCard>>

    @Query("SELECT * FROM OfflineCard ORDER BY `_id` DESC")
    suspend fun getDataList(): MutableList<OfflineCard>

    @Query("SELECT * FROM OfflineCard ORDER BY `_id` DESC")
    fun getDataListLiveData(): LiveData<MutableList<OfflineCard>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertSingle(data: OfflineCard)

    @Delete
    override suspend fun delete(data: OfflineCard)

    @Query("DELETE FROM OfflineCard")
    override suspend fun cleanTable()

    @Query("SELECT COUNT(*) FROM OfflineCard")
    override suspend fun getCount(): Long
}