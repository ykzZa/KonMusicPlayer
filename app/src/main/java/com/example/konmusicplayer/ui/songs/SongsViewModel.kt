package com.example.konmusicplayer.ui.songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.data.KonPlayerDatabase
import com.example.konmusicplayer.data.songs.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SongsViewModel(application: Application): AndroidViewModel(application) {

    val getAllSongs: LiveData<List<Song>>
    val getPaths: LiveData<List<String>>
    private val repository: SongRepository

    init {
        val songDao = KonPlayerDatabase.getDatabase(application).songDao()
        repository = SongRepository(songDao)
        getAllSongs = repository.getSongs
        getPaths = repository.getSongPaths
    }

    fun onMissingSong(song: Song){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSong(song)
        }
    }

    fun onRedundantSong(path: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSongByPath(path)
        }
    }

    fun onSongFavoriteChanged(song: Song, isFavorite: Boolean){
        viewModelScope.launch {
            repository.updateSong(song.copy(favorite = isFavorite))
        }
    }
}