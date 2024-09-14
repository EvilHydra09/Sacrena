package com.example.loginregisteration.presentation.channel.components

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import io.getstream.chat.android.compose.ui.channels.list.ChannelItem


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight

import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loginregisteration.presentation.message.component.MyChannelAvatar
import com.example.loginregisteration.presentation.user.components.ChannelImage
import io.getstream.chat.android.client.extensions.currentUserUnreadCount
import io.getstream.chat.android.compose.R

import io.getstream.chat.android.compose.state.channels.list.ItemState
import io.getstream.chat.android.compose.ui.components.Timestamp
import io.getstream.chat.android.compose.ui.components.avatar.ChannelAvatar
import io.getstream.chat.android.compose.ui.components.channels.MessageReadStatusIcon
import io.getstream.chat.android.compose.ui.components.channels.UnreadCountIndicator
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.util.getLastMessage
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.User

@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun CustomChannelItem(
    channelItem: ItemState.ChannelItemState,
    currentUser: User?,
    onChannelClick: (Channel) -> Unit,
    onChannelLongClick: (Channel) -> Unit,
    modifier: Modifier = Modifier,
    leadingContent: @Composable RowScope.(ItemState.ChannelItemState) -> Unit = {
        DefaultChannelItemLeadingContent(
            channelItem = it,
            currentUser = currentUser,
        )
    },
    centerContent: @Composable RowScope.(ItemState.ChannelItemState) -> Unit = {
        DefaultChannelItemCenterContent(
            channel = it.channel,
            isMuted = it.isMuted,
            currentUser = currentUser,
        )
    },
    trailingContent: @Composable RowScope.(ItemState.ChannelItemState) -> Unit = {
        DefaultChannelItemTrailingContent(
            channel = it.channel,
            currentUser = currentUser,
        )
    },
) {
    val channel = channelItem.channel
    val description = stringResource(id = R.string.stream_compose_cd_channel_item)

    Column(
        modifier = modifier
            .testTag("Stream_ChannelItem")
            .fillMaxWidth()
            .wrapContentHeight()
            .semantics { contentDescription = description }
            .combinedClickable(
                onClick = { onChannelClick(channel) },
                onLongClick = { onChannelLongClick(channel) },
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() },
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingContent(channelItem)

            centerContent(channelItem)

            trailingContent(channelItem)
        }
    }
}


@Composable
internal fun DefaultChannelItemLeadingContent(
    channelItem: ItemState.ChannelItemState,
    currentUser: User?,
) {
//    if (currentUser != null) {
//        ChannelImage(
//            url = channelItem.channel.image,
//            status = channelItem.channel.members.any { it.user.id == currentUser?.id }
//        )
//    }
    MyChannelAvatar(
        modifier = Modifier
            .padding(
                start = ChatTheme.dimens.channelItemHorizontalPadding,
                end = 4.dp,
                top = ChatTheme.dimens.channelItemVerticalPadding,
                bottom = ChatTheme.dimens.channelItemVerticalPadding,
            )
            .size(50.dp),
        channel = channelItem.channel,
        currentUser = currentUser,
    )
}


@Composable
internal fun RowScope.DefaultChannelItemCenterContent(
    channel: Channel,
    isMuted: Boolean,
    currentUser: User?,
) {
    Column(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp)
            .weight(1f)
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
    ) {
        val channelName: (@Composable (modifier: Modifier) -> Unit) = @Composable {
            Text(
                modifier = it,
                text = ChatTheme.channelNameFormatter.formatChannelName(channel, currentUser),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = ChatTheme.colors.textHighEmphasis,
            )
        }

        if (isMuted) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                channelName(Modifier.weight(weight = 1f, fill = false))

                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(16.dp),
                    painter = painterResource(id = R.drawable.stream_compose_ic_muted),
                    contentDescription = null,
                    tint = ChatTheme.colors.textLowEmphasis,
                )
            }
        } else {
            channelName(Modifier)
        }

        val lastMessageText = channel.getLastMessage(currentUser)?.let { lastMessage ->
            ChatTheme.messagePreviewFormatter.formatMessagePreview(lastMessage, currentUser)
        } ?: AnnotatedString("")

        if (lastMessageText.isNotEmpty()) {
            Text(
                text = lastMessageText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = ChatTheme.typography.body,
                color = ChatTheme.colors.textLowEmphasis,
            )
        }
    }
}


@Composable
internal fun RowScope.DefaultChannelItemTrailingContent(
    channel: Channel,
    currentUser: User?,
) {
    val lastMessage = channel.getLastMessage(currentUser)

    if (lastMessage != null) {
        Column(
            modifier = Modifier
                .padding(
                    start = 4.dp,
                    end = ChatTheme.dimens.channelItemHorizontalPadding,
                    top = ChatTheme.dimens.channelItemVerticalPadding,
                    bottom = ChatTheme.dimens.channelItemVerticalPadding,
                )
                .wrapContentHeight()
                .align(Alignment.Bottom),
            horizontalAlignment = Alignment.End,
        ) {
            val unreadCount = channel.currentUserUnreadCount

            if (unreadCount > 0) {
                UnreadCountIndicator(
                    modifier = Modifier.padding(bottom = 4.dp),
                    unreadCount = unreadCount,
                )
            }

            val isLastMessageFromCurrentUser = lastMessage.user.id == currentUser?.id

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isLastMessageFromCurrentUser) {
                    MessageReadStatusIcon(
                        channel = channel,
                        message = lastMessage,
                        currentUser = currentUser,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .heightIn(16.dp),
                    )
                }

                Timestamp(date = channel.lastUpdated)
            }
        }
    }
}



@Composable
private fun ChannelItemPreview(
    channel: Channel,
    isMuted: Boolean = false,
    currentUser: User? = null,
) {
    ChatTheme {
        ChannelItem(
            channelItem = ItemState.ChannelItemState(
                channel = channel,
                isMuted = isMuted,
            ),
            currentUser = currentUser,
            onChannelClick = {},
            onChannelLongClick = {},
        )
    }
}
