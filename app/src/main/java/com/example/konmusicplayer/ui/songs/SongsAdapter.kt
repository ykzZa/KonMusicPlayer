package com.example.konmusicplayer.ui.songs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.konmusicplayer.MainActivity
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.databinding.ItemSongBinding

class SongsAdapter(private val listener: OnItemClickListener): ListAdapter<Song, SongsAdapter.SongsViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class SongsViewHolder(private val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                relativeLayoutSong.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val song = getItem(position)
                        listener.onItemClick(song, position)
                    }
                }
                checkBoxFavorite.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, checkBoxFavorite.isChecked)
                    }
                }

            }
        }

        fun bind(song: Song) {
            binding.apply {
                checkBoxFavorite.isChecked = song.favorite
                textViewName.text = song.name
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(song: Song, pos: Int)
        fun onCheckBoxClick(song: Song, isFavorite: Boolean)
    }

    class DiffCallback : DiffUtil.ItemCallback<Song>() {

        override fun areItemsTheSame(oldItem: Song, newItem: Song) =
            oldItem.songId == newItem.songId

        override fun areContentsTheSame(oldItem: Song, newItem: Song) = oldItem == newItem
    }
}