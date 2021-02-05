package ng.mint.ocrscanner.networking

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ng.mint.ocrscanner.callbacks.CardCallable
import retrofit2.Retrofit

open class RequestHandler constructor(coroutineScope: CoroutineScope) {

    private var client: ApiInterface
    private var coroutineScope: CoroutineScope

    init {
        val retrofit: Retrofit = RetrofitCompat.getInstance()
        this.client = retrofit.create(ApiInterface::class.java)
        this.coroutineScope = coroutineScope
    }


    open fun getCardDetail(@NonNull cardPan: String, @Nullable callback: CardCallable?) {

        coroutineScope.launch(Dispatchers.IO) {
            val response = client.getCardDetail(cardPan = cardPan)
            withContext(Dispatchers.Main) {
                when (response.isSuccessful) {
                    true -> {
                        callback?.onGetCard(response.body())
                    }
                    false -> {
                        callback?.onGetCardError(response.errorBody(), response.code())
                    }
                }
            }

        }
    }
}