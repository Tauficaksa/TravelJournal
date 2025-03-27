package com.balaji.mytraveljournal.api

import com.balaji.mytraveljournal.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface api_service {

    @POST("api/users")
    fun registerUser(@Body user:User):Call<User>
}