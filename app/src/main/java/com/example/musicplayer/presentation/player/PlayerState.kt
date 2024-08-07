package com.example.musicplayer.presentation.player

import com.example.musicplayer.data.local.entity.Audio

data class PlayerState(
    val playingSong: Audio = Audio(),
    val isPlaying: Boolean = false,
    val currentProgress: Long = 0
)
