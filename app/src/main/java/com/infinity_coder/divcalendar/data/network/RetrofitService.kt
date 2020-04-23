package com.infinity_coder.divcalendar.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    private const val DIV_CALENDAR_URL = "https://div-calendar-prod.herokuapp.com/"

    val divCalendarApi: DivCalendarApi by lazy {
        val client = provideRetrofitClient(provideOkHttpClientBuilder().build())
        client.create(DivCalendarApi::class.java)
    }

    private fun provideRetrofitClient(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(DIV_CALENDAR_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private fun provideOkHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder().apply {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val actualizerInterceptor = ActualizerInterceptor()
        addInterceptor(loggingInterceptor)
        addInterceptor(actualizerInterceptor)
        connectTimeout(20, TimeUnit.SECONDS)
        readTimeout(20, TimeUnit.SECONDS)
        writeTimeout(20, TimeUnit.SECONDS)
    }
}