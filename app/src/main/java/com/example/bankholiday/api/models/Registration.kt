package com.example.bankholiday.api.models


import com.google.gson.annotations.SerializedName

data class Registration(
    @SerializedName("token")
    var token: String?, // eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2hybS5kaWdpa29laW4uY29tL2FwaS9yZWdpc3RlciIsImlhdCI6MTcwMTY3MzY5MywiZXhwIjoxNzAxNjc3MjkzLCJuYmYiOjE3MDE2NzM2OTMsImp0aSI6IkhIenZzV1JPSDdjU1RQQzIiLCJzdWIiOiI0NSIsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.a2mjQwEhSWhXa4HFlLrkcSQTTIiMuRIIO-6XZZdKOO8
    @SerializedName("user")
    var user: User?
) {
    data class User(
        @SerializedName("created_at")
        var createdAt: String?, // 2023-12-04T07:08:13.000000Z
        @SerializedName("email")
        var email: String?, // ratul@live.com
        @SerializedName("id")
        var id: Int?, // 45
        @SerializedName("name")
        var name: String?, // ratul
        @SerializedName("updated_at")
        var updatedAt: String?, // 2023-12-04T07:08:13.000000Z
        @SerializedName("username")
        var username: Any? // null
    )
}