package com.example.loginregisteration.domain.repository

import android.content.Context
import com.example.loginregisteration.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun login (email : String , password : String) : Flow<Resource<Unit>>

    fun register (email : String , password : String, name : String) : Flow<Resource<Unit>>

    fun logout ()

    fun currentUser () : FirebaseUser?

    fun getToken() : Flow<String?>

    suspend fun signInGoogle(context: Context) : Flow<Result<AuthResult>>

    fun streamLogin()


}