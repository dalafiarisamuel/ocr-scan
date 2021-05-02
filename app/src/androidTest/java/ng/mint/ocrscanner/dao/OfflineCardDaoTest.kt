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
import ng.mint.ocrscanner.model.OfflineCard
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class OfflineCardDaoTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_database")
    lateinit var database: Database
    lateinit var offlineCardDao: OfflineCardDao


    @Before
    fun setUp() {

        hiltRule.inject()
        offlineCardDao = database.offlineCardDao()

    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertOfflineCard() = runBlockingTest {

        val offlineCard = OfflineCard(
            id = 1,
            bin = "52452",
            dateCreated = "10-12-2020"
        )

        //insert offline card
        offlineCardDao.insertSingle(offlineCard)

        val allOfflineCard = offlineCardDao.getDataListLiveData().getOrAwaitValue()

        assertThat(allOfflineCard).contains(offlineCard)
    }

    @Test
    fun deleteOfflineCard() = runBlockingTest {

        val offlineCard = OfflineCard(
            id = 1,
            bin = "52452",
            dateCreated = "10-12-2020"
        )

        //insert offline card
        offlineCardDao.insertSingle(offlineCard)

        // delete offline card
        offlineCardDao.delete(offlineCard)

        val allOfflineCard = offlineCardDao.getDataListLiveData().getOrAwaitValue()

        assertThat(allOfflineCard).doesNotContain(offlineCard)
    }

    @Test
    fun cleanOfflineCardTable() = runBlockingTest {

        val offlineCard = OfflineCard(
            id = 1,
            bin = "52452",
            dateCreated = "10-12-2020"
        )

        //insert offline card
        offlineCardDao.insertSingle(offlineCard)

        // clean offline card
        offlineCardDao.cleanTable()

        val count = offlineCardDao.getCount()
        assertThat(count).isEqualTo(0L)
    }

    @Test
    fun offlineCardCount() = runBlockingTest {

        val offlineCard = OfflineCard(
            id = 1,
            bin = "52452",
            dateCreated = "10-12-2020"
        )

        //insert offline card
        offlineCardDao.insertSingle(offlineCard)

        val count = offlineCardDao.getCount()

        assertThat(count).isEqualTo(1L)
    }

    @Test
    fun noDuplicateBinNumberEntry() = runBlockingTest {

        val offlineCard = OfflineCard(
            id = 1,
            bin = "52452",
            dateCreated = "10-12-2020"
        )

        val offlineCard2 = OfflineCard(
            id = 2,
            bin = "52452",
            dateCreated = "12-12-2020"
        )

        offlineCardDao.insertSingle(offlineCard)
        offlineCardDao.insertSingle(offlineCard2)

        //get item count
        val count = offlineCardDao.getCount()

        // get all shopping items
        val allOfflineCards = offlineCardDao.getDataListLiveData().getOrAwaitValue()

        // assert that no duplicate entry exists for a single bin number
        assertThat(count).isEqualTo(1L)
        assertThat(allOfflineCards).containsExactly(offlineCard)

    }
}






