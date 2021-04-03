package ng.mint.ocrscanner.networking

import androidx.annotation.NonNull
import javax.inject.Inject

class RequestHandler @Inject constructor(private val client: ApiInterface) {

    suspend fun getCardDetail(@NonNull cardPan: String) = client.getCardDetail(cardPan = cardPan)
}