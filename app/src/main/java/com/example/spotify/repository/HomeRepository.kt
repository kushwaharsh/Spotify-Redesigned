package com.example.spotify.repository

import com.example.spotify.models.ExternalIds
import com.example.spotify.network.ServiceHelper
import com.example.spotify.util.BaseRepository

object HomeRepository : BaseRepository() {

suspend fun getAllAlbums(ids: String , apiKey: String , apiHost: String) = safeApiCall {
    ServiceHelper().getApi().getAllAlbums(ids , apiKey , apiHost)
}
}