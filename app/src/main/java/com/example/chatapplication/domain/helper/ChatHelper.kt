package com.example.chatapplication.domain.helper

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User
import javax.inject.Inject

class ChatHelper @Inject constructor(private val client: ChatClient,private val firebase: FirebaseAuth) {

    fun connectChatUser() {
        val currentFirebaseUser = firebase.currentUser
        val userToken = client.devToken(currentFirebaseUser!!.uid)
        val user = User(
            id = currentFirebaseUser.uid,
            name = currentFirebaseUser.displayName ?: "No Name",
            image = currentFirebaseUser.photoUrl.toString(),
            extraData = mapOf("element" to "earth")
        )
        if (client.getCurrentUser() == null) {
            client.connectUser(
                user = user,
                token = userToken
            ).enqueue { result ->
                if (result.isSuccess) {
                    Log.d("ChatHelper", "User connected successfully ${result.getOrNull()}")
                } else {
                    Log.d("ChatHelper", "User connection failed")
                }
            }
        }
    }
}