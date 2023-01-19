package com.example.konmusicplayer.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.data.KonPlayerDatabase
import com.example.konmusicplayer.data.songs.SongRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application): AndroidViewModel(application) {

    val getFavorites: LiveData<List<Song>>
    private val repository: SongRepository

    init {
        val songDao = KonPlayerDatabase.getDatabase(application).songDao()
        repository = SongRepository(songDao)
        getFavorites = repository.getFavoriteSongs
    }

    fun onFavoriteSongSwiped(song: Song) {
        viewModelScope.launch {
            repository.updateSong(song.copy(favorite = false))
        }
    }
}
