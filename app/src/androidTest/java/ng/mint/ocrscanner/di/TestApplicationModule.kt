package ng.mint.ocrscanner.di

import android.content.Context
import androidx.room.Room
import co.infinum.retromock.Behavior
import co.infinum.retromock.Retromock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ng.mint.ocrscanner.dao.OfflineCardDao
import ng.mint.ocrscanner.dao.RecentCardDao
import ng.mint.ocrscanner.database.Database
import ng.mint.ocrscanner.networking.ApiInterface
import ng.mint.ocrscanner.networking.ApiInterfaceTest
import ng.mint.ocrscanner.networking.RequestHandler
import ng.mint.ocrscanner.networking.RetrofitCompatTest
import ng.mint.ocrscanner.repositories.FakeOfflineCardsRepository
import ng.mint.ocrscanner.repositories.FakeRecentCardsRepository
import ng.mint.ocrscanner.repositories.OfflineCardRepository
import ng.mint.ocrscanner.repositories.RecentCardRepository
import ng.mint.ocrscanner.util.AppCoroutineDispatchers
import ng.mint.ocrscanner.util.FakeAppCoroutineDispatcher
import ng.mint.ocrscanner.viewmodel.CardsViewModel
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestApplicationModule {

    @[Provides Named("test_database")]
    fun providesInMemoryDatabase(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, Database::class.java).allowMainThreadQueries().build()

    @[Provides Singleton Named("test_recent_card_dao")]
    fun provideRecentCardDao(@Named("test_database") db: Database): RecentCardDao =
        db.recentCardDao()

    @[Provides Named("api_interface_test")]
    fun providesApiInterface(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @[Provides Named("retrofit_test")]
    fun providesRetrofit(): Retrofit = RetrofitCompatTest().createInstance()

    @Provides
    @Named("recentCardRepository")
    fun providesRecentCardRepository(@Named("test_recent_card_dao") dao: RecentCardDao) =
        FakeRecentCardsRepository(dao) as RecentCardRepository

    @[Provides Named("retro_mock_test")]
    fun providesRetroMock(@Named("retrofit_test") retrofit: Retrofit): Retromock =
        Retromock.Builder().retrofit(retrofit).defaultBehavior(Behavior { 0 }).build()

    @[Provides Named("api_client")]
    fun providesApiClient(@Named("retro_mock_test") retromock: Retromock): ApiInterfaceTest =
        retromock.create(ApiInterfaceTest::class.java)

    @[Provides Singleton Named("test_offline_card_dao")]
    fun providesOfflineCardDao(@Named("test_database") db: Database): OfflineCardDao =
        db.offlineCardDao()

    @[Provides Named("offlineCardRepository")]
    fun providesOfflineCardRepository(@Named("test_offline_card_dao") dao: OfflineCardDao) =
        FakeOfflineCardsRepository(dao) as OfflineCardRepository

    @[Provides Singleton Named("fake_app_coroutine_dispatcher")]
    fun providesFakeAppCoroutineDispatcher() =
        FakeAppCoroutineDispatcher() as AppCoroutineDispatchers


    @[Provides Named("cardsViewModel")]
    fun providesCardsViewModel(
        @Named("recentCardRepository") cardRepository: RecentCardRepository,
        @Named("offlineCardRepository") offlineCardRepo: OfflineCardRepository,
        requestHandler: RequestHandler,
        @Named("fake_app_coroutine_dispatcher") fakeAppCoroutineDispatcher: AppCoroutineDispatchers
    ): CardsViewModel =
        CardsViewModel(cardRepository, offlineCardRepo, requestHandler, fakeAppCoroutineDispatcher)


}