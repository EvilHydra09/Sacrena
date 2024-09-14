package com.example.loginregisteration.presentation.user

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loginregisteration.presentation.message.MessageActivity

import com.example.loginregisteration.ui.theme.AppTheme

class UserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val viewModel: UserViewModel = viewModel()
                val state by viewModel.uiState.collectAsState()
                Surface() {
                    UserScreen(
                        state = state,
                        onQueryChange = {viewModel.onQueryChange(it)},
                        onChannelClick = {
                            viewModel.createUser(selectedUser = it, onSuccess = {channelId->
                                startActivity(MessageActivity.getIntent(this, channelId))
                            })
                        },
                    )
                }
            }
        }
    }
}