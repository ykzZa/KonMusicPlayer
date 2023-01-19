package com.example.konmusicplayer.ui.selection

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.konmusicplayer.data.songs.Song
import com.example.konmusicplayer.databinding.ItemFavoriteBinding

class SelectionAdapter(private val listener: OnItemClickListener): ListAdapter<Song, SelectionAdapter.SelectionViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectionViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectionViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class SelectionViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                cardViewSong.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val song = getItem(position)
                        if(!checkBoxSelected.isChecked) {
                            Log.i("CHECKBOX", "${checkBoxSelected.isChecked}")
                            checkBoxSelected.isChecked = true
                            cardViewSong.setCardBackgroundColor(Color.parseColor("#C6E6FF"))
                            listener.onItemClick(song, 1)
                            Log.i("CHECKBOX", "${checkBoxSelected.isChecked}")
                        } else {
                            Log.i("CHECKBOX1", "${checkBoxSelected.isChecked}")
                            cardViewSong.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
                            checkBoxSelected.isChecked = false
                            listener.onItemClick(song, 0)
                            Log.i("CHECKBOX1", "${checkBoxSelected.isChecked}")
                        }
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
        fun onItemClick(song: Song, log: Int)
    }

    class DiffCallback : DiffUtil.ItemCallback<Song>() {

        override fun areItemsTheSame(oldItem: Song, newItem: Song) =
            oldItem.songId == newItem.songId

        override fun areContentsTheSame(oldItem: Song, newItem: Song) = oldItem == newItem
    }
}