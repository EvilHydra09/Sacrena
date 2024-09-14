package com.example.loginregisteration.presentation.message.component

import io.getstream.chat.android.compose.ui.components.avatar.Avatar

import io.getstream.chat.android.compose.ui.components.avatar.GroupAvatar
import io.getstream.chat.android.compose.ui.components.avatar.UserAvatar

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import io.getstream.chat.android.compose.state.OnlineIndicatorAlignment
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.User
import io.getstream.chat.android.ui.common.utils.extensions.initials


@Composable
public fun MyChannelAvatar(
    channel: Channel,
    currentUser: User?,
    modifier: Modifier = Modifier,
    shape: Shape = ChatTheme.shapes.avatar,
    textStyle: TextStyle = ChatTheme.typography.title3Bold,
    groupAvatarTextStyle: TextStyle = ChatTheme.typography.captionBold,
    showOnlineIndicator: Boolean = true,
    onlineIndicatorAlignment: OnlineIndicatorAlignment = OnlineIndicatorAlignment.TopEnd,
    onlineIndicator: @Composable BoxScope.() -> Unit = {
        DefaultOnlineIndicator(onlineIndicatorAlignment)
    },
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null,
) {
    val members = channel.members
    val memberCount = members.size

    when {
        /**
         * If the channel has an image we load that as a priority.
         */
        channel.image.isNotEmpty() -> {
            Avatar(
                modifier = modifier,
                imageUrl = channel.image,
                initials = channel.initials,
                textStyle = textStyle,
                shape = shape,
                contentDescription = contentDescription,
                onClick = onClick,
            )
        }


        memberCount == 1 -> {
            val user = members.first().user

            MyUserAvatar(
                modifier = modifier,
                user = user,
                shape = shape,
                contentDescription = user.name,
                showOnlineIndicator = showOnlineIndicator,
                onlineIndicatorAlignment = onlineIndicatorAlignment,
                onlineIndicator = onlineIndicator,
                onClick = onClick,
            )
        }
        /**
         * If the channel has two members and one of the is the current user - we show the other
         * member's image or initials.
         */
        memberCount == 2 && members.any { it.user.id == currentUser?.id } -> {
            val user = members.first { it.user.id != currentUser?.id }.user

            MyUserAvatar(
                modifier = modifier,
                user = user,
                shape = shape,
                contentDescription = user.name,
                showOnlineIndicator = showOnlineIndicator,
                onlineIndicatorAlignment = onlineIndicatorAlignment,
                onlineIndicator = onlineIndicator,
                onClick = onClick,
            )
        }
        /**
         * If the channel has more than two members - we load a matrix of their images or initials.
         */
        else -> {
            val users = members.filter { it.user.id != currentUser?.id }.map { it.user }

            GroupAvatar(
                users = users,
                modifier = modifier,
                shape = shape,
                textStyle = groupAvatarTextStyle,
                onClick = onClick,
            )
        }
    }
}




