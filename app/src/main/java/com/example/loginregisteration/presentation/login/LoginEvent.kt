package com.example.loginregisteration.presentation.login

import android.content.Context

sealed class LoginEvent {

    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChange(val password : String) : LoginEvent()
    data object Submit : LoginEvent()
    data object ResendVerificationEmail : LoginEvent()

    data class GoogleSignIn(val context: Context) : LoginEvent()

}