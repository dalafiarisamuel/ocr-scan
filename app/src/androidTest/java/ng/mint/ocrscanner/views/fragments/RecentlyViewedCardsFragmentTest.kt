package ng.mint.ocrscanner.views.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.TestCoroutineRule
import ng.mint.ocrscanner.adapters.CustomBindAdapter.setData
import ng.mint.ocrscanner.adapters.RecentCardsAdapter
import ng.mint.ocrscanner.getOrAwaitValue
import ng.mint.ocrscanner.launchFragmentInHiltContainer
import ng.mint.ocrscanner.model.RecentCard
import ng.mint.ocrscanner.model.RecentCardsState
import ng.mint.ocrscanner.viewmodel.CardsViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class RecentlyViewedCardsFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("cardsViewModel")
    lateinit var cardsViewModel: CardsViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun clearList() = runBlockingTest {
        cardsViewModel.cleanRecentCardsTable()
    }


    @Test
    fun onBackArrowButtonClicked_popBackStack() {

        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<RecentlyViewedCardsFragment> {

            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.backArrow)).perform(ViewActions.click())

        verify(navController).popBackStack()
    }


    @Test
    fun onBackPressed_popBackStack() {

        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<RecentlyViewedCardsFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        pressBack()
        verify(navController).popBackStack()

    }

    @Test
    fun onRecentCardItemClick_navigateToRecentCardDetailFragment() {

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

        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<RecentlyViewedCardsFragment> {

            Navigation.setViewNavController(requireView(), navController)

            this.viewModel = cardsViewModel

            binding.savedRecyclerview.setData(
                RecentCardsState.RecentCardList(
                    mutableListOf(recentCard)
                ), dataHandler
            )
        }

        onView(withId(R.id.saved_recyclerview)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecentCardsAdapter.RecentCardHolder>(
                0, ViewActions.click()
            )
        )

        verify(navController).navigate(
            RecentlyViewedCardsFragmentDirections.actionRecentlyViewedCardsFragmentToRecentCardDetailFragment(
                recentCard
            )
        )

    }


    fun onRecentCardSwipeLeft_deleteItem() = runBlocking {

        val recentCard = RecentCard(
            bin = "524523",
            dateCreated = "10-12-2020",
            bank = "GT Bank",
            currency = "NGN",
            country = "Nigeria",
            phone = "08141289822",
            emoji = "🇳🇬",
            scheme = "Master",
            type = "Credit",
            id = 2
        )


        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<RecentlyViewedCardsFragment> {

            Navigation.setViewNavController(requireView(), navController)

            viewModel = cardsViewModel

            cardsViewModel.insertSingleRecentCard(recentCard)

            binding.savedRecyclerview.setData(
                RecentCardsState.RecentCardList(
                    mutableListOf(recentCard)
                ), dataHandler
            )

        }

        val list1 = cardsViewModel.getRecentCardDataListLiveData().getOrAwaitValue()
        assertThat(list1).contains(recentCard)

        onView(withId(R.id.saved_recyclerview)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecentCardsAdapter.RecentCardHolder>(
                0,
                ViewActions.swipeLeft()
            )
        )

        val list = cardsViewModel.getRecentCardDataListLiveData().getOrAwaitValue()
        assertThat(list).doesNotContain(recentCard)

    }
}