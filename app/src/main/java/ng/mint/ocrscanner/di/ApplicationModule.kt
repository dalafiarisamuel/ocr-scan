package ng.mint.ocrscanner.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ng.mint.ocrscanner.dao.OfflineCardDao
import ng.mint.ocrscanner.dao.RecentCardDao
import ng.mint.ocrscanner.database.Database
import ng.mint.ocrscanner.networking.ApiInterface
import ng.mint.ocrscanner.networking.ConnectionDetector
import ng.mint.ocrscanner.networking.RequestHandler
import ng.mint.ocrscanner.networking.RetrofitCompat
import ng.mint.ocrscanner.repositories.DefaultOfflineCardRepository
import ng.mint.ocrscanner.repositories.DefaultRecentCardsRepository
import ng.mint.ocrscanner.repositories.OfflineCardRepository
import ng.mint.ocrscanner.repositories.RecentCardRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApplicationModule {

    @[Provides Singleton]
    fun providesRetrofit(): Retrofit = RetrofitCompat.getInstance()

    @[Provides Singleton]
    fun providesApiInterface(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @[Provides Singleton]
    fun providesRequestHandler(client: ApiInterface) = RequestHandler(client)

    @[Provides Singleton]
    fun provideDatabase(@ApplicationContext context: Context): Database =
        Database.getInstance(context)

    @[Provides Singleton]
    fun provideRecentCardDao(db: Database): RecentCardDao = db.recentCardDao()

    @[Provides Singleton]
    fun providesConnectionDetector(@ApplicationContext context: Context) =
        ConnectionDetector(context)

    @[Provides Singleton]
    fun providesOfflineCardDao(db: Database): OfflineCardDao = db.offlineCardDao()

    @[Provides Singleton]
    fun providesDefaultRecentCardRepository(dao: RecentCardDao) =
        DefaultRecentCardsRepository(dao) as RecentCardRepository

    @[Provides Singleton]
    fun providesDefaultOfflineCardRepository(dao: OfflineCardDao) =
        DefaultOfflineCardRepository(dao) as OfflineCardRepository

}