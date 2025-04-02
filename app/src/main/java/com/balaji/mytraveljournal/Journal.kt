package com.balaji.mytraveljournal

data class Journal(
    val id:String,
    val userid:String,
    var likecount:Int,
    val profileImage: String?,
    val name: String?,
    val title: String,
    val location:String,
    val description: String,
    val journalImage: String,
    var isFollowed: Boolean,
    var isLiked: Boolean
)
