package com.example.konmusicplayer.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.databinding.ItemFavoriteBinding

class FavoritesAdapter(private val listener: OnItemClickListener): ListAdapter<Song, FavoritesAdapter.FavoritesViewHolder>(
    DiffCallback()
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class FavoritesViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                relativeLayoutFavorite.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val song = getItem(position)
                        listener.onItemClick(song, position)
                    }
                }
            }
        }

        fun bind(song: Song) {
            binding.apply {
                textViewSongName.text = song.name
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(song: Song, pos: Int)
    }

    class DiffCallback : DiffUtil.ItemCallback<Song>() {

        override fun areItemsTheSame(oldItem: Song, newItem: Song) =
            oldItem.songId == newItem.songId

        override fun areContentsTheSame(oldItem: Song, newItem: Song) = oldItem == newItem
    }
}