package com.example.bankholiday.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankholiday.api.models.HolidayData
import com.example.bankholiday.api.models.Login
import com.example.bankholiday.api.models.MonthWithHolidays
import com.example.bankholiday.ui.repository.HolidayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HolidayViewModel @Inject constructor(private val repository: HolidayRepository) :
    ViewModel() {

    private var _getHolidayData = MutableLiveData<MutableList<MonthWithHolidays>?>()
    val getHolidayData: MutableLiveData<MutableList<MonthWithHolidays>?> = _getHolidayData

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading




    fun getHolidaysData(year: String?) {
        try {
            _isLoading.value = true
            viewModelScope.launch {
                val response = repository.getHolidaysData(year)
                if (response.isSuccessful) {
                    if (response.body() != null && response.body()?.meta?.code == 200) {
                        val holidays: List<HolidayData.Response.Holiday?>? =
                            response.body()?.response?.holidays
                        // Create a map to group holidays by month
                        val holidaysByMonth = holidays.orEmpty().groupBy {
                            it?.date?.datetime?.month
                        }
                        Log.d("holidaysByMonth", "${holidaysByMonth.toString()}")
                        // Convert the grouped data to custom data class
                        val monthsWithHolidays = holidaysByMonth.map { (month, holidays) ->
                            val monthName = getMonthName(month)
                            val holidayItems = holidays.mapNotNull {
                                val date = it?.date?.iso ?: return@mapNotNull null
                                val name = it.name ?: ""
                                val description = it.description ?: ""
                                MonthWithHolidays.HolidayItem(date, name, description)
                            }
                            Log.d("holidayItems", "${holidayItems.toString()}")

                            MonthWithHolidays(monthName, holidayItems)
                        }
                        _getHolidayData.value = monthsWithHolidays.toMutableList()
                    } else {
                        _getHolidayData.value = null
                    }
                } else {
                    _getHolidayData.value = null
                }
                _isLoading.value = false
            }
        } catch (e: Exception) {
            _isLoading.value = false
            e.printStackTrace()
        }
    }

    private fun getMonthName(month: Int?): String {
        return when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Unknown"
        }
    }


}