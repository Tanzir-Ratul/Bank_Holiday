package com.example.bankholiday.api.models

data class MonthWithHolidays(
    val monthName: String,
    val holidayItems: List<HolidayItem>
) {
    data class HolidayItem(
        val date: String,
        val name: String,
        val description: String
    )
}