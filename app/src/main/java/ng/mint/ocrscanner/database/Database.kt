package ng.mint.ocrscanner.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ng.mint.ocrscanner.dao.RecentCardDao
import ng.mint.ocrscanner.model.RecentCard


@androidx.room.Database(
    entities = [RecentCard::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: Database? = null

        fun getInstance(context: Context): Database {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "SavedCardsDB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun recentCardDao(): RecentCardDao
}