package ng.mint.ocrscanner.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ng.mint.ocrscanner.database.Database
import ng.mint.ocrscanner.networking.ApiInterface
import ng.mint.ocrscanner.networking.RequestHandler
import ng.mint.ocrscanner.networking.RetrofitCompat
import ng.mint.ocrscanner.repositories.FakeOfflineCardsRepository
import ng.mint.ocrscanner.repositories.FakeRecentCardsRepository
import ng.mint.ocrscanner.repositories.OfflineCardRepository
import ng.mint.ocrscanner.repositories.RecentCardRepository
import ng.mint.ocrscanner.viewmodel.CardsViewModel
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestApplicationModule {

    @Provides
    @Named("test_database")
    fun providesInMemoryDatabase(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, Database::class.java).allowMainThreadQueries().build()

    @Provides
    @Named("requestHandler")
    fun providesRequestHandler() =
        RequestHandler(RetrofitCompat.getInstance().create(ApiInterface::class.java))

    @Provides
    @Named("recentCardRepository")
    fun providesRecentCardRepository() = FakeRecentCardsRepository() as RecentCardRepository

    @Provides
    @Named("offlineCardRepository")
    fun providesOfflineCardRepository() = FakeOfflineCardsRepository() as OfflineCardRepository

    @Provides
    @Named("cardsViewModel")
    fun providesCardsVieModel(
        @Named("recentCardRepository") cardRepository: RecentCardRepository,
        @Named("offlineCardRepository") offlineCardRepo: OfflineCardRepository,
        @Named("requestHandler") requestHandler: RequestHandler
    ) = CardsViewModel(cardRepository, offlineCardRepo, requestHandler)
}