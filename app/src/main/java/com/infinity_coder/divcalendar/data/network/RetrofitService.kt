package com.infinity_coder.divcalendar.data.network

import com.infinity_coder.divcalendar.AppConfig
import com.infinity_coder.divcalendar.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

    private const val DIV_CALENDAR_URL_PROD = "https://div-calendar-prod.herokuapp.com/"
    private const val DIV_CALENDAR_URL_DEV = "https://div-calendar.herokuapp.com/"

    lateinit var divCalendarApi: DivCalendarApi
        private set

    init {
        initApi()
    }

    fun initApi() {
        val baseUrl = provideBaseUrl()
        divCalendarApi = buildDivCalendarApi(baseUrl)
    }

    private fun provideBaseUrl(): String {
        if (!BuildConfig.DEBUG) return DIV_CALENDAR_URL_PROD

        return when (AppConfig.serverConfig) {
            AppConfig.DEV -> DIV_CALENDAR_URL_DEV
            else -> DIV_CALENDAR_URL_PROD
        }
    }

    private fun buildDivCalendarApi(baseUrl: String): DivCalendarApi {
        val okHttpClient = provideOkHttpClientBuilder().build()
        val client = provideRetrofitClient(okHttpClient, baseUrl)
        return client.create(DivCalendarApi::class.java)
    }

    private fun provideRetrofitClient(okHttpClient: OkHttpClient, baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
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