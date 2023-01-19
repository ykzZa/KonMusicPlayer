package com.example.konmusicplayer.data.songs

import androidx.lifecycle.LiveData

class SongRepository(
    private val songDao: SongDao
) {
    val getSongs: LiveData<List<Song>> = songDao.getSongs()
    val getSongPaths: LiveData<List<String>> = songDao.getSongPaths()
    val getFavoriteSongs: LiveData<List<Song>> = songDao.getFavoriteSongs()

    suspend fun addSong(song: Song){
        songDao.insert(song)
    }

    suspend fun deleteSongByPath(path: String){
        songDao.deleteSongByPath(path)
    }

    suspend fun updateSong(song: Song){
        songDao.update(song)
    }
}