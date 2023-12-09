package com.example.bankholiday.ui.repository

import com.example.bankholiday.api.ApiService
import com.example.bankholiday.api.HolidayApiService
import com.example.bankholiday.api.models.HolidayData
import com.example.bankholiday.api.models.Login
import com.example.bankholiday.api.models.Registration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject


class HolidayRepository
@Inject
constructor(private val apiService: ApiService, private val holidayApiService: HolidayApiService) {
    suspend fun registerUser(
        name: String?,
        email: String?,
        password: String?,
    ): Response<Registration> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.registerUser(name, email, password)
            } catch (e: Exception) {
                e.printStackTrace()
                Response.success(null)
            }
        }
    }


    suspend fun loginUser(
        email: String?,
        password: String?
    ): Response<Login> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.loginUser(email, password)
            } catch (e: Exception) {
                e.printStackTrace()
                Response.success(null)
            }
        }
    }

    suspend fun getHolidaysData(year: String?): Response<HolidayData> {
        return withContext(Dispatchers.IO) {
            holidayApiService.getHolidays(
                apiKey = "1AmhKSrBVmfL3EcwAmTAT81SUSvkKyEa",
                country = "BD",
                year = year
            )
        }
    }
}
