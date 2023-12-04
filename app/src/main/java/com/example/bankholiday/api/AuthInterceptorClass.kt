package com.example.bankholiday.api

import android.app.Application
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptorClass(private val application: Application) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()


        request = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("api-version", "1.0")
           // .addHeader("Authorization", "Bearer ${SessionManager.accessToken}")
            .build()


        val response: Response = chain.proceed(request)
        if (response.code == 401) {
          //  (application as MainApplication).refreshToken()
        }
        return response
    }
}