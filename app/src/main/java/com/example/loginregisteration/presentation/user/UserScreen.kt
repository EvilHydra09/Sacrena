package com.example.loginregisteration.presentation.user

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.loginregisteration.presentation.user.components.SearchBar
import com.example.loginregisteration.presentation.user.components.UsersList

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    state: UsersUiState,
    onQueryChange: (String) -> Unit,
    onChannelClick: (String) -> Unit,

    ) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.isSuccessful) {
        if (state.isSuccessful) {
            Toast.makeText(context, "Channel Created", Toast.LENGTH_SHORT).show()

        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(query = state.query, onQueryChange = onQueryChange)
        UsersList(
            users = state.users,
            onChannelClick = {
                onChannelClick.invoke(it)
            }
        )


    }



}