package ng.mint.ocrscanner.networking

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import ng.mint.ocrscanner.model.CardResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.net.HttpURLConnection

interface ApiInterfaceTest {

    @MockResponse(body = NetworkResponseBodyTest.getCardInformationSuccessJson)
    @Mock
    @GET("{card_number}")
    suspend fun getCardDetail(@Path("card_number") cardPan: String): Response<CardResponse>

    @MockResponse(body = "", code = HttpURLConnection.HTTP_NOT_FOUND)
    @Mock
    @GET("{card_number}")
    suspend fun getCardDetailWhenCardNotFound(@Path("card_number") cardPan: String): Response<CardResponse?>

}