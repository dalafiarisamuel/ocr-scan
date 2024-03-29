package ng.mint.ocrscanner.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import ng.mint.ocrscanner.TestCoroutineRule
import ng.mint.ocrscanner.getOrAwaitValue
import ng.mint.ocrscanner.model.CardResponse
import ng.mint.ocrscanner.model.CardResult
import ng.mint.ocrscanner.model.RecentCard
import ng.mint.ocrscanner.networking.ApiInterfaceTest
import ng.mint.ocrscanner.toRecentCard
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Named


@HiltAndroidTest
@SmallTest
@ExperimentalCoroutinesApi
class CardsViewModelTest {


    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Inject
    @Named("cardsViewModel")
    lateinit var viewModel: CardsViewModel

    @Inject
    @Named("api_client")
    lateinit var apiInterfaceTest: ApiInterfaceTest


    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun clearList() = runBlockingTest {
        viewModel.cleanRecentCardsTable()
    }


    @Test
    fun verifyDefaultStateOfCardResultStateIsEmpty() = runBlocking {
        viewModel.cardResult.test {
            val defaultState = awaitItem()
            assertThat(defaultState).isEqualTo(CardResult.EmptyState)
        }

    }

    @Test
    fun verifyStateOfCardResultStateIsUpdated() = runBlocking {

        val response = CardResponse()

        viewModel.updateData(CardResult.Success(response))

        viewModel.cardResult.test {
            val state = awaitItem()
            assertThat(state).isEqualTo(CardResult.Success(response))
        }

    }

    @Test
    fun insertSingleRecentCard() = runBlockingTest {


        val recentCard = RecentCard(
            bin = "52452",
            dateCreated = "10-12-2020",
            bank = "GT Bank",
            currency = "NGN",
            country = "Nigeria",
            phone = "08141289822",
            emoji = "🇳🇬",
            scheme = "Master",
            type = "Credit",
            id = 1
        )

        //insert data
        viewModel.insertSingleRecentCard(recentCard)

        //observe the data just inserted
        val allRecentCards = viewModel.getRecentCardDataListLiveData().getOrAwaitValue()

        assertThat(allRecentCards).contains(recentCard)

    }

    @Test
    fun insertSingeRecentCardEmitFlow() = runBlockingTest {

        val recentCard = RecentCard(
            bin = "52452",
            dateCreated = "10-12-2020",
            bank = "GT Bank",
            currency = "NGN",
            country = "Nigeria",
            phone = "08141289822",
            emoji = "🇳🇬",
            scheme = "Master",
            type = "Credit",
            id = 1
        )

        //insert data
        viewModel.insertSingleRecentCard(recentCard)

        val list = viewModel.getRecentCardsListFlow().first()
        assertThat(list).contains(recentCard)

    }

    @Test
    fun deleteRecentCard() = runBlockingTest {

        val recentCard = RecentCard(
            bin = "52452",
            dateCreated = "10-12-2020",
            bank = "GT Bank",
            currency = "NGN",
            country = "Nigeria",
            phone = "08141289822",
            emoji = "🇳🇬",
            scheme = "Master",
            type = "Credit",
            id = 1
        )

        //insert data
        viewModel.insertSingleRecentCard(recentCard)

        // delete data
        viewModel.deleteRecentCard(recentCard)

        //observe the data just inserted
        val allRecentCards = viewModel.getRecentCardDataListLiveData().getOrAwaitValue()

        assertThat(allRecentCards).doesNotContain(recentCard)

    }

    @Test
    fun recentCardsCount() = runBlockingTest {

        val recentCard = RecentCard(
            bin = "52452",
            dateCreated = "10-12-2020",
            bank = "GT Bank",
            currency = "NGN",
            country = "Nigeria",
            phone = "08141289822",
            emoji = "🇳🇬",
            scheme = "Master",
            type = "Credit",
            id = 1
        )

        //insert data
        viewModel.insertSingleRecentCard(recentCard)

        val count = viewModel.recentCardsCount()

        assertThat(count).isEqualTo(1L)
    }

    @Test
    fun cleanRecentCardsTable() = runBlockingTest {

        val recentCard = RecentCard(
            bin = "52452",
            dateCreated = "10-12-2020",
            bank = "GT Bank",
            currency = "NGN",
            country = "Nigeria",
            phone = "08141289822",
            emoji = "🇳🇬",
            scheme = "Master",
            type = "Credit",
            id = 1
        )

        //insert the data
        viewModel.insertSingleRecentCard(recentCard)

        //delete the entire table entry
        viewModel.cleanRecentCardsTable()

        //get item count
        val count = viewModel.recentCardsCount()

        assertThat(count).isEqualTo(0L)

    }

    @Test
    fun noDuplicateBinNumberEntry() = runBlockingTest {

        val recentCard = RecentCard(
            bin = "52452",
            dateCreated = "10-12-2020",
            bank = "GT Bank",
            currency = "NGN",
            country = "Nigeria",
            phone = "08141289822",
            emoji = "🇳🇬",
            scheme = "Master",
            type = "Credit",
            id = 1
        )

        val recentCard2 = RecentCard(
            bin = "52452",
            dateCreated = "20-12-2020",
            bank = "Access Bank",
            currency = "NGN",
            country = "Nigeria",
            phone = "08141283422",
            emoji = "🇳🇬",
            scheme = "Verve",
            type = "Debit",
            id = 2
        )

        // insert recent card objects
        viewModel.insertSingleRecentCard(recentCard)
        viewModel.insertSingleRecentCard(recentCard2)

        //get item count
        val count = viewModel.recentCardsCount()

        // get all cards items
        val allRecentCards = viewModel.getRecentCardDataListLiveData().getOrAwaitValue()

        // assert that no duplicate entry exists for a single bin number
        assertThat(count).isEqualTo(1L)
        assertThat(allRecentCards).containsExactly(recentCard)
    }

    @Test
    fun getCardInformationFromNetwork_returnsSuccessResponse() = runBlocking {

        val data = apiInterfaceTest.getCardDetail("53998344")

        assertThat(data.isSuccessful).isTrue()
        assertThat(data.body()).isNotNull()
    }

    @Test
    fun getCardInformationFromNetwork_returnValidCardsData() = runBlocking {

        val recentCard = RecentCard(
            bin = "53998344",
            dateCreated = "10-12-2020",
            bank = "Jyske Bank",
            currency = "DKK",
            country = "Denmark",
            phone = "+4589893300",
            emoji = "🇩🇰",
            scheme = "visa",
            type = "debit"
        )

        val data = apiInterfaceTest.getCardDetail("53998344")
            .body()!!.toRecentCard("53998344").apply {
                dateCreated = "10-12-2020"
            }

        assertThat(data).isEqualTo(recentCard)
    }

    @Test
    fun getCardInformationFromNetwork_returnsNotFound() = runBlocking {

        val data = apiInterfaceTest.getCardDetailWhenCardNotFound("00000000")

        assertThat(data.isSuccessful).isFalse()
        assertThat(data.code()).isEqualTo(HttpURLConnection.HTTP_NOT_FOUND)
        assertThat(data.body()).isNull()
    }


}