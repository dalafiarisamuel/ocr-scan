package ng.mint.ocrscanner.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ng.mint.ocrscanner.model.RecentCard

@Dao
interface RecentCardDao : BaseDao<RecentCard> {

    @Query("SELECT * FROM RecentCard ORDER BY `_id` DESC")
    override fun getDataListLive(): LiveData<MutableList<RecentCard>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertSingle(data: RecentCard)

    @Delete
    override suspend fun delete(data: RecentCard)

    @Query("DELETE FROM RecentCard")
    override suspend fun cleanTable()

    @Query("SELECT COUNT(*) FROM RecentCard")
    override suspend fun getCount(): Long
}