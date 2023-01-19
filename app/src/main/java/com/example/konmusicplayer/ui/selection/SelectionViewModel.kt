package com.example.konmusicplayer.ui.selection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.konmusicplayer.data.KonPlayerDatabase
import com.example.konmusicplayer.data.relations.PlaylistSongCrossRef
import com.example.konmusicplayer.data.relations.PlaylistWithSongs
import com.example.konmusicplayer.data.relations.PlaylistWithSongsRepository
import com.example.konmusicplayer.data.songs.Song
import kotlinx.coroutines.launch

class SelectionViewModel(application: Application): AndroidViewModel(application) {

    private val repository: PlaylistWithSongsRepository

    init {
        val playlistWithSongsDao = KonPlayerDatabase.getDatabase(application).PlaylistWithSongsDao()
        repository = PlaylistWithSongsRepository(playlistWithSongsDao)
    }

    suspend fun onFabAddClicked(playlistSongCrossRef: PlaylistSongCrossRef){
        viewModelScope.launch {
            repository.addSongToPlaylist(playlistSongCrossRef)
        }
    }

    suspend fun onFabRemoveClicked(playlistSongCrossRef: PlaylistSongCrossRef){
        viewModelScope.launch {
            repository.deleteSongFromPlaylist(playlistSongCrossRef)
        }
    }

    fun onRemoveOpen(playlistId: Int): LiveData<List<PlaylistWithSongs>> = repository.getPlaylistWithSongs(playlistId)

    fun onAddOpen(playlistId: Int): LiveData<List<Song>> = repository.getSongsNotInPlaylist(playlistId)

}