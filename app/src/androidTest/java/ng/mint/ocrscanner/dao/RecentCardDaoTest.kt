package ng.mint.ocrscanner.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import ng.mint.ocrscanner.database.Database
import ng.mint.ocrscanner.getOrAwaitValue
import ng.mint.ocrscanner.model.RecentCard
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
class RecentCardDaoTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_database")
    lateinit var database: Database
    lateinit var recentCardDao: RecentCardDao

    @Before
    fun setup() {
        hiltRule.inject()
        recentCardDao = database.recentCardDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertRecentCard() = runBlockingTest {

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
        recentCardDao.insertSingle(recentCard)

        //observe the data just inserted
        val allRecentCards = recentCardDao.getDataListLiveData().getOrAwaitValue()

        assertThat(allRecentCards).contains(recentCard)

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

        //insert the data
        recentCardDao.insertSingle(recentCard)

        //delete the data
        recentCardDao.delete(recentCard)

        // get all shopping items
        val allRecentCards = recentCardDao.getDataListLiveData().getOrAwaitValue()

        assertThat(allRecentCards).doesNotContain(recentCard)

    }

    @Test
    fun cleanRecentCardTable() = runBlockingTest {

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
        recentCardDao.insertSingle(recentCard)

        //delete the entire table entry
        recentCardDao.cleanTable()

        //get item count
        val count = recentCardDao.getCount()

        assertThat(count).isEqualTo(0L)

    }

    @Test
    fun recentCardsTableCount() = runBlockingTest {

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
        recentCardDao.insertSingle(recentCard)

        //get item count
        val count = recentCardDao.getCount()

        assertThat(count).isEqualTo(1L)

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
        recentCardDao.insertSingle(recentCard)
        recentCardDao.insertSingle(recentCard2)

        //get item count
        val count = recentCardDao.getCount()

        // get all shopping items
        val allRecentCards = recentCardDao.getDataListLiveData().getOrAwaitValue()

        // assert that no duplicate entry exists for a single bin number
        assertThat(count).isEqualTo(1L)
        assertThat(allRecentCards).containsExactly(recentCard)
    }
}