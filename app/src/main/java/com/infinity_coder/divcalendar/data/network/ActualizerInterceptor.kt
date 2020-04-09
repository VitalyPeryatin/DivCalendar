package com.infinity_coder.divcalendar.data.network

import com.infinity_coder.divcalendar.domain._common.Actualizer
import okhttp3.Interceptor
import okhttp3.Response

class ActualizerInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.isSuccessful) {
            Actualizer.actualize()
        }

        return response
    }
}