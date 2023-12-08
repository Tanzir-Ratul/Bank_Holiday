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
    fun provideApiKey(): String = "1AmhKSrBVmfL3EcwAmTAT81SUSvkKyEa"

    @Provides
    @Singleton
    @Named("bearer_token")
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

                    /*originalRequest.url.toString().startsWith("https://calendarific.com/api/v2/") -> {
                        // Add API key for the second base URL
                        originalRequest.newBuilder()
                            .build()
                    }*/

                    else -> originalRequest
                }
                chain.proceed(newRequest)
            }
            cache(cache)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            connectTimeout(30, TimeUnit.SECONDS)

        }.build()
    }

   /*fun retrofitObjWithApiKey(baseUrl: String, gson: Gson, httpClient: OkHttpClient): Retrofit {
        return if (baseUrl == "https://calendarific.com/api/v2/") {
            retrofitObj(baseUrl, gson, httpClient.newBuilder()
                .addInterceptor { chain ->
                    // Include the API key in the request headers only for the second base URL
                    val originalRequest = chain.request()
                    val newRequest = originalRequest.newBuilder()
                        .build()
                    chain.proceed(newRequest)
                }
                .build()
            )
        } else {
            retrofitObj(baseUrl, gson, httpClient)
        }
    }*/

    fun retrofitObj(baseUrl: String, gson: Gson, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .client(httpClient)
            .build()
    }
}


/*@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().setPrettyPrinting().create()
    }

    @Provides
    @Singleton
    fun createCache(application: Application): Cache {
        val cacheSize = 5L  1024L  1024L // 5 MB
        return Cache(File(application.cacheDir, "${application.packageName}.cache"), cacheSize)
    }

    @Provides
    @Singleton
    fun createOkHttpClient(cache: Cache?): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                val logging = httpLoggingInterceptor.apply {
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                }
                addInterceptor(logging)
            }
            cache(cache)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(1, TimeUnit.MINUTES)
            connectTimeout(30, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstances(gson: Gson, httpClient: OkHttpClient): Map<String, ApiInterface> {
        val baseUrlMap = mapOf(
            "weather" to AppConstant.BASE_URL_WEATHER,
            // Add other base URLs and their keys here
        )

        val retrofitMap = mutableMapOf<String, ApiInterface>()
        baseUrlMap.forEach { (key, value) ->
            val retrofit = Retrofit.Builder()
                .baseUrl(value)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()
            retrofitMap[key] = retrofit.create(ApiInterface::class.java)
        }
        return retrofitMap
    }
}*/

