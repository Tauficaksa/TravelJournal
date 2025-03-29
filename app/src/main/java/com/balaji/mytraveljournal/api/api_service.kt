package com.balaji.mytraveljournal.api

import com.balaji.mytraveljournal.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface api_service {

    @POST("api/users")
    fun registerUser(@Body user:User):Call<User>

    @Multipart
    @POST("api/journals")
    fun uploadJournal(
        @Part("user_id") userId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("location") location: RequestBody,
        @Part image: MultipartBody.Part?
    ):Call<Unit>

    @POST("api/users/login")
    fun loginuser(@Body credentials:Map<String,String>):Call<User>


}