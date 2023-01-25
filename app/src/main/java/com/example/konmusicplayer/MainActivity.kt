package com.example.konmusicplayer

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.databinding.ActivityMainBinding
import com.example.konmusicplayer.ui.favorites.FavoritesFragment
import com.example.konmusicplayer.ui.playlists.PlaylistsFragment
import com.example.konmusicplayer.ui.songs.SongsFragment
import com.example.konmusicplayer.ui.songs.SongsViewModel

class MainActivity : AppCompatActivity() {

    private val mSongsViewModel: SongsViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    companion object {
        val mediaPlayer = MediaPlayer()
        var currentSongPos = MutableLiveData(0)
        var songsList = listOf<Song>()
        var playingMode = 0 // 0 - stop when list end, 1 - list loop, 2 - song loop
        @SuppressLint("StaticFieldLeak")
        lateinit var cardViewPlaying: View
        @SuppressLint("StaticFieldLeak")
        lateinit var bottomNav: View
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)

        requestRuntimePermission()

        cardViewPlaying = binding.cardViewPlaying
        bottomNav = binding.bottomNavigation

        binding.apply {
            cardViewPlaying.setOnClickListener {
                navController.navigate(R.id.playerFragment)
            }
            bottomNavigation.setupWithNavController(navController)
        }
        currentSongPos.observe(this) {
            if(songsList.isNotEmpty()) {
                binding.textViewCurrentSong.text = songsList[currentSongPos.value!!].name
            }
        }
    }

    private fun requestRuntimePermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 113)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 113){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                fillDatabase(findAllAudio())
            }
        }
    }

    private fun fillDatabase(audioList: MutableMap<String, Song>) {
        val searchedPaths = audioList.keys
        for(path in searchedPaths){
            audioList[path]?.let { mSongsViewModel.onMissingSong(it) }
        }
    }

    @SuppressLint("Range")
    private fun findAllAudio(): MutableMap<String, Song> {
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        val cursor = applicationContext?.contentResolver?.query(uri, projection, selection, null, sortOrder)

        val audioList = mutableMapOf<String, Song>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                val audio = Song(name, path, albumId)
                audioList[path] = audio
            }
            cursor.close()
        }
        return audioList
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}