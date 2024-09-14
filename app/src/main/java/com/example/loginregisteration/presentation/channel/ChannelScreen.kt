package com.example.loginregisteration.presentation.channel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.loginregisteration.R
import com.example.loginregisteration.presentation.channel.components.CustomChannelList
import com.example.loginregisteration.ui.theme.provider
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.compose.ui.channels.list.ChannelList
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamColors
import io.getstream.chat.android.compose.ui.theme.StreamTypography
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.models.User
import io.getstream.chat.android.models.querysort.QuerySortByField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelScreen(
    user: User?,
    navigateToUser: () -> Unit,
    modifier: Modifier = Modifier,
    onChannelClick: (Channel) -> Unit,
    onLogout: () -> Unit = {},
    onDeleteChannel : (Channel) ->Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedChannel by remember { mutableStateOf<Channel?>(null) }



    val client = ChatClient.instance()
    ChatTheme(
        colors = StreamColors.defaultColors().copy(
            primaryAccent = MaterialTheme.colorScheme.primary,
            ownMessagesBackground = MaterialTheme.colorScheme.surface,
            appBackground = MaterialTheme.colorScheme.surface,
            textHighEmphasis = MaterialTheme.colorScheme.onSurface,
            barsBackground = MaterialTheme.colorScheme.surface,
            otherMessagesBackground = MaterialTheme.colorScheme.onSurface,
            infoAccent = MaterialTheme.colorScheme.primary
        ),
        typography = StreamTypography.defaultTypography(
            fontFamily = FontFamily(
                androidx.compose.ui.text.googlefonts.Font(
                    googleFont = GoogleFont("Fira Code"),
                    fontProvider = provider,
                )
            )
        )

    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = modifier.fillMaxSize()) {
                TopAppBar(title = {
                    if (user != null) {
                        AsyncImage(
                            model = user.image,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(45.dp)
                                .clip(shape = CircleShape),
                            error = painterResource(id = R.drawable.placeholder),
                            fallback = painterResource(id = R.drawable.placeholder),
                            placeholder = painterResource(id = R.drawable.placeholder),

                        )
                    }
                }, actions = {
                    IconButton(onClick = {
                        onLogout()
                    }) {
                        Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = null)
                    }
                })
                Text(
                    "Connections",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                    color = Color.White,
                )
                CustomChannelList(
                    viewModel = viewModel(
                        factory =
                        ChannelViewModelFactory(
                            ChatClient.instance(),
                            QuerySortByField.descByName("last_updated"),
                            filters = Filters.and(
                                Filters.eq("type", "messaging"),
                                Filters.`in`("members", listOf(client.getCurrentUser()!!.id)),
                            )
                        ),

                    ),
                    modifier = Modifier.fillMaxSize(),
                    onChannelClick = onChannelClick,
                    onChannelLongClick = {
                        selectedChannel = it
                        showDialog = true
                    },
                    divider = {
                        HorizontalDivider(modifier.padding(horizontal = 8.dp))
                    },

                )
            }
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { navigateToUser.invoke() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .navigationBarsPadding()
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
        // Confirmation Dialog
        if (showDialog && selectedChannel != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Delete Channel") },
                text = { Text("Are you sure you want to delete this channel?") },
                confirmButton = {
                    Button(onClick = {
                        onDeleteChannel(selectedChannel!!)
                        showDialog = false
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}











