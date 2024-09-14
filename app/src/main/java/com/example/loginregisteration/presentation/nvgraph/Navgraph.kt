package com.example.loginregisteration.presentation.nvgraph

import android.content.Intent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.loginregisteration.presentation.channel.ChannelActivity
import com.example.loginregisteration.presentation.login.LoginScreen
import com.example.loginregisteration.presentation.login.LoginViewModel
import com.example.loginregisteration.presentation.register.RegisterScreen
import com.example.loginregisteration.presentation.register.RegisterViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val user = Firebase.auth.currentUser
    val context = LocalContext.current // Obtain the context

    // Determine the start destination
    val startDestination = if (user != null && user.isEmailVerified) {
        // Here, instead of navigating to MainScreen, launch ChannelActivity
        context.startActivity(Intent(context, ChannelActivity::class.java))
        return // Exit the composable to prevent further navigation setup
    } else {
        Screen.AuthScreen.route
    }

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            startDestination = Screen.LoginScreen.route,
            route = Screen.AuthScreen.route
        ) {
            composable(
                route = Screen.LoginScreen.route,
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,

                        )
                },
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = spring()
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                    )
                }
            ) {
                val viewModel: LoginViewModel = hiltViewModel()
                val state by viewModel.loginState.collectAsState()
                LoginScreen(
                    onNavigateToHome = {
                        context.startActivity(Intent(context, ChannelActivity::class.java))
                    },
                    onRegisterClick = { navController.navigate(Screen.RegisterScreen.route) },
                    onEvent = viewModel::onEvent,
                    state = state
                )
            }
            composable(
                route = Screen.RegisterScreen.route,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                    )
                }
            ) {
                val viewModel: RegisterViewModel = hiltViewModel()
                val state by viewModel.registerState.collectAsState()
                RegisterScreen(
                    onEvent = viewModel::onEvent,
                    state = state,
                    onNavigateToLogin = { navController.navigateUp() },
                )
            }
        }
        navigation(
            startDestination = Screen.ChannelScreen.route,
            route = Screen.MainScreen.route
        ) {
            composable(route = Screen.ChannelScreen.route) {

            }

        }
    }

}

