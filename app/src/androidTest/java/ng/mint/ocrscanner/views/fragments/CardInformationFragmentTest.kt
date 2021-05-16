package ng.mint.ocrscanner.views.fragments

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.launchFragmentInHiltContainer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class CardInformationFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickRecentCardsButton_navigateToRecentCardsFragment(){

        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<CardInformationFragment> {

            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.recent_cards)).perform(ViewActions.click())

        verify(navController).navigate(R.id.action_cardInformationFragment_to_recentlyViewedCardsFragment)

    }
}