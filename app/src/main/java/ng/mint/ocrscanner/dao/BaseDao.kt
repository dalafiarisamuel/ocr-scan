package ng.mint.ocrscanner.dao

import kotlinx.coroutines.flow.Flow

interface BaseDao<T> {
    fun getDataListLive(): Flow<MutableList<T>>
    suspend fun insertSingle(data: T)
    suspend fun delete(data: T)
    suspend fun cleanTable()
    suspend fun getCount(): Long
}