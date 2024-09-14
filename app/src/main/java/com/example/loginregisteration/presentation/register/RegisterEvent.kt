package com.example.loginregisteration.presentation.register

sealed class RegisterEvent {
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegisterEvent()
    data class UserNameChanged(val name: String) : RegisterEvent()


    data object Register : RegisterEvent()
}