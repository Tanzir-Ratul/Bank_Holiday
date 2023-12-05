package com.example.bankholiday.di

import android.content.Context
import android.content.SharedPreferences
import com.example.bankholiday.api.ApiService
import com.example.bankholiday.api.RetrofitClient.retrofitObj
import com.example.bankholiday.api.models.HolidayApiService
import com.example.bankholiday.api.models.HolidayData
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Named("loginRetrofit")
    fun provideBaseURL() = "https://hrm.digikoein.com/api/"

    @Provides
    @Named("holidayRetrofit")
    fun provideOtherBaseURL() = "https://calendarific.com/api/v2/"

    @Provides
    @Singleton
    fun provideRetrofitInstance(@Named("loginRetrofit") baseUrl: String,gson:Gson, httpClient: OkHttpClient): ApiService{
        return retrofitObj(baseUrl,gson,httpClient).create(ApiService::class.java)

    }
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
    }
    @Provides
    @Singleton
    fun provideOtherRetrofitInstance(
        @Named("holidayRetrofit") baseUrl: String,
        gson: Gson,
        httpClient: OkHttpClient
    ): HolidayApiService {
        return retrofitObj(baseUrl, gson, httpClient).create(HolidayApiService::class.java)
    }

}
