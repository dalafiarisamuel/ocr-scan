package ng.mint.ocrscanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ng.mint.ocrscanner.formatToViewDateTimeDefaults
import ng.mint.ocrscanner.model.CardResult
import ng.mint.ocrscanner.model.OfflineCard
import ng.mint.ocrscanner.model.RecentCard
import ng.mint.ocrscanner.model.RecentCardsState
import ng.mint.ocrscanner.networking.RequestHandler
import ng.mint.ocrscanner.repositories.OfflineCardRepository
import ng.mint.ocrscanner.repositories.RecentCardRepository
import ng.mint.ocrscanner.toRecentCard
import ng.mint.ocrscanner.util.AppCoroutineDispatchers
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val cardRepository: RecentCardRepository,
    private val offlineCardRepo: OfflineCardRepository,
    private val requestHandler: RequestHandler,
    private val appCoroutineDispatcher: AppCoroutineDispatchers
) : ViewModel() {

    private val cardResultStateFlow = MutableStateFlow<CardResult>(CardResult.EmptyState)
    val cardResult = cardResultStateFlow.asStateFlow()


    var dataFlow: MutableLiveData<RecentCardsState> = MutableLiveData(RecentCardsState.EmptyList)
        private set

    init {
        fetchRecentCardAndPostToLiveData()
    }

    fun updateData(cardResult: CardResult) {
        cardResultStateFlow.value = cardResult
    }

    fun processCardDetail(value: String) {

        viewModelScope.launch(appCoroutineDispatcher.io) {

            val request = requestHandler.getCardDetail(value)
            if (request.isSuccessful) {
                when (val cardResponse = request.body()) {
                    null -> updateData(CardResult.Failure)
                    else -> {
                        updateData(CardResult.Success(cardResponse))
                        insertSingleRecentCard(cardResponse.toRecentCard(value))
                    }
                }

            } else {
                updateData(CardResult.Failure)
            }


        }

    }

    fun insertOfflineCard(bin: String) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            offlineCardRepo.insertSingle(
                OfflineCard(
                    bin = bin,
                    dateCreated = Date().formatToViewDateTimeDefaults()
                )
            )
        }
    }

    private fun fetchRecentCardAndPostToLiveData() {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            cardRepository.getRecentCardDataListLive()
                .catch { emit(ArrayList()) }
                .collectLatest {
                    if (it.isEmpty()) {
                        dataFlow.postValue(RecentCardsState.EmptyList)
                    } else {
                        dataFlow.postValue(RecentCardsState.RecentCardList(it))
                    }
                }
        }
    }


    fun insertSingleRecentCard(recentCard: RecentCard) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            cardRepository.insertSingleRecentCard(
                recentCard
            )
        }
    }


    fun deleteRecentCard(recentCard: RecentCard) {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            cardRepository.deleteRecentCard(
                recentCard
            )
        }
    }

    suspend fun recentCardsCount(): Long = cardRepository.getRecentCardsCount()

    fun getRecentCardDataListLiveData() = cardRepository.getRecentCardDataListLiveData()

    fun getRecentCardsListFlow() = cardRepository.getRecentCardDataListLive()

    fun cleanRecentCardsTable() {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            cardRepository.cleanRecentCardTable()
        }
    }

}

