package com.example.chatapplication.presentation.channel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.InitializationState

@Composable
fun ChannelScreen(
    initializationState: InitializationState,
    onChannelClick: (String) -> Unit,
    onLogout: () -> Unit,
    onDeleteChannel: (Channel) -> Unit,
    navigateToUser: () -> Unit,
    client: ChatClient
) {
        when (initializationState) {
            InitializationState.COMPLETE -> {
                Column(
                    modifier = Modifier.statusBarsPadding()
                ) {
                    IconButton(onClick = {
                        onLogout()
                    }) {
                        Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = null)
                    }
                    ChannelContent(
                        onChannelClick = { channel ->
                            onChannelClick(channel.cid)
                        },
                        onDeleteChannel = onDeleteChannel,
                        navigateToUser = navigateToUser,
                        client = client
                    )
                }
            }

            InitializationState.INITIALIZING -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxSize()
                        .wrapContentSize(
                            align = Alignment.Center
                        )
                )
            }

            InitializationState.NOT_INITIALIZED -> {
                Text(text = "Not initialized...")
            }
        }
}