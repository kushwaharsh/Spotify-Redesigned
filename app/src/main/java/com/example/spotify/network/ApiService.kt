package com.example.spotify.network

import com.example.spotify.models.GetAlbumsResponseModel
import com.example.spotify.models.GetArtistResponseModel
import com.example.spotify.util.KeyConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET(KeyConstants.GET_SUGGESTED_ARTIST)
    suspend fun getAllAlbums(
        @Query("ids") ids: String,
        @Header("x-rapidapi-key") apiKey: String,
        @Header("x-rapidapi-host") apiHost: String): GetArtistResponseModel?

}