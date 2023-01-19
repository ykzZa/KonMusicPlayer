package com.example.konmusicplayer.ui.playlistopened

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.konmusicplayer.data.KonPlayerDatabase
import com.example.konmusicplayer.data.playlists.Playlist
import com.example.konmusicplayer.data.playlists.PlaylistRepository
import com.example.konmusicplayer.data.relations.PlaylistWithSongs
import com.example.konmusicplayer.data.relations.PlaylistWithSongsRepository
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.data.songs.SongRepository
import kotlinx.coroutines.launch

class PlaylistOpenedViewModel(application: Application): AndroidViewModel(application) {

    private val repository: PlaylistWithSongsRepository
    private val playlistsRepository: PlaylistRepository
    private val songsRepository: SongRepository

    init {
        val playlistWithSongsDao = KonPlayerDatabase.getDatabase(application).PlaylistWithSongsDao()
        val playlistDao = KonPlayerDatabase.getDatabase(application).playlistDao()
        val songsDao = KonPlayerDatabase.getDatabase(application).songDao()
        repository = PlaylistWithSongsRepository(playlistWithSongsDao)
        playlistsRepository = PlaylistRepository(playlistDao)
        songsRepository = SongRepository(songsDao)
    }

    fun onPlaylistOpen(playlistId: Int): LiveData<List<PlaylistWithSongs>> = repository.getPlaylistWithSongs(playlistId)

    fun onUpdatePlaylistTextView(id: Int): LiveData<Playlist> = playlistsRepository.getPlaylistById(id)

    fun onSongFavoriteChanged(song: Song, isFavorite: Boolean){
        viewModelScope.launch {
            songsRepository.updateSong(song.copy(favorite = isFavorite))
        }
    }
}