package com.example.chatapplication.presentation.nvgraph

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object LoginScreen : Screen()

    @Serializable
    data object RegisterScreen : Screen()

    @Serializable
    data object HomeScreen : Screen()

    @Serializable
    data object UserScreen : Screen()

    @Serializable
    data object ChannelScreen : Screen()

    @Serializable
    data class MessageScreen(val channelId: String) : Screen()

    @Serializable
    data object LogoutScreen : Screen()



    //Sub Graph
    @Serializable
    data object AuthGraph : Screen()

    @Serializable
    data object MainGraph : Screen()




}