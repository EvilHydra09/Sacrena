package com.example.chatapplication

import android.app.Application
import com.example.chatapplication.domain.helper.ChatHelper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(){

    @Inject
    lateinit var chatHelper: ChatHelper

    @Inject
    lateinit var firebase: FirebaseAuth

    override fun onCreate() {
        super.onCreate()

        val user = firebase.currentUser
        if (user != null) {
            chatHelper.connectChatUser()
        }
    }
}