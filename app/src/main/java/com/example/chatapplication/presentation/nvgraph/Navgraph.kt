package com.example.chatapplication.presentation.nvgraph

import android.widget.Toast
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
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
import androidx.navigation.toRoute
import com.example.chatapplication.presentation.channel.ChannelScreen
import com.example.chatapplication.presentation.channel.ChannelViewModel
import com.example.chatapplication.presentation.login.LoginScreen
import com.example.chatapplication.presentation.login.LoginViewModel
import com.example.chatapplication.presentation.logout.LogoutScreen
import com.example.chatapplication.presentation.logout.LogoutViewmodel
import com.example.chatapplication.presentation.message.MessageScreen
import com.example.chatapplication.presentation.register.RegisterScreen
import com.example.chatapplication.presentation.register.RegisterViewModel
import com.example.chatapplication.presentation.user.UserScreen
import com.example.chatapplication.presentation.user.UserViewModel


@Composable
fun NavGraph(modifier: Modifier = Modifier, startDestination: Screen) {
    val context = LocalContext.current // Obtain the context

    val navController = rememberNavController()


    Surface {
        NavHost(navController = navController, startDestination = startDestination) {
            navigation<Screen.AuthGraph>(
                startDestination = Screen.LoginScreen,
            ) {
                composable<Screen.LoginScreen>(
                ) {
                    val viewModel: LoginViewModel = hiltViewModel()
                    val state by viewModel.loginState.collectAsState()
                    LoginScreen(
                        onNavigateToHome = {
                            navController.navigate(Screen.MainGraph) {
                                popUpTo(Screen.AuthGraph) {
                                    inclusive = true
                                }
                            }
                        },
                        onRegisterClick = { navController.navigate(Screen.RegisterScreen) },
                        onEvent = viewModel::onEvent,
                        state = state
                    )
                }
                composable<Screen.RegisterScreen>(

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
            navigation<Screen.MainGraph>(
                startDestination = Screen.ChannelScreen,
            ) {
                composable<Screen.ChannelScreen> {
                    val viewModel: ChannelViewModel = hiltViewModel()
                    val clientInitialisationState by viewModel.initializationState.collectAsState()
                    val client = viewModel.chatClient
                    ChannelScreen(
                        initializationState = clientInitialisationState,
                        onChannelClick = { channelId ->
                            navController.navigate(Screen.MessageScreen(channelId))
                        },
                        onLogout = {
                            navController.navigate(Screen.LogoutScreen)
                        },
                        onDeleteChannel = { channel ->
                            viewModel.deleteChannel(channel, context)
                        },
                        navigateToUser = {
                            navController.navigate(Screen.UserScreen)
                        },
                        client = client
                    )
                }
                composable<Screen.MessageScreen> { entry ->
                    val messageScreen = entry.toRoute<Screen.MessageScreen>()
                    MessageScreen(
                        modifier = Modifier
                            .statusBarsPadding()
                            .navigationBarsPadding()
                            .imePadding(),
                        channelId = messageScreen.channelId,
                        onBackClick = { navController.navigateUp() }
                    )
                }
                composable<Screen.UserScreen> {
                    val viewModel: UserViewModel = hiltViewModel()
                    val state by viewModel.uiState.collectAsState()
                    UserScreen(
                        modifier = Modifier
                            .statusBarsPadding()
                            .navigationBarsPadding()
                        ,
                        state = state,
                        onQueryChange = { viewModel.onQueryChange(it) },
                        onChannelClick = {
                            viewModel.createUser(
                                selectedUser = it,
                                onSuccess = { channelId ->
                                    navController.navigate(Screen.MessageScreen(channelId))
                                }
                            )
                            viewModel.getChannelIdBetweenUsers(
                                user2 = it,
                                onSuccess = { channelId ->
                                    Toast.makeText(
                                        context,
                                        "Channel ID: $channelId",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onError = { error ->
                                    Toast.makeText(
                                        context,
                                        "Error: ${error.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        },
                    )
                }
                composable<Screen.LogoutScreen> {
                    val viewModel: LogoutViewmodel = hiltViewModel()
                    LogoutScreen(
                        onLogout = {
                            viewModel.logout()
                            navController.navigate(Screen.AuthGraph) {
                                popUpTo(Screen.MainGraph) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            }

        }
    }

}



