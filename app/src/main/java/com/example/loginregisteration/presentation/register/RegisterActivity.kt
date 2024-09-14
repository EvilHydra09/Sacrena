package com.example.loginregisteration.presentation.register

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.loginregisteration.R
import com.example.loginregisteration.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{

            val viewModel: RegisterViewModel = hiltViewModel()
            val state by viewModel.registerState.collectAsState()
            AppTheme {
                RegisterScreen(
                    onEvent = viewModel::onEvent,
                    state = state,
                    onNavigateToLogin = {
                        finish()
                    },
                )
            }
        }
    }
}