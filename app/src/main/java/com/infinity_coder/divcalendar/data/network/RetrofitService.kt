package com.infinity_coder.divcalendar.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.infinity_coder.divcalendar.data.network.gson_serializers.SecListDeserializer
import com.infinity_coder.divcalendar.data.network.model.ShortSecList
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

    private const val MOEX_URL = "https://iss.moex.com/"

    val moexApi: MoexApi by lazy {
        val client = provideRetrofitClient(provideOkHttpClientBuilder().build())
        client.create(MoexApi::class.java)
    }

    private fun provideRetrofitClient(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(MOEX_URL)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(getMoexGsonConverter()))
        .client(okHttpClient)
        .build()

    private fun getMoexGsonConverter(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(ShortSecList::class.java, SecListDeserializer())
            .create()
    }

    private fun provideOkHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder().apply {
        connectTimeout(10, TimeUnit.SECONDS)
        readTimeout(10, TimeUnit.SECONDS)
        writeTimeout(10, TimeUnit.SECONDS)
    }

}