package com.example.konmusicplayer.ui.queue

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.konmusicplayer.MainActivity
import com.example.konmusicplayer.R
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.databinding.FragmentQueueBinding
import com.example.konmusicplayer.ui.favorites.FavoritesAdapter
import com.example.konmusicplayer.ui.selection.SelectionAdapter
import com.example.konmusicplayer.utils.recreateFragment

class QueueFragment : Fragment(R.layout.fragment_queue), QueueAdapter.OnItemClickListener {

    private lateinit var binding: FragmentQueueBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentQueueBinding.bind(view)
        val songsAdapter = QueueAdapter(this)
        songsAdapter.submitList(MainActivity.songsList)
        MainActivity.cardViewPlaying.visibility = View.GONE

        binding.apply {
            recyclerViewSongs.apply {
                adapter = songsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
    }

    override fun onItemClick(song: Song, pos: Int) {
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