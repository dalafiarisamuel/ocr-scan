package ng.mint.ocrscanner.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ng.mint.ocrscanner.model.RecentCard

@Dao
interface RecentCardDao : BaseDao<RecentCard> {

    @Query("SELECT * FROM RecentCard ORDER BY `_id` DESC")
    override fun getDataListLive(): Flow<MutableList<RecentCard>>

    @Query("SELECT * FROM RecentCard ORDER BY `_id` DESC")
    fun getDataListLiveData(): LiveData<MutableList<RecentCard>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertSingle(data: RecentCard)

    @Delete
    override suspend fun delete(data: RecentCard)

    @Query("DELETE FROM RecentCard")
    override suspend fun cleanTable()

    @Query("SELECT COUNT(*) FROM RecentCard")
    override suspend fun getCount(): Long
}