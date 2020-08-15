package ng.mint.ocrscanner.callbacks

import ng.mint.ocrscanner.model.CardResponse
import okhttp3.ResponseBody
import retrofit2.Call

interface CardCallable {

    fun onGetCard(cardResponse: CardResponse?)

    fun onGetCardError(error: ResponseBody?, responseCode: Int)

    fun onGetCardFailureCall(
        call: Call<CardResponse>?, t: Throwable?
    )
}