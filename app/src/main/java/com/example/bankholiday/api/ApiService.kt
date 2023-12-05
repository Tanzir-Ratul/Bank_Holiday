package com.example.bankholiday.api

import com.example.bankholiday.api.models.Login
import com.example.bankholiday.api.models.Registration
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {


    @POST("register")
    @FormUrlEncoded
    suspend fun registerUser(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
    ): Response<Registration>

    @POST("login")
    @FormUrlEncoded
    suspend fun loginUser(
        @Field("email") email: String?,
        @Field("password") password: String?,
    ): Response<Login>



}