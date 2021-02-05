package ng.mint.ocrscanner.networking;

import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitCompat {

    @NotNull
    public static Retrofit getInstance(final String token) {

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(null)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS);

        if (!token.equals("false")) {
            okHttpBuilder.addInterceptor(chain -> {
                Request request = chain.request();

                Request.Builder newRequest = request.newBuilder().header("Authorization", "Bearer " + token);

                return chain.proceed(newRequest.build());
            });
        }


        return new Retrofit.Builder()
                .baseUrl("https://lookup.binlist.net/")
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                .client(okHttpBuilder.build())
                .build();
    }
}
