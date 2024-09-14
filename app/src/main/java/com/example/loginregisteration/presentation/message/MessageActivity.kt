package com.example.loginregisteration.presentation.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import com.example.loginregisteration.presentation.channel.ChannelActivity
import com.example.loginregisteration.presentation.message.component.CustomMessageScreen
import com.example.loginregisteration.ui.theme.AppTheme
import com.example.loginregisteration.ui.theme.provider
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamColors
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.ui.theme.StreamTypography
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

class MessageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelId = intent.getStringExtra(KEY_CHANNEL_ID)

        if (channelId == null) {
            finish()
            return
        }

        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface {
                    MyMessageScreen(
                        channelId = channelId,
                        modifier = Modifier
                            .statusBarsPadding()
                            .navigationBarsPadding()
                            .imePadding(),
                    )

                }
            }

        }
    }

    @Composable
    private fun MyMessageScreen(
        channelId: String,
        modifier: Modifier = Modifier,

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
                linkBackground = Color.Blue
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




                Column (modifier = modifier.fillMaxSize()) {
                CustomMessageScreen(
                    viewModelFactory = MessagesViewModelFactory(
                        context = LocalContext.current,
                        channelId = channelId
                    ),
                    onBackPressed = {
                        val intent = Intent(this@MessageActivity, ChannelActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                )



            }

        }
    }


    companion object {
        private const val KEY_CHANNEL_ID = "channelId"

        fun getIntent(context: Context, channelId: String): Intent {
            return Intent(context, MessageActivity::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
            }
        }
    }
}