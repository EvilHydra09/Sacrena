package com.example.loginregisteration.presentation.nvgraph

sealed class Screen(val route : String) {

    data object LoginScreen : Screen("login_screen")
    data object RegisterScreen : Screen("register_screen")
    data object HomeScreen : Screen("home_screen")
    data object UserScreen : Screen("profile_screen")
    data object ChannelScreen : Screen("channel_screen")
    data object MessageScreen : Screen("message_screen")


    //Sub Graph
    data object AuthScreen : Screen("auth_screen")
    data object MainScreen : Screen("main_screen")




}