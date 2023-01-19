package com.example.konmusicplayer.ui.playlistopened

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.konmusicplayer.MainActivity
import com.example.konmusicplayer.R
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.databinding.FragmentPlaylistOpenedBinding
import com.example.konmusicplayer.ui.songs.SongsAdapter
import kotlinx.coroutines.launch


class PlaylistOpenedFragment : Fragment(R.layout.fragment_playlist_opened), SongsAdapter.OnItemClickListener {

    private val mPlaylistOpenedViewModel: PlaylistOpenedViewModel by viewModels()
    private val args: PlaylistOpenedFragmentArgs by navArgs()
    private lateinit var songsList: List<Song>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_playlist_opened, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPlaylistOpenedBinding.bind(view)
        val songsAdapter = SongsAdapter(this)

        binding.apply {
            recyclerViewSongs.apply {
                adapter = songsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            lifecycleScope.launch {
                val value = mPlaylistOpenedViewModel.onUpdatePlaylistTextView(args.playlistId)
                value.observe(viewLifecycleOwner) {
                    textViewPlaylistDescription.text = it.description
                    textViewPlaylistName.text = it.name
                }
            }
            buttonAdd.setOnClickListener {
                val action = PlaylistOpenedFragmentDirections.actionPlaylistOpenedFragmentToSelectionFragment(0, args.playlistId)
                findNavController().navigate(action)
            }
            buttonRemove.setOnClickListener {
                val action = PlaylistOpenedFragmentDirections.actionPlaylistOpenedFragmentToSelectionFragment(1, args.playlistId)
                findNavController().navigate(action)
            }
            fabShuffle.setOnClickListener {
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
            imageViewPlaylist.setOnClickListener {
                if(songsList.isNotEmpty()) {
                    findNavController().navigate(R.id.playerFragment)
                    MainActivity.songsList = songsList
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
        }

        mPlaylistOpenedViewModel.onPlaylistOpen(args.playlistId).observe(viewLifecycleOwner) {
            songsAdapter.submitList(it[0].songs)
            songsList = it[0].songs
            binding.textNumber.text = "${it[0].songs.size}"
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

    override fun onCheckBoxClick(song: Song, isFavorite: Boolean) {
        mPlaylistOpenedViewModel.onSongFavoriteChanged(song, isFavorite)
    }


}