package com.example.konmusicplayer.data.playlists

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.konmusicplayer.data.relations.PlaylistSongCrossRef

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(playlist: Playlist)

    @Update
    suspend fun update(playlist: Playlist)

    @Delete
    suspend fun delete(playlist: Playlist)

    @Query("SELECT * FROM playlist_table WHERE playlistId = :id")
    fun getPlaylistById(id: Int): LiveData<Playlist>

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): LiveData<List<Playlist>>

    @Insert
    suspend fun insertPlaylistWithSongs(playlistSongCrossRef: PlaylistSongCrossRef)
}