package com.example.bankholiday.api

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitClient {

    @Provides
    @Singleton
    fun getGson(): Gson {
        return GsonBuilder().setLenient().setPrettyPrinting().create()
    }

    /*@Provides
    @Singleton
    fun createCache(application: Application): Cache {
        val cacheSize = 5L * 1024L * 1024L
        return Cache(File(application.cacheDir, "${application.packageName}.cache"), cacheSize)
    }*/
    @Provides
    @Singleton
    fun createOkhttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder().apply {
            //cache(cache)
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            val loggingInterceptor = httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
            addInterceptor(loggingInterceptor)
            addInterceptor(OkHttpProfilerInterceptor())
            //cache(cache)
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            connectTimeout(20, TimeUnit.SECONDS)

        }.build()
    }
    fun retrofitObj(baseUrl: String, gson: Gson, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .client(httpClient)
            .build()
    }
}

