package com.example.bankholiday.api

import android.app.Application
import com.example.bankholiday.session.SessionManager
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
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitClient {

    @Provides
    @Singleton
    fun getGson(): Gson {
        return GsonBuilder().setLenient().setPrettyPrinting().create()
    }

    @Provides
    @Singleton
    @Named("api_key")
    fun provideApiKey(): String = "your_api_key_here"

    @Provides
    @Singleton
    fun provideBearerToken(sessionManager: SessionManager): String {
        return sessionManager.accessToken ?: ""
    }

    @Provides
    @Singleton
    fun createCache(application: Application): Cache {
        val cacheSize = 5L * 1024L * 1024L
        return Cache(File(application.cacheDir, "${application.packageName}.cache"), cacheSize)
    }

    @Provides
    @Singleton
    fun createOkhttpClient(
        cache: Cache, @Named("api_key") apiKey: String,
        @Named("bearer_token") bearerToken: String
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            val loggingInterceptor = httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
            addInterceptor(loggingInterceptor)
            addInterceptor(OkHttpProfilerInterceptor())
            addInterceptor { chain ->
                // Include the appropriate authentication in the request headers based on the base URL
                val originalRequest = chain.request()
                val newRequest = when {
                    originalRequest.url.toString().startsWith("https://hrm.digikoein.com/") -> {
                        // Add Bearer token for the first base URL
                        originalRequest.newBuilder()
                            .header("Authorization", "Bearer $bearerToken")
                            .build()
                    }

                    originalRequest.url.toString().startsWith("https://other.api.com/") -> {
                        // Add API key for the second base URL
                        originalRequest.newBuilder()
                            .header("Api-Key", apiKey)
                            .build()
                    }

                    else -> originalRequest
                }
                chain.proceed(newRequest)
            }
            cache(cache)
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            connectTimeout(20, TimeUnit.SECONDS)

        }.build()
    }

    fun retrofitObjWithApiKey(baseUrl: String, gson: Gson, httpClient: OkHttpClient): Retrofit {
        return if (baseUrl == "https://other.api.com/") {
            retrofitObj(baseUrl, gson, httpClient.newBuilder()
                .addInterceptor { chain ->
                    // Include the API key in the request headers only for the second base URL
                    val originalRequest = chain.request()
                    val newRequest = originalRequest.newBuilder()
                        .header("Api-Key", provideApiKey())
                        .build()
                    chain.proceed(newRequest)
                }
                .build()
            )
        } else {
            retrofitObj(baseUrl, gson, httpClient)
        }
    }

    fun retrofitObj(baseUrl: String, gson: Gson, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .client(httpClient)
            .build()
    }
}

