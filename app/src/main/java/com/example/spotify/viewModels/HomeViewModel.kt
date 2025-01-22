package com.example.spotify.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.models.GetAlbumsResponseModel
import com.example.spotify.models.GetArtistResponseModel
import com.example.spotify.repository.HomeRepository
import com.example.spotify.util.Resource
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _getAllAlbums : MutableLiveData<Resource<GetArtistResponseModel?>?> = MutableLiveData()
    val getAllAlbums : LiveData<Resource<GetArtistResponseModel?>?>
        get() = _getAllAlbums

    fun getAllAlbums(ids: String , apiKey: String , apiHost: String){
        viewModelScope.launch {
            _getAllAlbums.value = Resource.Loading
            _getAllAlbums.value = HomeRepository.getAllAlbums(ids , apiKey , apiHost)
        }

    }
}