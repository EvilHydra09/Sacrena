package com.example.loginregisteration.domain.usecase.auth

import com.example.loginregisteration.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
){

     operator fun invoke() : Flow<String?> {
        return authRepository.getToken()
    }


}