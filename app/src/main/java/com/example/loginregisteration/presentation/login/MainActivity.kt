package com.example.loginregisteration.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.loginregisteration.presentation.channel.ChannelActivity
import com.example.loginregisteration.presentation.register.RegisterActivity
import com.example.loginregisteration.ui.theme.AppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
         if (user != null && user.isEmailVerified) {
            startActivity(Intent(applicationContext, ChannelActivity::class.java))
            finish()
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val viewModel: LoginViewModel = hiltViewModel()
                val state by viewModel.loginState.collectAsState()
                LoginScreen(
                    onNavigateToHome = {
                        startActivity(Intent(this, ChannelActivity::class.java))
                        finish()
                    },
                    onRegisterClick = {
                        startActivity(Intent(this, RegisterActivity::class.java))
                    },
                    onEvent = viewModel::onEvent,
                    state = state
                )
            }
        }
    }
}


