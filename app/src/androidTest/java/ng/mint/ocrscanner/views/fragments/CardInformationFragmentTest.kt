package ng.mint.ocrscanner.views.fragments

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import ng.mint.ocrscanner.R
import ng.mint.ocrscanner.launchFragmentInHiltContainer
import ng.mint.ocrscanner.model.CardResponse
import ng.mint.ocrscanner.model.CardResult
import ng.mint.ocrscanner.viewmodel.CardsViewModel
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject
import javax.inject.Named


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class CardInformationFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    @Named("cardsViewModel")
    lateinit var cardsViewModel: CardsViewModel

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun clearList() = runBlockingTest {
        cardsViewModel.cleanRecentCardsTable()
    }


    @Test
    fun clickRecentCardsButton_navigateToRecentCardsFragment() {

        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<CardInformationFragment> {

            Navigation.setViewNavController(requireView(), navController)

            this.viewModel = cardsViewModel
        }

        onView(withId(R.id.recent_cards)).perform(ViewActions.click())

        verify(navController).navigate(R.id.action_cardInformationFragment_to_recentlyViewedCardsFragment)

    }

    @Test
    fun onNavigateToRecentRecentFragment_cardInformationShouldBeHidden() {

        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<CardInformationFragment> {

            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.card_information_layout)).check(matches(not(isDisplayed())))


    }

    @Test
    fun whenUiReceivesCardData_cardInformationLayoutShouldBeVisible() {

        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<CardInformationFragment> {
            Navigation.setViewNavController(requireView(), navController)
            binding.bankData = CardResponse()
        }

        onView(withId(R.id.card_information_layout)).check(matches(isDisplayed()))

    }


    fun onUiStateChangedToSuccess_cardInformationShouldVisible() {

        cardsViewModel.updateData(CardResult.Success(CardResponse()))

        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<CardInformationFragment> {
            Navigation.setViewNavController(requireView(), navController)
            this.viewModel = cardsViewModel

        }

        onView(withId(R.id.card_information_layout)).check(matches(isDisplayed()))


    }
}