package com.example.konmusicplayer.data.playlists

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class Playlist(
    val name: String,
    val description: String,
    @PrimaryKey(autoGenerate = true) val playlistId: Int = 0
)

