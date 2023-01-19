package com.example.konmusicplayer.ui.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.konmusicplayer.data.playlists.Playlist
import com.example.konmusicplayer.databinding.ItemPlaylistBinding


class PlaylistsAdapter(private val listener: OnItemClickListener): ListAdapter<Playlist, PlaylistsAdapter.PlaylistsViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class PlaylistsViewHolder(private val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                cardViewPlaylist.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val playlist = getItem(position)
                        listener.onItemClick(playlist)
                    }
                }
                imageViewDelete.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val playlist = getItem(position)
                        listener.onImageViewDeleteClick(playlist)
                    }
                }
            }
        }

        fun bind(playlist: Playlist) {
            binding.apply {
                textViewPlaylistName.text = playlist.name
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(playlist: Playlist)
        fun onImageViewDeleteClick(playlist: Playlist)
    }

    class DiffCallback : DiffUtil.ItemCallback<Playlist>() {

        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist) =
            oldItem.playlistId == newItem.playlistId

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist) = oldItem == newItem
    }
}