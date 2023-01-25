package com.example.konmusicplayer.ui.songs

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.konmusicplayer.MainActivity
import com.example.konmusicplayer.R
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.databinding.FragmentSongsBinding
import kotlinx.coroutines.launch
import java.util.*


class SongsFragment : Fragment(R.layout.fragment_songs), SongsAdapter.OnItemClickListener {

    private val mSongsViewModel: SongsViewModel by viewModels()
    private var pos = MainActivity.currentSongPos
    private lateinit var audioList: MutableMap<String, Song>
    private lateinit var songsList: List<Song>
    private lateinit var binding: FragmentSongsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) }
            == PackageManager.PERMISSION_GRANTED) {
            Log.i("KITTY", "CAT CAT CAT")
            audioList = findAllAudio()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) }
            == PackageManager.PERMISSION_GRANTED) {
            fillDatabase(audioList)
        }
        pos = MainActivity.currentSongPos
        return inflater.inflate(R.layout.fragment_songs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSongsBinding.bind(view)
        val songsAdapter = SongsAdapter(this)

        mSongsViewModel.getAllSongs.observe(viewLifecycleOwner) {
            songsList = it
            songsAdapter.submitList(it)
        }

        binding.apply {
            recyclerViewSongs.apply {
                adapter = songsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
    }

    private fun fillDatabase(audioList: MutableMap<String, Song>) {
        val searchedPaths = audioList.keys
        var dbPaths: List<String>
        mSongsViewModel.getPaths.observe(viewLifecycleOwner) { paths ->
            dbPaths = paths
            lifecycleScope.launch {
                for(path in searchedPaths){
                    if(path !in dbPaths){
                        audioList[path]?.let { mSongsViewModel.onMissingSong(it) }
                    }
                }
            }
            for(path in dbPaths){
                if(path !in searchedPaths){
                    mSongsViewModel.onRedundantSong(path)
                }
            }
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
        val cursor = context?.contentResolver?.query(uri, projection, selection, null, sortOrder)

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

    private fun playlistsOnClicked() {
        findNavController().navigate(R.id.playlistsFragment)
    }

    private fun favoritesOnClicked() {
        findNavController().navigate(R.id.favoritesFragment)
    }

    private fun shuffleOnClicked() {
        if(songsList.isNotEmpty()) {
            findNavController().navigate(R.id.playerFragment)
            MainActivity.songsList = songsList.shuffled()
            MainActivity.apply {
                currentSongPos.value = 0
                mediaPlayer.apply {
                    reset()
                    setDataSource(songsList[0].path)
                    prepare()
                    start()
                }
            }
        }
    }

    override fun onItemClick(song: Song, pos: Int) {
        if(songsList.isNotEmpty()) {
            findNavController().navigate(R.id.playerFragment)
            MainActivity.songsList = songsList
            MainActivity.apply {
                currentSongPos.value = pos
                mediaPlayer.apply {
                    reset()
                    setDataSource(song.path)
                    prepare()
                    start()
                }
            }
        }
    }

    override fun onCheckBoxClick(song: Song, isFavorite: Boolean) {
        mSongsViewModel.onSongFavoriteChanged(song, isFavorite)
    }

}