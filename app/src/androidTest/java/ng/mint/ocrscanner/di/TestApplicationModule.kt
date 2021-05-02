package ng.mint.ocrscanner.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ng.mint.ocrscanner.database.Database
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestApplicationModule {

    @Provides
    @Named("test_database")
    fun providesInMemoryDatabase(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, Database::class.java).allowMainThreadQueries().build()
}