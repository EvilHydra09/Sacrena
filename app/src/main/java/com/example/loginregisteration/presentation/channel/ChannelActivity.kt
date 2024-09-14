package com.example.loginregisteration.presentation.channel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loginregisteration.presentation.login.MainActivity
import com.example.loginregisteration.presentation.message.MessageActivity
import com.example.loginregisteration.presentation.user.UserActivity
import com.example.loginregisteration.ui.theme.AppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.models.InitializationState
import io.getstream.chat.android.models.User
import javax.inject.Inject

@AndroidEntryPoint
class ChannelActivity : ComponentActivity() {
    @Inject
    lateinit var client: ChatClient

    @Inject
    lateinit var user: FirebaseAuth

    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val firebaseUser = user.currentUser

        val user = firebaseUser?.let {
            User(
                id = it.uid,
                name = it.displayName ?: "No Name",
                image = it.photoUrl.toString(),

                )
        }
        Log.d("ChannelActivity", user.toString())

        // 3 - Authenticate and connect the user

        if (user != null) {
            Log.d("ChannelActivity", "User is not null")
            if (client.getCurrentUser() == null) {
                client.connectUser(
                    user = user,
                    token = client.devToken(user.id)
                ).enqueue() {
                    if (it.isSuccess) {
                        Log.d("ChannelActivity", "Success")
                    } else {
                        Log.d("ChannelActivity", it.errorOrNull().toString())
                    }
                }
            }
        }

        setContent {
            val clientInitialisationState by client.clientState.initializationState.collectAsState()
            val viewModel: ChannelViewModel = viewModel()
            ChatTheme {
                AppTheme {
                    Surface {
                        when (clientInitialisationState) {
                            InitializationState.COMPLETE -> {
                                ChannelScreen(
                                    user = user,
                                    modifier = Modifier.statusBarsPadding(),
                                    onChannelClick = {
                                        startActivity(
                                            MessageActivity.getIntent(
                                                this,
                                                channelId = it.cid
                                            )
                                        )
                                        finish()
                                    },
                                    navigateToUser = {
                                        startActivity(
                                            Intent(
                                                this@ChannelActivity,
                                                UserActivity::class.java
                                            )
                                        )
                                    },
                                    onLogout = {
                                        client.disconnect(flushPersistence = false).enqueue {
                                            if (it.isSuccess) {
                                                Log.d("ChannelActivity", "Logged out successfully")
                                                auth.signOut()
                                                startActivity(
                                                    Intent(
                                                        this@ChannelActivity,
                                                        MainActivity::class.java
                                                    )
                                                )
                                                finish()
                                            }
                                        }
                                    },
                                    onDeleteChannel = {
                                        viewModel.deleteChannel(it, this)
                                    }
                                )


                            }

                            InitializationState.INITIALIZING -> {
                                Box(modifier = Modifier
                                    .statusBarsPadding()
                                    .fillMaxSize()) {
                                    Text(text = " initialized...")
                                }
                            }

                            InitializationState.NOT_INITIALIZED -> {
                                Box(modifier = Modifier
                                    .statusBarsPadding()
                                    .fillMaxSize()) {
                                    Text(text = "Not initialized...")
                                }

                            }
                        }
                    }
                }

            }
        }
    }
}