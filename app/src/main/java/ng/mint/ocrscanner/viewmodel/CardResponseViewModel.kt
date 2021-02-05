package ng.mint.ocrscanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ng.mint.ocrscanner.callbacks.CardCallable
import ng.mint.ocrscanner.model.CardResponse
import ng.mint.ocrscanner.model.CardResult
import ng.mint.ocrscanner.networking.RequestHandler
import okhttp3.ResponseBody
import retrofit2.Call

class CardResponseViewModel : ViewModel() {

    private val requestHandler: RequestHandler = RequestHandler(viewModelScope)

    var data = MutableLiveData<CardResult>()
        private set

    private fun updateData(cardResult: CardResult) = data.postValue(cardResult)

    fun processCardDetail(value: String) {
        requestHandler.getCardDetail(value, object : CardCallable {
            override fun onGetCard(cardResponse: CardResponse?) {

                updateData(
                    if (cardResponse == null) CardResult.Failure
                    else CardResult.Success(
                        cardResponse
                    )
                )
            }

            override fun onGetCardError(error: ResponseBody?, responseCode: Int) {
                updateData(CardResult.Failure)
            }

            override fun onGetCardFailureCall(call: Call<CardResponse>?, t: Throwable?) {
                updateData(CardResult.Failure)
            }
        })

    }


}