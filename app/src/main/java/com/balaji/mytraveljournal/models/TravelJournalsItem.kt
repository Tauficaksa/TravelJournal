package com.balaji.mytraveljournal.models

data class TravelJournalsItem(
    val description: String,
    val id: String,
    val image: String,
    val location: String,
    val name: String,
    val user_id: String
)