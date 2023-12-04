package com.example.bankholiday.di

import com.example.bankholiday.api.ApiService
import com.example.bankholiday.api.RetrofitClient.retrofitObj
import com.example.bankholiday.api.RetrofitClient.retrofitObjWithApiKey
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Named("bankHoliday")
    fun provideBaseURL() = "https://hrm.digikoein.com/api/"

    @Provides
    @Named("otherBaseUrl")
    fun provideOtherBaseURL() = "https://other.api.com/"

    @Provides
    @Singleton
    fun provideRetrofitInstance(@Named("bankHoliday") baseUrl: String,gson:Gson, httpClient: OkHttpClient): ApiService{
        return retrofitObj(baseUrl,gson,httpClient).create(ApiService::class.java)

    }

    @Provides
    @Singleton
    @Named("otherRetrofit")
    fun provideOtherRetrofitInstance(
        @Named("otherBaseUrl") baseUrl: String,
        gson: Gson,
        httpClient: OkHttpClient
    ): ApiService {
        return retrofitObjWithApiKey(baseUrl, gson, httpClient).create(ApiService::class.java)
    }

}
