package com.example.konmusicplayer.ui.selection

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.konmusicplayer.R
import com.example.konmusicplayer.data.relations.PlaylistSongCrossRef
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.databinding.FragmentSelectionBinding
import com.example.konmusicplayer.ui.playlistopened.PlaylistOpenedViewModel
import com.example.konmusicplayer.ui.songs.SongsViewModel
import kotlinx.coroutines.launch

class SelectionFragment : Fragment(R.layout.fragment_selection), SelectionAdapter.OnItemClickListener {

    private val songsList = mutableListOf<Song>()
    private val mSongsViewModel: SongsViewModel by viewModels()
    private val mSelectionViewModel: SelectionViewModel by viewModels()
    private lateinit var binding: FragmentSelectionBinding
    private val args: SelectionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSelectionBinding.bind(view)
        val selectionAdapter = SelectionAdapter(this)

        binding.apply {
            recyclerViewSelections.apply {
                adapter = selectionAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            if(args.actionType == 0){
                fabConfirm.setImageResource(R.drawable.ic_add)
                fabConfirm.setOnClickListener {
                    for (song in songsList) {
                        lifecycleScope.launch {
                            mSelectionViewModel.onFabAddClicked(
                                PlaylistSongCrossRef(
                                    args.playlistId,
                                    song.songId
                                )
                            )
                        }
                    }
                    activity?.onBackPressed()
                }
            } else {
                fabConfirm.setImageResource(R.drawable.ic_baseline_remove_24)
                fabConfirm.setOnClickListener {
                    for (song in songsList) {
                        lifecycleScope.launch {
                            mSelectionViewModel.onFabRemoveClicked(
                                PlaylistSongCrossRef(
                                    args.playlistId,
                                    song.songId
                                )
                            )
                        }
                    }
                    activity?.onBackPressed()
                }
            }


        }

        if(args.actionType == 0) {
            mSelectionViewModel.onAddOpen(args.playlistId).observe(viewLifecycleOwner) {
                selectionAdapter.submitList(it)
            }
        } else {
            mSelectionViewModel.onRemoveOpen(args.playlistId).observe(viewLifecycleOwner) {
                selectionAdapter.submitList(it[0].songs)
            }
        }
    }

    override fun onItemClick(song: Song, log: Int) {
        if (log == 1) {
            songsList.add(song)
        } else {
            songsList.remove(song)
        }
    }

}