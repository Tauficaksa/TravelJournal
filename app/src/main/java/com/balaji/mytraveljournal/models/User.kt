package com.balaji.mytraveljournal.models

data class User(
    val email: String,
    val firebase_uid: String?=null,
    val id: String?=null,
    val name: String,
    val profile_image: Any?=null,
    val pwd: String
)