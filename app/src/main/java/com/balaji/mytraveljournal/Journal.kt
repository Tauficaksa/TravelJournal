package com.balaji.mytraveljournal

data class Journal(
    val id:String,
    val userid:String,
    val profileImage: String?,
    val name: String?,
    val title: String,
    val description: String,
    val journalImage: String,
    var isFollowed: Boolean,
    var isLiked: Boolean
)
