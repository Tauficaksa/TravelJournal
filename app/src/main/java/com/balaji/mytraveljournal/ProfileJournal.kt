package com.balaji.mytraveljournal

data class ProfileJournal(
    val id:String,
    var title:String,
    var location:String,
    var description:String,
    val journal_image:String,
    val likes:Int
)
