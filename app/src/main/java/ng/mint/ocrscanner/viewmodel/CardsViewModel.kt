package ng.mint.ocrscanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ng.mint.ocrscanner.model.CardResult
import ng.mint.ocrscanner.model.RecentCard
import ng.mint.ocrscanner.networking.RequestHandler
import ng.mint.ocrscanner.toRecentCard
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val repository: CardsRepository,
    private val requestHandler: RequestHandler
) : ViewModel() {

    var data = MutableSharedFlow<CardResult>(replay = 1)
        private set

    private suspend fun updateData(cardResult: CardResult) = data.emit(cardResult)

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

    fun getRecentCardDataListLive() = repository.getRecentCardDataListLive()

    private fun insertSingleRecentCard(recentCard: RecentCard) =
        viewModelScope.launch(Dispatchers.IO) { repository.insertSingleRecentCard(recentCard) }

    fun delete(recentCard: RecentCard) =
        viewModelScope.launch(Dispatchers.IO) { repository.delete(recentCard) }

    fun cleanTable() = viewModelScope.launch(Dispatchers.IO) { repository.cleanTable() }

    suspend fun getCount(): Long = repository.getCount()


}