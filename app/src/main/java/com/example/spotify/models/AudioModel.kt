package com.example.spotify.models

sealed class AudioModel {

    // Model for top mixes with extended data fields
    data class YourTopMixesModel(
        val title: String,
        val path: String,
        val albumArt: String,
        val duration: Long,
        val artistName: String,
        val albumName: String = "Unknown Album", // Default value
        val genre: String = "Unknown Genre",    // Default value
        val trackNumber: Int = 0,              // Default value
        val size: Long = 0L                    // Default value
    ) : AudioModel()

    // Model for general audio files with basic data fields
    data class AudioFileModel(
        val title: String,
        val path: String,
        val albumArt: String,
        val duration: Long,
        val artist: String = "Unknown Artist",  // Default value
        val album: String = "Unknown Album",   // Default value
        val genre: String = "Unknown Genre",   // Default value
        val trackNumber: Int = 0,             // Default value
        val size: Long = 0L                   // Default value
    ) : AudioModel()

    // Add additional models here if needed
}
