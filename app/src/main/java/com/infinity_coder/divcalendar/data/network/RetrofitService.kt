package com.infinity_coder.divcalendar.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

    private const val DIV_CALENDAR_URL = "http://div-calendar.herokuapp.com/"

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
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        addInterceptor(logging)
        connectTimeout(10, TimeUnit.SECONDS)
        readTimeout(10, TimeUnit.SECONDS)
        writeTimeout(10, TimeUnit.SECONDS)
    }
}