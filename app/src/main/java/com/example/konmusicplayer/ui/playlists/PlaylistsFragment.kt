package com.example.konmusicplayer.ui.playlists

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.konmusicplayer.R
import com.example.konmusicplayer.data.playlists.Playlist
import com.example.konmusicplayer.databinding.AddPlaylistBinding
import com.example.konmusicplayer.databinding.FragmentPlaylistsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PlaylistsFragment : Fragment(R.layout.fragment_playlists), PlaylistsAdapter.OnItemClickListener {

    private lateinit var binding: FragmentPlaylistsBinding
    private val mPlaylistsViewModel: PlaylistsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_playlists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlaylistsBinding.bind(view)
        val playlistsAdapter = PlaylistsAdapter(this)

        binding.apply {
            recyclerViewPlaylists.apply {
                adapter = playlistsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            binding.fabAddPlaylist.setOnClickListener {
                addAlertDialog()
            }

        }

        mPlaylistsViewModel.getAllPlaylists.observe(viewLifecycleOwner) {
            playlistsAdapter.submitList(it)
        }
    }

    private fun addAlertDialog(){
        val customDialog = LayoutInflater.from(context).inflate(R.layout.add_playlist, binding.root, false)
        val binder = AddPlaylistBinding.bind(customDialog)
        val builder = context?.let { MaterialAlertDialogBuilder(it) }
        val dialog = builder?.setView(customDialog)
            ?.setNegativeButton("Cancel", null)
            ?.setPositiveButton("Add"){ dialog, _ ->
                val playlistName = binder.textInputPlaylistName.text
                val playlistDescription = binder.textInputPlaylistDescription.text
                if(playlistName != null && playlistDescription != null)
                    if(playlistName.isNotEmpty() && playlistDescription.isNotEmpty()) {
                        mPlaylistsViewModel.onAddNewPlaylistClick(Playlist(playlistName.toString(), playlistDescription.toString()))
                    } else {
                        Toast.makeText(context, "Fields shouldn't be empty!", Toast.LENGTH_SHORT).show()
                    }
                dialog.dismiss()
            }?.create()
        dialog?.show()
    }


    override fun onItemClick(playlist: Playlist) {
        val action = PlaylistsFragmentDirections.actionPlaylistsFragmentToPlaylistOpenedFragment(playlist.playlistId)
        findNavController().navigate(action)
    }

    override fun onImageViewDeleteClick(playlist: Playlist) {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setView(R.layout.delete_playlist)
                setPositiveButton(
                    "Yes"
                ) { _, _ ->
                    mPlaylistsViewModel.onDeleteClick(playlist)
                    Toast.makeText(context, "Playlist was deleted", Toast.LENGTH_SHORT).show()
                }
                setNegativeButton("No",
                    DialogInterface.OnClickListener { _, _ ->
                        // User cancelled the dialog
                    })
            }
            builder.create()
        }?.show()
    }
}