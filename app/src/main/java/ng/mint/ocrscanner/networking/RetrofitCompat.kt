package ng.mint.ocrscanner.networking

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitCompat {

    fun getInstance(token: String = "false"): Retrofit {

        val okHttpBuilder = OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .cache(null)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)

        if (token != "false") {
            okHttpBuilder.addInterceptor { chain: Interceptor.Chain ->
                val request = chain.request()
                val newRequest = request.newBuilder().header("Authorization", "Bearer $token")
                chain.proceed(newRequest.build())
            }
        }
        return Retrofit.Builder()
            .baseUrl("https://lookup.binlist.net/")
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .client(okHttpBuilder.build())
            .build()
    }
}