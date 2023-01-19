package com.example.konmusicplayer.data.playlists

import androidx.lifecycle.LiveData

class PlaylistRepository(
    private val playlistDao: PlaylistDao
) {
    val getPlaylists: LiveData<List<Playlist>> = playlistDao.getPlaylists()

    suspend fun addPlaylist(playlist: Playlist){
        playlistDao.insert(playlist)
    }

    suspend fun deletePlaylist(playlist: Playlist){
        playlistDao.delete(playlist)
    }

    suspend fun updatePlaylist(playlist: Playlist){
        playlistDao.update(playlist)
    }

    fun getPlaylistById(id: Int) = playlistDao.getPlaylistById(id)

}