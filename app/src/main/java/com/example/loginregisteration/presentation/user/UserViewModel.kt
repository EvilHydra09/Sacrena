package com.example.loginregisteration.presentation.user

import android.util.Log
import androidx.lifecycle.ViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.models.User
import io.getstream.chat.android.models.querysort.QuerySortByField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update



class UserViewModel : ViewModel() {

    private val client = ChatClient.instance()
    private val _uiState = MutableStateFlow(UsersUiState())
    val uiState: StateFlow<UsersUiState> = _uiState

    init {
        queryAllUsers()
        Log.d("UserViewmodel", "init")
    }

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
        if (newQuery.isEmpty()) {
            queryAllUsers()
        } else {
            searchUser(newQuery)
        }
    }

    private fun searchUser(query: String) {
        val filters = Filters.and(
            Filters.autocomplete("name", query),
            Filters.ne("id", client.getCurrentUser()!!.id)
        )
        val request = QueryUsersRequest(
            filter = filters,
            offset = 0,
            limit = 100,
            querySort = QuerySortByField.descByName("last_updated")
        )

        client.queryUsers(request).enqueue{
            if (it.isSuccess){
                Log.d("UserViewmodel", it.toString())
                _uiState.value = _uiState.value.copy(users = it.getOrThrow())
                Log.d("UserViewmodel", uiState.value.users.toString())
                Log.d("UserViewmodel", "Hello")
            }else{
                Log.d("UserViewmodel", it.toString())
                _uiState.update { it.copy(error = it.error) }
            }
        }
    }

    private fun queryAllUsers() {
        val request = QueryUsersRequest(
            filter = Filters.ne("id", client.getCurrentUser()!!.id),
            offset = 0,
            limit = 100,
            querySort = QuerySortByField.descByName("last_updated")
        )

        client.queryUsers(request).enqueue{
            if (it.isSuccess){
                _uiState.value = _uiState.value.copy(users = it.getOrThrow())
                Log.d("UserViewmodel", it.toString())
            }else{
                _uiState.update { it.copy(error = it.error) }
                Log.d("UserViewmodel", it.toString())
            }
        }
    }

    fun createUser(selectedUser: String, onSuccess: (String) -> Unit) {
        client.createChannel(
            channelType = "messaging",
            memberIds = listOf(client.getCurrentUser()!!.id, selectedUser),
            extraData = emptyMap(),
            channelId = ""  // Here you can specify a channelId or let the server generate one
        ).enqueue {
            if (it.isSuccess) {
                val channelId = it.getOrThrow().cid // Retrieve the channel ID
                _uiState.update { it.copy(isSuccessful = true) }
                onSuccess(channelId) // Pass the channelId to the onSuccess callback
            } else {
                Log.d("UserViewModel", it.toString())
                _uiState.update { it.copy(error = it.error) }
            }
        }
    }
}
data class UsersUiState(
    val isSuccessful: Boolean = false,
    val users: List<User> = emptyList(),
    val query: String = "",
    val error: String? = null
)