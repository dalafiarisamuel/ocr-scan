package ng.mint.ocrscanner.views.fragments


import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.launchFragmentInHiltContainer
import ng.mint.ocrscanner.model.RecentCard
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class RecentCardDetailFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun onBackArrowButtonClicked_popBackStack() {

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

        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<RecentCardDetailFragment>(fragmentArgs = Bundle().apply {
            this.putParcelable("recentCard", recentCard)
        }) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.backArrow)).perform(ViewActions.click())

        verify(navController).popBackStack()
    }

    @Test
    fun onNavigateToRecentCardDetailFragment_validateTextDisplayCorrectly() {

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

        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<RecentCardDetailFragment>(fragmentArgs = Bundle().apply {
            this.putParcelable("recentCard", recentCard)
        }) {
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.card_scheme)).check(matches(withText(recentCard.scheme)))
        onView(withId(R.id.card_type)).check(matches(withText(recentCard.type)))
        onView(withId(R.id.phone)).check(matches(withText(recentCard.phone)))
        onView(withId(R.id.card_bank)).check(matches(withText(recentCard.bank)))
        onView(withId(R.id.card_currency)).check(matches(withText(recentCard.currency)))
        onView(withId(R.id.card_country)).check(matches(withText("${recentCard.emoji} ${recentCard.country}")))


    }

}