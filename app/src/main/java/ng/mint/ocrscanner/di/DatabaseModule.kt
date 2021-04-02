package ng.mint.ocrscanner.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ng.mint.ocrscanner.dao.RecentCardDao
import ng.mint.ocrscanner.database.Database
import ng.mint.ocrscanner.networking.ConnectionDetector
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @[Provides Singleton]
    fun provideDatabase(@ApplicationContext context: Context): Database =
        Database.getInstance(context)

    @[Provides Singleton]
    fun provideRecentCardDao(db: Database): RecentCardDao = db.recentCardDao()

    @[Provides Singleton]
    fun providesConnectionDetector(@ApplicationContext context: Context) =
        ConnectionDetector(context)

}