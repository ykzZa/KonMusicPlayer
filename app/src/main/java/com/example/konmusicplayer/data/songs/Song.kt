package com.example.konmusicplayer.data.songs

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song_table")
data class Song(
    val name: String,
    val path: String,
    val albumId: String,
    var favorite: Boolean = false,
    @PrimaryKey(autoGenerate = true) val songId: Int = 0
)



