package com.example.bankholiday.api

import com.example.bankholiday.api.models.Login
import com.example.bankholiday.api.models.Registration
import retrofit2.http.Field
import retrofit2.http.POST

interface ApiService {


    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Registration

    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Login
}