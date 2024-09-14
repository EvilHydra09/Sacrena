package com.example.loginregisteration.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginregisteration.domain.usecase.auth.RegisterUseCase
import com.example.loginregisteration.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel@Inject constructor(
    private val registerUseCase: RegisterUseCase
): ViewModel() {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()


    fun onEvent(event: RegisterEvent){

        when(event){
            is RegisterEvent.UserNameChanged -> validateUserName(event.name)
            is RegisterEvent.ConfirmPasswordChanged -> validateConfirmPassword(event.confirmPassword)
            is RegisterEvent.EmailChanged -> validateEmail(event.email)
            is RegisterEvent.PasswordChanged -> validatePassword(event.password)
            RegisterEvent.Register -> registerUser()

        }
    }

    private fun validateUserName(name: String) {
        _registerState.value = _registerState.value.copy(
            name = name,
            nameError = if (name.isBlank()) "Username cannot be empty" else null
        )
    }

    private fun validateEmail(email: String) {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        _registerState.value = _registerState.value.copy(
            email = email,
            emailError = when {
                email.isBlank() -> "Email cannot be empty"
                !email.matches(emailRegex.toRegex()) -> "Invalid email format"
                else -> null
            }
        )
    }

    private fun validatePassword(password: String) {
        _registerState.value = _registerState.value.copy(
            password = password,
            passwordError = if (password.length < 8) "Password must be at least 8 characters" else null
        )
    }

    private fun validateConfirmPassword(confirmPassword: String) {
        _registerState.value = _registerState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = if (confirmPassword != _registerState.value.password) "Passwords do not match" else null
        )
    }

    private fun registerUser() {
        if (_registerState.value.nameError != null ||
            _registerState.value.emailError != null||
            _registerState.value.passwordError != null ||
            _registerState.value.confirmPasswordError != null
        ) {
           return
        }
        viewModelScope.launch {
            registerUseCase(
                email = registerState.value.email,
                password = registerState.value.password,
                name = registerState.value.name
            ).collect{result->
                when(result){
                    is Resource.Error -> _registerState.value = registerState.value.copy(isLoading = false, errorMessage = result.message)
                    is Resource.Loading -> _registerState.value = registerState.value.copy(isLoading = true)
                    is Resource.Success -> _registerState.value = registerState.value.copy(isLoading = false, isSuccess = true, errorMessage = null)
                }
            }
        }
    }


}


data class RegisterState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val name: String = "",

    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val nameError: String? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess : Boolean = false
)