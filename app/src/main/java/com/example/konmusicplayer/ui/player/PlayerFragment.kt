package com.example.konmusicplayer.ui.player

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bullhead.equalizer.DialogEqualizerFragment
import com.example.konmusicplayer.MainActivity
import com.example.konmusicplayer.R
import com.example.konmusicplayer.databinding.FragmentPlayerBinding
import com.example.konmusicplayer.utils.recreateFragment
import java.text.SimpleDateFormat
import java.util.*


class PlayerFragment : Fragment(R.layout.fragment_player) {

    private lateinit var binding: FragmentPlayerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlayerBinding.bind(view)

        onPlayingEnd()

        binding.apply {
            buttonPlayPause.apply {
                if(MainActivity.mediaPlayer.isPlaying) {
                    setImageResource(R.drawable.ic_baseline_pause_circle_24)
                } else {
                    setImageResource(R.drawable.ic_baseline_play_circle_24)
                }
            }
            cardViewNext.setOnClickListener {
                onNextClick()
            }
            cardViewPlayPause.setOnClickListener {
                onPlayPauseClick()
            }
            cardViewPrev.setOnClickListener {
                onPrevClick()
            }
            cardViewRepeat.setOnClickListener {
                onRepeatClick()
            }
            cardViewEqualizer.setOnClickListener {
                onEqualizerClick()
            }
            cardViewQueue.setOnClickListener {
                onQueueClick()
            }
            buttonMode.apply {
                when(MainActivity.playingMode){
                    0 -> setImageResource(R.drawable.ic_baseline_repeat_24)
                    1 -> setImageResource(R.drawable.ic_baseline_repeat_on_24)
                    2 -> setImageResource(R.drawable.ic_baseline_repeat_one_24)
                }
            }
            songSeekbar.apply {
                MainActivity.apply {
                    max = mediaPlayer.duration
                    val handler = Handler()
                    handler.postDelayed(object: Runnable{
                        override fun run() {
                            try {
                                songSeekbar.progress = mediaPlayer.currentPosition
                                textViewCurrentTime.text = SimpleDateFormat("mm:ss").format(Date(mediaPlayer.currentPosition.toLong()))
                                handler.postDelayed(this, 10)
                            } catch (e: Exception) {
                                songSeekbar.progress = 0
                            }
                        }

                    }, 0)
                    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar?,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                            if (fromUser) mediaPlayer.seekTo(progress)
                            textViewCurrentTime.text = SimpleDateFormat("mm:ss").format(Date(mediaPlayer.currentPosition.toLong()))
                        }

                        override fun onStartTrackingTouch(p0: SeekBar?) {
                        }

                        override fun onStopTrackingTouch(p0: SeekBar?) {

                        }

                    })
                }
            }
            MainActivity.apply {
                textViewSongName.text = songsList[currentSongPos].name
                textViewDuration.text = SimpleDateFormat("mm:ss").format(Date(mediaPlayer.duration.toLong()))
            }
            textViewSongName.isSelected = true
        }
    }

    private fun onQueueClick() {
        findNavController().navigate(R.id.queueFragment)
    }

    private fun onEqualizerClick() {
        val fragment = DialogEqualizerFragment.newBuilder()
            .setAudioSessionId(MainActivity.mediaPlayer.audioSessionId)
            .themeColor(ContextCompat.getColor(requireContext(), R.color.white))
            .textColor(ContextCompat.getColor(requireContext(), R.color.black))
            .accentAlpha(ContextCompat.getColor(requireContext(), R.color.blue_icon))
            .darkColor(ContextCompat.getColor(requireContext(), R.color.blue_icon))
            .setAccentColor(ContextCompat.getColor(requireContext(), R.color.light_blue_icon))
            .build()
        fragmentManager?.let { fragment.show(it, "eq") }
    }

    private fun onRepeatClick() {
        when(MainActivity.playingMode) {
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
        when(MainActivity.playingMode) {
            0 -> {
                MainActivity.apply {
                    mediaPlayer.apply {
                        reset()
                        if(currentSongPos + 1 == songsList.size) {
                            mediaPlayerPrepareStart()
                            pause()
                        } else {
                            currentSongPos += 1
                            mediaPlayerPrepareStart()
                        }
                    }
                }
            }
            1 -> {
                MainActivity.apply {
                    mediaPlayer.apply {
                        reset()
                        if(currentSongPos + 1 == songsList.size) {
                            currentSongPos = 0
                            mediaPlayerPrepareStart()
                        } else {
                            currentSongPos += 1
                            mediaPlayerPrepareStart()
                        }
                    }
                }
            }
            2 -> {
                MainActivity.apply {
                    mediaPlayer.apply {
                        reset()
                        if(currentSongPos + 1 == songsList.size) {
                            mediaPlayerPrepareStart()
                            mediaPlayer.isLooping = true
                            binding.buttonPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_24)
                            pause()
                        } else {
                            currentSongPos += 1
                            mediaPlayerPrepareStart()
                            mediaPlayer.isLooping = true
                        }
                    }
                }
            }
        }
        recreateFragment()
    }

    private fun onPrevClick() {
        when(MainActivity.playingMode) {
            0 -> {
                MainActivity.apply {
                    mediaPlayer.apply {
                        if(currentSongPos == 0) {
                            mediaPlayer.seekTo(0)
                            start()
                        } else {
                            currentSongPos -= 1
                            reset()
                            mediaPlayerPrepareStart()
                        }
                    }
                }
            }
            1 -> {
                MainActivity.apply {
                    mediaPlayer.apply {
                        reset()
                        if(currentSongPos == 0) {
                            currentSongPos = songsList.size - 1
                            mediaPlayerPrepareStart()
                        } else {
                            currentSongPos -= 1
                            mediaPlayerPrepareStart()
                        }
                    }
                }
            }
            2 -> {
                MainActivity.apply {
                    mediaPlayer.apply {
                        reset()
                        if(currentSongPos == 0) {
                            mediaPlayerPrepareStart()
                            mediaPlayer.isLooping = true
                            binding.buttonPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_24)
                            pause()
                            start()
                        } else {
                            currentSongPos -= 1
                            mediaPlayerPrepareStart()
                            mediaPlayer.isLooping = true
                        }
                    }
                }
            }
        }
        recreateFragment()
    }

    private fun onPlayPauseClick() {
        MainActivity.mediaPlayer.apply {
            if(isPlaying){
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
                            if (currentSongPos + 1 == songsList.size) {
                                binding.buttonPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_24)
                                pause()
                            } else {
                                reset()
                                currentSongPos += 1
                                mediaPlayerPrepareStart()
                                try {
                                    recreateFragment()
                                } catch (_: IllegalStateException) {
                                    Log.i("FRG2", "sd")
                                }
                            }
                        }
                        1 ->  {
                            mediaPlayer.apply {
                                reset()
                                if (currentSongPos + 1 == songsList.size) {
                                    currentSongPos = 0
                                    mediaPlayerPrepareStart()
                                } else {
                                    currentSongPos += 1
                                    mediaPlayerPrepareStart()
                                }
                                try {
                                    recreateFragment()
                                } catch (_: IllegalStateException) {
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    private fun mediaPlayerPrepareStart() {
        MainActivity.mediaPlayer.apply {
            setDataSource(MainActivity.songsList[MainActivity.currentSongPos].path)
            prepare()
            start()
        }
    }
}