package ng.mint.ocrscanner.networking

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import ng.mint.ocrscanner.callbacks.CardCallable
import ng.mint.ocrscanner.model.CardResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

open class RequestHandler {

    private var client: ApiInterface

    init {
        val retrofit: Retrofit = RetrofitCompat.getInstance("false")
        this.client = retrofit.create(ApiInterface::class.java)
    }


    open fun getCardDetail(@NonNull cardPan: String, @Nullable callback: CardCallable?) {

        client.getCardDetail(cardPan = cardPan).enqueue(object : Callback<CardResponse> {
            override fun onResponse(call: Call<CardResponse>, response: Response<CardResponse>) {
                when (response.isSuccessful) {

                    true -> {
                        callback?.onGetCard(response.body())
                    }

                    false -> {
                        callback?.onGetCardError(response.errorBody(), response.code())
                    }
                }
            }

            override fun onFailure(call: Call<CardResponse>, t: Throwable) {

                callback?.onGetCardFailureCall(call, t)
            }

        })
    }
}