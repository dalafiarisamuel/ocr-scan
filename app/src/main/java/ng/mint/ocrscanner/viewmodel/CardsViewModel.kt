package ng.mint.ocrscanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ng.mint.ocrscanner.callbacks.CardCallable
import ng.mint.ocrscanner.model.CardResponse
import ng.mint.ocrscanner.model.CardResult
import ng.mint.ocrscanner.model.RecentCard
import ng.mint.ocrscanner.networking.RequestHandler
import ng.mint.ocrscanner.toRecentCard
import okhttp3.ResponseBody
import retrofit2.Call

class CardsViewModel(application: Application, private val repository: CardsRepository) :
    AndroidViewModel(application) {

    private val requestHandler: RequestHandler = RequestHandler(viewModelScope)

    var data = MutableSharedFlow<CardResult>(replay = 1)
        private set

    private suspend fun updateData(cardResult: CardResult) = data.emit(cardResult)

    fun processCardDetail(value: String) {
        requestHandler.getCardDetail(value, object : CardCallable {
            override fun onGetCard(cardResponse: CardResponse?) {

                viewModelScope.launch {
                    when (cardResponse) {
                        null -> {
                            updateData(CardResult.Failure)
                        }
                        else -> {
                            updateData(CardResult.Success(cardResponse))
                            insertSingleRecentCard(cardResponse.toRecentCard(value))
                        }
                    }
                }

            }

            override fun onGetCardError(error: ResponseBody?, responseCode: Int) {
                viewModelScope.launch {
                    updateData(CardResult.Failure)
                }
            }

            override fun onGetCardFailureCall(call: Call<CardResponse>?, t: Throwable?) {
                viewModelScope.launch {
                    updateData(CardResult.Failure)
                }
            }
        })

    }

    fun getRecentCardDataListLive() = repository.getRecentCardDataListLive()

    private fun insertSingleRecentCard(recentCard: RecentCard) =
        viewModelScope.launch(Dispatchers.IO) { repository.insertSingleRecentCard(recentCard) }

    fun delete(recentCard: RecentCard) =
        viewModelScope.launch(Dispatchers.IO) { repository.delete(recentCard) }

    fun cleanTable() = viewModelScope.launch(Dispatchers.IO) { repository.cleanTable() }

    suspend fun getCount(): Long = repository.getCount()


}