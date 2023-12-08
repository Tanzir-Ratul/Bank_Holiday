package com.example.bankholiday.api.models


import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class HolidayData(
    @SerializedName("meta")
    var meta: Meta?,
    @SerializedName("response")
    var response: Response?
) {
    data class Meta(
        @SerializedName("code")
        var code: Int? // 200
    )

    data class Response(
        @SerializedName("holidays")
        var holidays: List<Holiday?>?
    ) {
        data class Holiday(
            @SerializedName("canonical_url")
            var canonicalUrl: String?, // https://calendarific.com/holiday/bangladesh/new-year-day
            @SerializedName("country")
            var country: Country?,
            @SerializedName("date")
            var date: Date?,
            @SerializedName("description")
            var description: String?, // New Year’s Day is the first day of the year, or January 1, in the Gregorian calendar.
            @SerializedName("locations")
            var locations: String?, // All
            @SerializedName("name")
            var name: String?, // New Year's Day
            @SerializedName("primary_type")
            var primaryType: String?, // Optional Holiday
            @SerializedName("states")
            var states: String?, // All
            @SerializedName("type")
            var type: List<String?>?,
            @SerializedName("urlid")
            var urlid: String? // bangladesh/new-year-day
        ) {
            data class Country(
                @SerializedName("id")
                var id: String?, // bd
                @SerializedName("name")
                var name: String? // Bangladesh
            )

            data class Date(
                @SerializedName("datetime")
                var datetime: Datetime?,
                @SerializedName("iso")
                var iso: String?, // 2023-01-01
                @SerializedName("timezone")
                var timezone: Timezone?
            ) {
                data class Datetime(
                    @SerializedName("day")
                    var day: Int?, // 1
                    @SerializedName("hour")
                    var hour: Int?, // 3
                    @SerializedName("minute")
                    var minute: Int?, // 24
                    @SerializedName("month")
                    var month: Int?, // 1
                    @SerializedName("second")
                    var second: Int?, // 20
                    @SerializedName("year")
                    var year: Int? // 2023
                )

                data class Timezone(
                    @SerializedName("offset")
                    var offset: String?, // +06:00
                    @SerializedName("zoneabb")
                    var zoneabb: String?, // BST
                    @SerializedName("zonedst")
                    var zonedst: Int?, // 0
                    @SerializedName("zoneoffset")
                    var zoneoffset: Int?, // 21600
                    @SerializedName("zonetotaloffset")
                    var zonetotaloffset: Int? // 21600
                )
            }
        }
    }
}