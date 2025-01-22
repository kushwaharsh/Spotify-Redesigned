package com.example.spotify.models

data class EventsResponseModel(
    val eventName: String,
    val eventVenue : String,
    val eventImage: Int,
    val date : String
)
