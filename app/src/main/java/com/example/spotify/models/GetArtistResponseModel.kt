package com.example.spotify.models

data class GetArtistResponseModel(
    val artists: List<SuggestedArtist?>?
)

data class SuggestedArtist(
    val genres: List<String?>?,
    val id: String?,
    val images: List<Image?>?,
    val name: String?,
    val popularity: Int?,
    val type: String?,
    val uri: String?
)