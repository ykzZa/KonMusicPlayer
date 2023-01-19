package com.example.konmusicplayer.data.songs

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(song: Song)

    @Update
    suspend fun update(song: Song)

    @Query("DELETE FROM song_table WHERE path = :pathDel")
    suspend fun deleteSongByPath(pathDel: String)

    @Query("SELECT * FROM song_table")
    fun getSongs(): LiveData<List<Song>>

    @Query("SELECT path FROM song_table")
    fun getSongPaths(): LiveData<List<String>>

    @Query("SELECT * FROM song_table WHERE favorite = 1")
    fun getFavoriteSongs(): LiveData<List<Song>>
}