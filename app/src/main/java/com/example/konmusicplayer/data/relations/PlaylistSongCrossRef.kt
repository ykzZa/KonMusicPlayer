package com.example.konmusicplayer.data.relations

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "songId"], tableName = "playlist_songs_table")
data class PlaylistSongCrossRef(
    val playlistId: Int,
    val songId: Int
)

