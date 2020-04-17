package com.ajay.synccontacts.network

import retrofit2.Retrofit

object ApiClient {

    private var mRestApi: RestApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .build()

        mRestApi = retrofit.create(RestApi::class.java)
    }

    fun getClient(): RestApi {
        return mRestApi
    }
}