package ng.mint.ocrscanner.networking

import ng.mint.ocrscanner.model.CardResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("{card_number}")
    suspend fun getCardDetail(@Path("card_number") cardPan: String): Response<CardResponse>
}