package com.balaji.mytraveljournal.api

import com.balaji.mytraveljournal.models.TravelJournals
import com.balaji.mytraveljournal.models.User
import com.balaji.mytraveljournal.models.Users
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

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

    @Multipart
    @PUT("api/users/{id}")
    fun updateUser(
        @Path("id") id:String,
        @Part("name") name:RequestBody,
        @Part("email") email:RequestBody,
        @Part image:MultipartBody.Part?
    ):Call<User>

    @GET("api/journals/alljournals/{id}")
    fun getAllJournals(
        @Path("id") id:String,
    ):Call<TravelJournals>

    @GET("api/follows/getFollowingUsers/{id}")
    fun getFollowingUsers(
        @Path("id") id:String
    ):Call<Users>

    @GET("api/likes/getLikedJournals/{id}")
    fun getLikedJournals(
        @Path("id") id:String
    ):Call<TravelJournals>

    @GET("api/users/{id}")
    fun getUser(
        @Path("id") id:String
    ):Call<User>

    @POST("api/likes/like")
    fun likeJournal(
        @Body details:Map<String,String>
    ):Call<Unit>

    @POST("api/likes/unlike")
    fun unlikeJournal(
        @Body details:Map<String,String>
    ):Call<Unit>

    @POST("api/follows/follow")
    fun followUser(
        @Body details:Map<String,String>
    ):Call<Unit>

    @POST("api/follows/unfollow")
    fun unfollowUser(
        @Body details:Map<String,String>
    ):Call<Unit>

}