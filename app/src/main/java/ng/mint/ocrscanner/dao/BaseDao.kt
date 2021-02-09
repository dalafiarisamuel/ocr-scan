package ng.mint.ocrscanner.dao

import androidx.lifecycle.LiveData

interface BaseDao<T> {
    fun getDataListLive(): LiveData<MutableList<T>>
    suspend fun insertSingle(data: T)
    suspend fun delete(data: T)
    suspend fun cleanTable()
    suspend fun getCount(): Long
}