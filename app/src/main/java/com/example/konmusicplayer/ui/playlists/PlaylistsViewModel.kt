package com.example.konmusicplayer.ui.playlists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.konmusicplayer.data.KonPlayerDatabase
import com.example.konmusicplayer.data.playlists.Playlist
import com.example.konmusicplayer.data.playlists.PlaylistRepository
import kotlinx.coroutines.launch

class PlaylistsViewModel(application: Application): AndroidViewModel(application) {

    val getAllPlaylists: LiveData<List<Playlist>>
    private val repository: PlaylistRepository

    init {
        val playlistDao = KonPlayerDatabase.getDatabase(application).playlistDao()
        repository = PlaylistRepository(playlistDao)
        getAllPlaylists = repository.getPlaylists
    }

    fun onAddNewPlaylistClick(playlist: Playlist) {
        viewModelScope.launch {
            repository.addPlaylist(playlist)
        }
    }

    fun onDeleteClick(playlist: Playlist) {
        viewModelScope.launch {
            repository.deletePlaylist(playlist)
        }
    }

    fun onUpdatePlaylistTextView(id: Int): LiveData<Playlist> = repository.getPlaylistById(id)

}