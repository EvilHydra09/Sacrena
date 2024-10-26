package com.example.chatapplication.presentation.message

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import com.example.chatapplication.presentation.message.component.CustomMessageScreen
import com.example.chatapplication.ui.theme.provider
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamColors
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.ui.theme.StreamTypography
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    channelId: String,
    onBackClick: () -> Unit = {}
) {
    ChatTheme(
        shapes = StreamShapes.defaultShapes().copy(
            attachment = RoundedCornerShape(16.dp),
            myMessageBubble = RoundedCornerShape(
                topStart = 10.dp,
                topEnd = 10.dp,
                bottomStart = 10.dp
            ),
            otherMessageBubble = RoundedCornerShape(
                topStart = 10.dp,
                topEnd = 10.dp,
                bottomEnd = 10.dp
            ),
            inputField = RoundedCornerShape(16.dp)
        ),
        colors = StreamColors.defaultColors().copy(
            appBackground = MaterialTheme.colorScheme.surface,
            primaryAccent = MaterialTheme.colorScheme.primary,
            ownMessagesBackground = MaterialTheme.colorScheme.primary,
            otherMessagesBackground = MaterialTheme.colorScheme.onSurface,
            barsBackground = MaterialTheme.colorScheme.surface,
            inputBackground = MaterialTheme.colorScheme.surface,
            textHighEmphasis = MaterialTheme.colorScheme.onSurface,
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
        Box(
            modifier = modifier
        ) {
            CustomMessageScreen(
                viewModelFactory = MessagesViewModelFactory(
                    context = LocalContext.current,
                    channelId = channelId
                ),
                onBackPressed = onBackClick
            )
        }

    }
}