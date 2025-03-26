package com.balaji.mytraveljournal

data class Journal(
    val profileImage: Int,
    val name: String,
    val title: String,
    val description: String,
    val journalImage: Int,
    var isFollowed: Boolean,
    var isLiked: Boolean
)
