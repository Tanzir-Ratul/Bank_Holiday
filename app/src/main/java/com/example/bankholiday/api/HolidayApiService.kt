package com.example.bankholiday.api

import com.example.bankholiday.api.models.HolidayData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HolidayApiService {
    @GET("holidays")
    suspend fun getHolidays(
        @Query("api_key") apiKey: String?,
        @Query("country") country: String?,
        @Query("year") year: String?
    ): Response<HolidayData>
}