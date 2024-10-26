package com.example.chatapplication.presentation.channel.components

import androidx.compose.runtime.Composable
import com.example.chatapplication.R

@Composable
fun ElementIcon(elementType: String): Int {
    return when (elementType.lowercase()) {
        "fire" -> R.drawable.ic_firesimple
        "earth" -> R.drawable.ic_globehemispherewest
        "air" -> R.drawable.ic_wind
        "water" -> R.drawable.ic_drop
        else -> R.drawable.ic_firesimple // Default icon if the element is not recognized
    }
}