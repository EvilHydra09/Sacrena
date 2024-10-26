package com.example.chatapplication.domain.usecase.auth

import android.content.Context
import com.example.chatapplication.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository

) {

    suspend operator fun invoke(context: Context) = authRepository.signInGoogle(context)

}