package com.example.chatapplication.presentation.logout

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.chatapplication.domain.usecase.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import javax.inject.Inject

@HiltViewModel
class LogoutViewmodel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val client: ChatClient
) : ViewModel() {

    fun logout() {
        Log.d("ChannelActivity", "Attempting to log out")
        if (client == null) {
            Log.e("ChannelActivity", "ChatClient is null, cannot disconnect.")
            return
        }

        client.disconnect(flushPersistence = true).enqueue { result ->
            if (result.isSuccess) {
                Log.d("ChannelActivity", "Logged out successfully")
                logoutUseCase.invoke()
            } else {
                Log.d("ChannelActivity", "Error logging out: ${result.errorOrNull()}")
            }
        }
    }

}