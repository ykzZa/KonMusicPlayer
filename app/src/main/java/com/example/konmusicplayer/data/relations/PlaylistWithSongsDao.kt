package com.example.konmusicplayer.data.relations

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.konmusicplayer.data.songs.Song

@Dao
interface PlaylistWithSongsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistSongCrossRef(playlistSongCrossRef: PlaylistSongCrossRef)

    @Delete
    suspend fun deletePlaylistSongCrossRef(playlistSongCrossRef: PlaylistSongCrossRef)

    @Transaction
    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    fun getSongsOfPlaylist(playlistId: Int): LiveData<List<PlaylistWithSongs>>

    @Query("SELECT song_table.name, song_table.path, song_table.favorite, song_table.songId FROM song_table LEFT JOIN playlist_songs_table " +
            "ON song_table.songId = playlist_songs_table.songId " +
            "AND playlist_songs_table.playlistId = :playlistId" +
            " WHERE playlist_songs_table.songId IS NULL" )
    fun getSongsNotInPlaylist(playlistId: Int): LiveData<List<Song>>
}