package com.example.bankholiday.api.models


import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("token")
    var token: String?, // eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2hybS5kaWdpa29laW4uY29tL2FwaS9sb2dpbiIsImlhdCI6MTcwMTY3MTYwNCwiZXhwIjoxNzAxNjc1MjA0LCJuYmYiOjE3MDE2NzE2MDQsImp0aSI6ImJaYTkzaUtBeGd4ZWp0OEYiLCJzdWIiOiI0NCIsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.k-eWFirO6am3NU33U8MZWiWWEMK3lHcavyq_VYcyf0A
    @SerializedName("user")
    var user: User?
) {
    data class User(
        @SerializedName("avatar")
        var avatar: Any?, // null
        @SerializedName("created_at")
        var createdAt: String?, // 2023-12-04T03:14:54.000000Z
        @SerializedName("deleted_at")
        var deletedAt: Any?, // null
        @SerializedName("email")
        var email: String?, // abcd@gmail.com
        @SerializedName("email_verified_at")
        var emailVerifiedAt: Any?, // null
        @SerializedName("id")
        var id: Int?, // 44
        @SerializedName("name")
        var name: String?, // ABCD
        @SerializedName("updated_at")
        var updatedAt: String?, // 2023-12-04T03:14:54.000000Z
        @SerializedName("username")
        var username: Any? // null
    )
}