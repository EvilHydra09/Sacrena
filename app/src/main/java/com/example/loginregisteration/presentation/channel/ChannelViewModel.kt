package com.example.loginregisteration.presentation.channel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Channel


class ChannelViewModel : ViewModel(){


    val client = ChatClient.instance()

    fun deleteChannel(channel: Channel,context: Context){
        client.deleteChannel(channelId = channel.id, channelType = "messaging").enqueue { result ->
            if (result.isSuccess) {
                Toast.makeText(context, "Deleted Channel", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("ChannelViewModel", "deleteChannel: Failed to delete channel ")
            }
        }
    }



}


