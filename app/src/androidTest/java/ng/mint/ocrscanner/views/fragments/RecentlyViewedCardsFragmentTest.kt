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
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.adapters.CustomBindAdapter.setData
import ng.mint.ocrscanner.adapters.RecentCardsAdapter
import ng.mint.ocrscanner.launchFragmentInHiltContainer
import ng.mint.ocrscanner.model.RecentCard
import ng.mint.ocrscanner.model.RecentCardsState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class RecentlyViewedCardsFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
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

        verify(navController).navigate(RecentlyViewedCardsFragmentDirections.actionRecentlyViewedCardsFragmentToRecentCardDetailFragment(
            recentCard
        ))

    }
}