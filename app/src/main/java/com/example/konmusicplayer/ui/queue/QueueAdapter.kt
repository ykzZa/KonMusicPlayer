package com.example.konmusicplayer.ui.queue

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.konmusicplayer.MainActivity
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.databinding.ItemFavoriteBinding

class QueueAdapter(private val listener: OnItemClickListener): ListAdapter<Song, QueueAdapter.QueueViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueueViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QueueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QueueViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class QueueViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                cardViewSong.setOnClickListener {
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