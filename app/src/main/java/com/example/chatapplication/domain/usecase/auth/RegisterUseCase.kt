package com.example.chatapplication.domain.usecase.auth

import com.example.chatapplication.domain.repository.AuthRepository
import com.example.chatapplication.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    operator fun invoke(email: String, password: String, name: String): Flow<Resource<Unit>> {
        if (email.isBlank() || password.isBlank()) {
            return flowOf(Resource.Error("Email and password cannot be empty"))
        }
        return repository.register(email, password, name)
    }


}