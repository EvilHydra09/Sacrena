package com.example.chatapplication.domain.usecase.auth

import com.example.chatapplication.domain.repository.AuthRepository
import javax.inject.Inject

class StreamLogin @Inject constructor(
    private val authRepository: AuthRepository
)
 {

    operator fun invoke() = authRepository.streamLogin()
}