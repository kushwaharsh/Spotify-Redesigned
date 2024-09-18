package com.example.spotify.models

sealed class AudioModel {
    // Model with more data fields
    data class YourTopMixesModel(
        val title: String,
        val path: String,
        val albumArt: String,
        val duration: Long,
        val artistName: String
    ) : AudioModel()

    // Another model, no change here
    data class AudioFileModel(
        val title: String,
        val path: String,
        val albumArt: String,
        val duration: Long
    ) : AudioModel()

    // Add other models as needed
}