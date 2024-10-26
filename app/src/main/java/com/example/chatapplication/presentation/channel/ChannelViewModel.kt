package com.example.chatapplication.presentation.channel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.InitializationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val client: ChatClient,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _initializationState =
        MutableStateFlow(client.clientState.initializationState.value)
    val initializationState: StateFlow<InitializationState> = _initializationState

    // Expose the ChatClient
    val chatClient: ChatClient get() = client


    init {
        Log.d("ChannelViewModel", "init: ${initializationState.value}")
        observeClientState()
    }

    private fun observeClientState() {
        viewModelScope.launch {
            client.clientState.initializationState.collect { newState ->
                _initializationState.value = newState
                Log.d("ChannelViewModel", "Updated initializationState: $newState")
            }
        }
    }

    //TODO: Delete Channel
    fun deleteChannel(channel: Channel, context: Context) {
        client.deleteChannel(channelId = channel.id, channelType = "messaging").enqueue { result ->
            if (result.isSuccess) {
                Toast.makeText(context, "Deleted Channel", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("ChannelViewModel", "deleteChannel: Failed to delete channel ")
            }
        }
    }


}


