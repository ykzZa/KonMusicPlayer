package com.example.konmusicplayer.ui.favorites

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.konmusicplayer.MainActivity
import com.example.konmusicplayer.R
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.databinding.FragmentFavoritesBinding


class FavoritesFragment : Fragment(R.layout.fragment_favorites),
    FavoritesAdapter.OnItemClickListener {

    private val mFavoritesViewModel: FavoritesViewModel by viewModels()
    private lateinit var songsList: List<Song>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentFavoritesBinding.bind(view)
        val favoritesAdapter = FavoritesAdapter(this)

        binding.apply {
            recyclerViewFavorites.apply {
                adapter = favoritesAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val song: Song = favoritesAdapter.currentList[viewHolder.adapterPosition]
                    mFavoritesViewModel.onFavoriteSongSwiped(song)
                }
            }).attachToRecyclerView(recyclerViewFavorites)

            fabShuffle.setOnClickListener {
                shuffleOnClicked()
            }
        }

        mFavoritesViewModel.getFavorites.observe(viewLifecycleOwner) {
            songsList = it
            favoritesAdapter.submitList(it)
        }
    }

    private fun shuffleOnClicked(){
        if(songsList.isNotEmpty()) {
            findNavController().navigate(R.id.playerFragment)
            MainActivity.songsList = songsList.shuffled()
            MainActivity.apply {
                currentSongPos = 0
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
        findNavController().navigate(R.id.playerFragment)
        MainActivity.songsList = songsList
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