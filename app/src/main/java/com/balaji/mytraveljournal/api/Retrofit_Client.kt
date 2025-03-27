package com.balaji.mytraveljournal.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit_Client {

    private const val BASE_URL="http://10.0.2.2:5000"
    val instance:api_service by lazy {
        val retrofit=Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(api_service::class.java)
    }
}