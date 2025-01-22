package com.example.spotify.models

data class GetAlbumsResponseModel(
    val albums: List<Album>?
)

data class Album(
    val album_type: String?,
    val artists: List<Artist>?,
    val external_ids: ExternalIds?,
    val external_urls: ExternalUrlsX?,
    val genres: List<Any?>?,
    val id: String?,
    val images: List<Image>?,
    val is_playable: Boolean?,
    val label: String?,
    val name: String?,
    val popularity: Int?,
    val release_date: String?,
    val release_date_precision: String?,
    val total_tracks: Int?,
    val tracks: Tracks?,
    val type: String?,
    val uri: String?
)

data class Artist(
    val external_urls: ExternalUrlsX?,
    val id: String?,
    val name: String?,
    val type: String?,
    val uri: String?
)

data class Image(
    val height: Int?,
    val url: String?,
    val width: Int?
)

data class Tracks(
    val items: List<Item>?,
    val limit: Int?,
    val next: Any?,
    val offset: Int?,
    val previous: Any?,
    val total: Int?
)

data class Item(
    val artists: List<Artist>?,
    val disc_number: Int?,
    val duration_ms: Int?,
    val explicit: Boolean?,
    val external_urls: ExternalUrlsX?,
    val id: String?,
    val is_local: Boolean?,
    val is_playable: Boolean?,
    val name: String?,
    val preview_url: String?,
    val track_number: Int?,
    val type: String?,
    val uri: String?
)

data class ExternalIds(
    val upc: String?
)

data class ExternalUrlsX(
    val spotify: String?
)