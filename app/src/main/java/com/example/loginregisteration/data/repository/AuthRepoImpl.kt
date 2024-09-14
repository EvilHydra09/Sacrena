package com.example.loginregisteration.data.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.loginregisteration.R
import com.example.loginregisteration.domain.repository.AuthRepository
import com.example.loginregisteration.util.Resource
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val client: ChatClient
) : AuthRepository {

    override fun login(email: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        try {
            // Use the `await` function to suspend until the task is completed
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }.catch { e ->
        emit(Resource.Error(e.message ?: "An error occurred"))
    }

    override fun register(email: String, password: String, name: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            // Create user with email and password
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user

            user?.let {
                // Update user profile with the display name
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }
                it.updateProfile(profileUpdates).await()

                // Send verification email
                it.sendEmailVerification().await()

                emit(Resource.Success(Unit))
            } ?: throw Exception("User registration failed")
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }


    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun currentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun getToken(): Flow<String?> = flow {
        val user = firebaseAuth.currentUser
        if (user != null) {
            try {
                val tokenResult = user.getIdToken(true).await()
                emit(tokenResult.token)
            } catch (e: Exception) {
                emit(null) // Emit null if token retrieval fails
            }
        } else {
            emit(null) // Emit null if no user is logged in
        }
    }

    override suspend fun signInGoogle(context: Context): Flow<Result<AuthResult>> {
        return callbackFlow {
            try {
                // Initialize Credential Manager
                val credentialManager: CredentialManager = CredentialManager.create(context)

                // Generate a nonce (a random number used once) for security
                val ranNonce: String = UUID.randomUUID().toString()
                val bytes: ByteArray = ranNonce.toByteArray()
                val md: MessageDigest = MessageDigest.getInstance("SHA-256")
                val digest: ByteArray = md.digest(bytes)
                val hashedNonce: String = digest.fold("") { str, it -> str + "%02x".format(it) }

                // Set up Google ID option with necessary parameters
                val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false) // To give the user the option to choose from any Google account on their device, not just the ones they've used with your app before.
                    .setServerClientId(context.getString(R.string.web_client)) // This is required to identify the app on the backend server.
                    .setNonce(hashedNonce) // A nonce is a unique, random string used to ensure that the ID token received is fresh and to prevent replay attacks.
                    .setAutoSelectEnabled(true) // Which allows the user to be automatically signed in without additional user interaction if there is a single eligible account.
                    .build()

                // Create a credential request with the Google ID option
                val request: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // Get the credential result from the Credential Manager
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                // Check if the received credential is a valid Google ID Token
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    // Extract the Google ID Token credential
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    // Create an auth credential using the Google ID Token
                    val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    // Sign in with Firebase using the auth credential
                    val authResult = firebaseAuth.signInWithCredential(authCredential).await() // .await() -> allows the coroutine to wait for the result of the authentication operation before proceeding.
                    // Send the successful result
                    trySend(Result.success(authResult)) // Is used to send the result of the Firebase sign-in operation to the Flow's collectors.
                } else {
                    // Throw an exception if the credential type is invalid
                    throw RuntimeException("Received an invalid credential type.")
                }

            } catch (e: GetCredentialCancellationException) {
                // Handle sign-in cancellation
                trySend(Result.failure(Exception("Sign-in was canceled.")))
            } catch (e: Exception) {
                // Handle other exceptions
                trySend(Result.failure(e))
            }

            // When a collector starts collecting from the callbackFlow, the flow remains open and ready to emit values until the awaitClose block is reached or the flow is cancelled.
            // Even though the current block is empty, in other scenarios, you might use the awaitClose block to unregister listeners or release resources associated with the callback-based API.
            awaitClose { }
        }
    }

    override fun streamLogin() {
        val firebaseUser = currentUser()
        val user = firebaseUser?.let {
            User(
                id = it.uid,
                name = it.displayName ?: "No Name",
                image = it.photoUrl.toString()
            )
        }
        val token = user?.let { client.devToken(it.id) }

        if (user != null) {
            if (token != null) {
                client.connectUser(user,token).enqueue{
                    if (it.isSuccess){
                        Log.d("TAG", "streamLogin: Success")
                    }else{
                        Log.d("TAG", "streamLogin: ${it.errorOrNull().toString()}")
                    }
                }
            }
        }
    }


}