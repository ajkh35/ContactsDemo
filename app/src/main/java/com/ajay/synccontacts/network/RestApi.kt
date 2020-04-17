package com.ajay.synccontacts.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RestApi {

    @GET(".")
    fun getNumbersList(): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/register")
    fun registerNumber(@Field("number") number: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/deregister")
    fun deregisterNumber(@Field("number") number: String): Call<ResponseBody>
}