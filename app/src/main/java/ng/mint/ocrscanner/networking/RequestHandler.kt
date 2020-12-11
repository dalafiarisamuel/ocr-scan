package ng.mint.ocrscanner.networking

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ng.mint.ocrscanner.callbacks.CardCallable
import retrofit2.Retrofit

open class RequestHandler constructor(lifecycleCoroutineScope: LifecycleCoroutineScope) {

    private var client: ApiInterface
    private var lifecycleCoroutineScope: LifecycleCoroutineScope

    init {
        val retrofit: Retrofit = RetrofitCompat.getInstance("false")
        this.client = retrofit.create(ApiInterface::class.java)
        this.lifecycleCoroutineScope = lifecycleCoroutineScope
    }


    open fun getCardDetail(@NonNull cardPan: String, @Nullable callback: CardCallable?) {

        lifecycleCoroutineScope.launch(Dispatchers.IO) {
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