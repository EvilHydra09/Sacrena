package com.example.chatapplication

import androidx.lifecycle.ViewModel
import com.example.chatapplication.presentation.nvgraph.Screen
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
):ViewModel() {
    val user = firebaseAuth.currentUser

    private val _startDestination = MutableStateFlow<Screen>(Screen.AuthGraph)
    val startDestination = _startDestination.asStateFlow()

    init {
        if(user != null && user.isEmailVerified){
            _startDestination.value = Screen.MainGraph
        }
        else{
            _startDestination.value = Screen.AuthGraph
        }
    }

}