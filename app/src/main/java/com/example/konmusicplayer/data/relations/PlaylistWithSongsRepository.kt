package com.example.konmusicplayer.data.relations

import androidx.lifecycle.LiveData
import com.example.konmusicplayer.data.songs.Song

class PlaylistWithSongsRepository(
    private val playlistWithSongsDao: PlaylistWithSongsDao
) {

    suspend fun addSongToPlaylist(playlistSongCrossRef: PlaylistSongCrossRef){
        playlistWithSongsDao.insertPlaylistSongCrossRef(playlistSongCrossRef)
    }

    suspend fun deleteSongFromPlaylist(playlistSongCrossRef: PlaylistSongCrossRef){
        playlistWithSongsDao.deletePlaylistSongCrossRef(playlistSongCrossRef)
    }

    fun getPlaylistWithSongs(playlistId: Int): LiveData<List<PlaylistWithSongs>> = playlistWithSongsDao.getSongsOfPlaylist(playlistId)

    fun getSongsNotInPlaylist(playlistId: Int): LiveData<List<Song>> = playlistWithSongsDao.getSongsNotInPlaylist(playlistId)

}