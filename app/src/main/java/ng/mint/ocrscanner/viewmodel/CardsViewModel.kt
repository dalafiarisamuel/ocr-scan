package ng.mint.ocrscanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
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
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val cardRepository: RecentCardRepository,
    private val offlineCardRepo: OfflineCardRepository,
    private val requestHandler: RequestHandler
) : ViewModel() {

    private val appChannel = Channel<CardResult>(Channel.BUFFERED)
    val eventFlow = appChannel.receiveAsFlow()


    var dataFlow: MutableLiveData<RecentCardsState> = MutableLiveData(RecentCardsState.EmptyList)
        private set

    init {
        fetchRecentCardAndPostToLiveData()
    }

    private suspend fun updateData(cardResult: CardResult) = appChannel.send(cardResult)

    fun processCardDetail(value: String) {

        viewModelScope.launch {

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
        viewModelScope.launch {
            offlineCardRepo.insertSingle(
                OfflineCard(
                    bin = bin,
                    dateCreated = Date().formatToViewDateTimeDefaults()
                )
            )
        }
    }

    private fun fetchRecentCardAndPostToLiveData() {
        viewModelScope.launch {

            cardRepository.getRecentCardDataListLive()
                .catch { emit(ArrayList()) }
                .collect {
                    if (it.isEmpty()) {
                        dataFlow.postValue(RecentCardsState.EmptyList)
                    } else {
                        dataFlow.postValue(RecentCardsState.RecentCardList(it))
                    }
                }
        }
    }


    fun insertSingleRecentCard(recentCard: RecentCard) =
        viewModelScope.launch { cardRepository.insertSingleRecentCard(recentCard) }

    fun deleteRecentCard(recentCard: RecentCard) =
        viewModelScope.launch { cardRepository.deleteRecentCard(recentCard) }

    suspend fun recentCardsCount(): Long = cardRepository.getRecentCardsCount()

    fun getRecentCardDataListLiveData() = cardRepository.getRecentCardDataListLiveData()

    fun getRecentCardsListFlow() = cardRepository.getRecentCardDataListLive()

    fun cleanRecentCardsTable() =
        viewModelScope.launch { cardRepository.cleanRecentCardTable() }

}

