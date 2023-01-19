package com.example.konmusicplayer.ui.queue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.konmusicplayer.MainActivity
import com.example.konmusicplayer.R
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.databinding.FragmentQueueBinding
import com.example.konmusicplayer.ui.favorites.FavoritesAdapter

class QueueFragment : Fragment(R.layout.fragment_queue), FavoritesAdapter.OnItemClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQueueBinding.bind(view)
        val songsAdapter = FavoritesAdapter(this)
        songsAdapter.submitList(MainActivity.songsList)

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
            currentSongPos = pos
            mediaPlayer.apply {
                reset()
                setDataSource(song.path)
                prepare()
                start()
            }
        }
    }

}