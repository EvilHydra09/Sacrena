package com.example.loginregisteration.presentation.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginregisteration.domain.usecase.auth.GetCurrentUserUseCase
import com.example.loginregisteration.domain.usecase.auth.LoginUseCase
import com.example.loginregisteration.domain.usecase.auth.SignInWithGoogleUseCase
import com.example.loginregisteration.domain.usecase.auth.StreamLogin
import com.example.loginregisteration.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
   private val loginUseCase: LoginUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,

): ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()




    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.EmailChanged -> validateEmail(event.email)
            is LoginEvent.PasswordChange -> validatePassword(event.password)
            LoginEvent.Submit -> loginUser()
            is LoginEvent.GoogleSignIn -> signInWithGoogle(event.context)
            LoginEvent.ResendVerificationEmail -> resendVerificationEmail()
        }
    }

    private fun resendVerificationEmail() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            user?.let {
                it.sendEmailVerification().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _loginState.value = loginState.value.copy(
                            errorMessage = "Verification email sent.",
                            isSuccess = false
                        )
                    } else {
                        _loginState.value = loginState.value.copy(
                            errorMessage = "Failed to send verification email."
                        )
                    }
                }
            }
        }
    }

    private fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            signInWithGoogleUseCase(context).collect { result ->
                result.fold( // It allows you to specify actions for both success and failure cases of the operation, making it easy to manage the different outcomes.
                    onSuccess = { authResult ->
                        // Handle successful sign-in
                        val currentUser = authResult.user
                        if (currentUser != null) {
                           _loginState.value = loginState.value.copy(isSuccess = true)

                        }
                    },
                    onFailure = { e ->
                        _loginState.value = loginState.value.copy(errorMessage = e.message)
                        Log.d("Issue", "handleGoogleSignIn: ${e.message}")
                    }
                )
            }

        }
    }

    private fun loginUser() {
        if (loginState.value.emailError != null || loginState.value.passwordError != null) {
            return
        }
        viewModelScope.launch {
            loginUseCase(email = loginState.value.email, password = loginState.value.password).collect{result->
                when(result){
                    is Resource.Error -> _loginState.value = loginState.value.copy(isLoading = false, errorMessage = result.message)
                       is Resource.Loading -> _loginState.value = loginState.value.copy(isLoading = true)
                    is Resource.Success -> checkEmailVerification()
                }

            }
        }
    }

    private fun checkEmailVerification() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            if (user != null && user.isEmailVerified) {
                _loginState.value = loginState.value.copy(isLoading = false, isSuccess = true, errorMessage = null)

            } else {
                _loginState.value = loginState.value.copy(isLoading = false, errorMessage = "Please verify your email address to continue.")
            }
        }
    }

    private fun validatePassword(password: String) {
       _loginState.value = _loginState.value.copy(
           password = password,
           passwordError = if (password.length < 8) "Password must be at least 8 characters" else null,
           errorMessage = null
       )
        Log.d("LoginViewModel", "${loginState.value}")
    }

    private fun validateEmail(email: String) {
        _loginState.value = _loginState.value.copy(
            email = email,
            emailError = if (!email.contains("@")) "Invalid email" else null,
            errorMessage = null
        )
    }


}



data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val errorMessage: String? = null,
    val isSuccess : Boolean = false
)