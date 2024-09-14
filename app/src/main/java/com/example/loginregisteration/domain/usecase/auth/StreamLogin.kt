package com.example.loginregisteration.domain.usecase.auth

import com.example.loginregisteration.domain.repository.AuthRepository
import javax.inject.Inject

class StreamLogin @Inject constructor(
    private val authRepository: AuthRepository
)
 {

    operator fun invoke() = authRepository.streamLogin()
}