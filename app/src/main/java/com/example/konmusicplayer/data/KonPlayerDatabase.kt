package com.example.konmusicplayer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.konmusicplayer.data.playlists.Playlist
import com.example.konmusicplayer.data.playlists.PlaylistDao
import com.example.konmusicplayer.data.relations.PlaylistSongCrossRef
import com.example.konmusicplayer.data.relations.PlaylistWithSongsDao
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.data.songs.SongDao

@Database(
    entities = [
        Song::class,
        Playlist::class,
        PlaylistSongCrossRef::class
               ],
    version = 1,
    exportSchema = false
)
abstract class KonPlayerDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun PlaylistWithSongsDao(): PlaylistWithSongsDao

    companion object {
        @Volatile
        private var INSTANCE: KonPlayerDatabase? = null

        fun getDatabase(context: Context): KonPlayerDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KonPlayerDatabase::class.java,
                    "player_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}