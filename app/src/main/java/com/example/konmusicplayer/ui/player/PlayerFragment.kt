package com.example.konmusicplayer.ui.player

import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.konmusicplayer.MainActivity
import com.example.konmusicplayer.R
import com.example.konmusicplayer.databinding.FragmentPlayerBinding
import java.text.SimpleDateFormat
import java.util.*


class PlayerFragment : Fragment(R.layout.fragment_player) {

    private lateinit var binding: FragmentPlayerBinding

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.cardViewPlaying.visibility = View.VISIBLE
        MainActivity.bottomNav.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        MainActivity.cardViewPlaying.visibility = View.GONE
        MainActivity.bottomNav.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlayerBinding.bind(view)

        onPlayingEnd()
        viewUpdate()

        binding.apply {
            songImageView.apply { extractAlbumArt(this) }
            cardViewNext.setOnClickListener { onNextClick() }
            cardViewPlayPause.setOnClickListener { onPlayPauseClick() }
            cardViewPrev.setOnClickListener { onPrevClick() }
            cardViewRepeat.setOnClickListener { onRepeatClick() }
            cardViewQueue.setOnClickListener { onQueueClick() }
            buttonMode.apply {
                when (MainActivity.playingMode) {
                    0 -> setImageResource(R.drawable.ic_baseline_repeat_24)
                    1 -> setImageResource(R.drawable.ic_baseline_repeat_on_24)
                    2 -> setImageResource(R.drawable.ic_baseline_repeat_one_24)
                }
            }
            songSeekbar.apply {
                MainActivity.apply {
                    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar?,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                            if (fromUser) mediaPlayer.seekTo(progress)
                            textViewCurrentTime.text =
                                SimpleDateFormat("mm:ss", Locale("ua", "UA")).format(
                                    Date(mediaPlayer.currentPosition.toLong())
                                )
                        }
                        override fun onStartTrackingTouch(p0: SeekBar?) {}
                        override fun onStopTrackingTouch(p0: SeekBar?) {}
                    })
                }
            }
            MainActivity.apply {
                currentSongPos.observe(viewLifecycleOwner) {
                    textViewSongName.apply {
                        text = songsList[it].name
                        textViewSongName.isSelected = true
                    }
                    songImageView.apply {
                        val sArtworkUri = Uri.parse("content://media/external/audio/albumart")
                        val uri = ContentUris.withAppendedId(
                            sArtworkUri,
                            songsList[it].albumId.toLong()
                        )
                        Glide.with(context)
                            .load(uri)
                            .placeholder(R.drawable.ic_splash_screen)
                            .error(R.drawable.ic_splash_screen)
                            .centerCrop()
                            .into(this)
                    }
                }
                val handler = Handler()
                handler.postDelayed(object: Runnable{
                    override fun run() {
                        try {
                            songSeekbar.progress = mediaPlayer.currentPosition
                            textViewCurrentTime.text = SimpleDateFormat("mm:ss", Locale("ua", "UA")).format(
                                Date(mediaPlayer.currentPosition.toLong())
                            )
                            handler.postDelayed(this, 10)
                        } catch (e: Exception) {
                            songSeekbar.progress = 0
                        }
                    }

                }, 0)
            }

        }
    }

    private fun onQueueClick() {
        findNavController().navigate(R.id.queueFragment)
    }

    private fun onRepeatClick() {
        when (MainActivity.playingMode) {
            0 -> {
                MainActivity.playingMode = 1
                binding.buttonMode.setImageResource(R.drawable.ic_baseline_repeat_on_24)
            }
            1 -> {
                MainActivity.playingMode = 2
                MainActivity.mediaPlayer.isLooping = true
                binding.buttonMode.setImageResource(R.drawable.ic_baseline_repeat_one_24)
            }
            2 -> {
                MainActivity.playingMode = 0
                MainActivity.mediaPlayer.isLooping = false
                binding.buttonMode.setImageResource(R.drawable.ic_baseline_repeat_24)
            }
        }
    }

    private fun onNextClick() {
        when (MainActivity.playingMode) {
            0 -> {
                MainActivity.apply {
                    mediaPlayer.apply {
                        if ((currentSongPos.value ?: 0) + 1 == songsList.size) {
                            mediaPlayerPrepareStart()
                            pause()
                            viewUpdate()
                        } else {
                            currentSongPos.value = currentSongPos.value?.plus(1)
                            mediaPlayerPrepareStart()
                            viewUpdate()
                        }
                    }
                }
            }
            1 -> {
                MainActivity.apply {
                    mediaPlayer.apply {
                        if ((currentSongPos.value ?: 0) + 1 == songsList.size) {
                            currentSongPos.value = 0
                            mediaPlayerPrepareStart()
                            viewUpdate()
                        } else {
                            currentSongPos.value = currentSongPos.value?.plus(1)
                            mediaPlayerPrepareStart()
                            viewUpdate()
                        }
                    }
                }
            }
            2 -> {
                MainActivity.apply {
                    mediaPlayer.apply {
                        if ((currentSongPos.value ?: 0) + 1 == songsList.size) {
                            mediaPlayerPrepareStart()
                            mediaPlayer.isLooping = true
                            binding.buttonPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_24)
                            pause()
                            viewUpdate()
                        } else {
                            currentSongPos.value = currentSongPos.value?.plus(1)
                            mediaPlayerPrepareStart()
                            mediaPlayer.isLooping = true
                            viewUpdate()
                        }
                    }
                }
            }
        }
    }

    private fun onPrevClick() {
        when (MainActivity.playingMode) {
            0 -> {
                MainActivity.apply {
                    mediaPlayer.apply {
                        if (currentSongPos.value == 0) {
                            mediaPlayer.seekTo(0)
                            start()
                            viewUpdate()
                        } else {
                            currentSongPos.value = currentSongPos.value?.minus(1)
                            reset()
                            mediaPlayerPrepareStart()
                            viewUpdate()
                        }
                    }
                }
            }
            1 -> {
                MainActivity.apply {
                    mediaPlayer.apply {
                        if (currentSongPos.value == 0) {
                            currentSongPos.value = songsList.size - 1
                            mediaPlayerPrepareStart()
                            viewUpdate()
                        } else {
                            currentSongPos.value = currentSongPos.value?.minus(1)
                            mediaPlayerPrepareStart()
                            viewUpdate()
                        }
                    }
                }
            }
            2 -> {
                MainActivity.apply {
                    mediaPlayer.apply {
                        if (currentSongPos.value == 0) {
                            mediaPlayerPrepareStart()
                            mediaPlayer.isLooping = true
                            binding.buttonPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_24)
                            pause()
                            start()
                            viewUpdate()
                        } else {
                            currentSongPos.value = currentSongPos.value?.minus(1)
                            mediaPlayerPrepareStart()
                            mediaPlayer.isLooping = true
                            viewUpdate()
                        }
                    }
                }
            }
        }
    }

    private fun onPlayPauseClick() {
        MainActivity.mediaPlayer.apply {
            if (isPlaying) {
                pause()
                binding.buttonPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_24)
            } else {
                start()
                binding.buttonPlayPause.setImageResource(R.drawable.ic_baseline_pause_circle_24)
            }
        }
    }

    private fun onPlayingEnd() {
        MainActivity.apply {
            mediaPlayer.apply {
                setOnCompletionListener {
                    when (playingMode) {
                        0 -> {
                            if ((currentSongPos.value ?: 0) + 1 == songsList.size) {
                                binding.buttonPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_24)
                                pause()
                                viewUpdate()
                            } else {
                                currentSongPos.value = currentSongPos.value?.plus(1)
                                mediaPlayerPrepareStart()
                                viewUpdate()
                            }
                        }
                        1 -> {
                            mediaPlayer.apply {
                                if ((currentSongPos.value ?: 0) + 1 == songsList.size) {
                                    currentSongPos.value = 0
                                    mediaPlayerPrepareStart()
                                    viewUpdate()
                                } else {
                                    currentSongPos.value = currentSongPos.value?.plus(1)
                                    mediaPlayerPrepareStart()
                                    viewUpdate()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun viewUpdate() {
        binding.apply {
            buttonPlayPause.apply {
                if (MainActivity.mediaPlayer.isPlaying) {
                    setImageResource(R.drawable.ic_baseline_pause_circle_24)
                } else {
                    setImageResource(R.drawable.ic_baseline_play_circle_24)
                }
            }
            textViewDuration.text = SimpleDateFormat(
                "mm:ss",
                Locale("ua", "UA")
            ).format(Date(MainActivity.mediaPlayer.duration.toLong()))
            songSeekbar.apply {
                max = MainActivity.mediaPlayer.duration
                progress = 0
            }
        }
    }

    private fun extractAlbumArt(imgView: ImageView) {
        val sArtworkUri = Uri.parse("content://media/external/audio/albumart")
        val uri = ContentUris.withAppendedId(
            sArtworkUri,
            MainActivity.songsList[MainActivity.currentSongPos.value!!].albumId.toLong()
        )
        context?.let {
            Glide.with(it)
                .load(uri)
                .placeholder(R.drawable.ic_splash_screen)
                .error(R.drawable.ic_splash_screen)
                .centerCrop()
                .into(imgView)
        }
    }

    private fun mediaPlayerPrepareStart() {
        MainActivity.mediaPlayer.apply {
            reset()
            Log.i("CATACAK", "media player edited")
            setDataSource(MainActivity.songsList[MainActivity.currentSongPos.value!!].path)
            prepare()
            start()
        }
    }
}