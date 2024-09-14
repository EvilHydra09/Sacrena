package com.example.loginregisteration.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.loginregisteration.R
import com.example.loginregisteration.presentation.common.MyTextField
import com.example.loginregisteration.presentation.common.PasswordTextField
import com.example.loginregisteration.presentation.common.SocialButton
import com.example.loginregisteration.presentation.common.rememberImeState
import com.example.loginregisteration.ui.theme.AppTheme
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit,
    onRegisterClick: () -> Unit,
    onEvent: (LoginEvent) -> Unit,
    state: LoginState
) {
    var visible by remember { mutableStateOf(false) }
    val imeState by rememberImeState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    LaunchedEffect(key1 = state.isSuccess) {
        if (state.isSuccess) {
            onNavigateToHome.invoke()
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = "User Login Successfully",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
    LaunchedEffect(key1 = state.errorMessage) {
        if (state.errorMessage != null) {
            val actionLabel = if (state.errorMessage.contains("verify your email")) {
                "Resend Verification"
            } else {
                null
            }

            val snackBarResult = snackBarHostState.showSnackbar(
                message = state.errorMessage,
                duration = SnackbarDuration.Short,
                actionLabel = actionLabel
            )

            // Handle the snack bar action
            if (actionLabel != null && snackBarResult == SnackbarResult.ActionPerformed) {
                // Trigger resend verification email event
                onEvent(LoginEvent.ResendVerificationEmail)
            }
        }

    }


    Box(modifier = Modifier.fillMaxSize()) {
        Surface() {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                // Header Section
                AnimatedVisibility(!imeState) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(40.dp)
                                .padding(top = 20.dp)
                        )
                        val gradientColors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            MaterialTheme.colorScheme.tertiary,
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher),
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.CenterHorizontally)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = gradientColors,
                                        start = Offset(0f, 0f), // Starting point of the gradient
                                        end = Offset(1000f, 1000f) // Ending point of the gradient
                                    ), shape = CircleShape
                                )
                                .border(
                                    BorderStroke(
                                        width = 1.dp, color =
                                        MaterialTheme.colorScheme.primary
                                    ),
                                    shape = CircleShape,
                                )
                        )
                    }
                }



                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Login in continue using the app",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )


                AnimatedVisibility(visible = state.errorMessage != null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        state.errorMessage ?: "Unknown Error",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Form Section


                MyTextField(
                    value = state.email,
                    onValueChange = {
                        onEvent(LoginEvent.EmailChanged(it))
                    },
                    placeholder = "Enter your email",
                    isError = state.emailError != null,
                    label = "Email",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                AnimatedVisibility(
                    visible = state.emailError != null,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 10.dp)
                ) {
                    Text(
                        text = state.emailError ?: "",
                        color = Color.Red,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))


                PasswordTextField(
                    value = state.password,
                    onValueChange = {
                        onEvent(LoginEvent.PasswordChange(it))
                    },
                    placeholder = "Enter your password",
                    trailingIcon = {
                        IconButton(
                            onClick = { visible = !visible },
                        ) {
                            Icon(
                                painter = painterResource(id = if (visible) R.drawable.eye_open else R.drawable.eye_slash),
                                contentDescription = null
                            )
                        }
                    },
                    isPassword = !visible,
                    isError = state.passwordError != null,
                    label = "Password",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                AnimatedVisibility(
                    visible = state.passwordError != null,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 10.dp)
                ) {
                    Text(
                        text = state.passwordError ?: "",
                        color = Color.Red,
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Forget Password?",
                    modifier = Modifier
                        .clickable { /* Handle forget password click */ }
                        .align(Alignment.End)
                        .alpha(0.6f))
                Spacer(modifier = Modifier.height(15.dp))


                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = {
                        onEvent(LoginEvent.Submit)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text(text = "Login", modifier = Modifier.padding(vertical = 10.dp))
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))


                //Social Media Section

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Color.Gray,
                        thickness = 1.dp
                    )
                    Text(
                        text = "Or Login with",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Color.Gray,
                        thickness = 1.dp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Middle section with social media buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    SocialButton(
                        modifier = Modifier.fillMaxWidth(1f),
                        iconResId = R.drawable.google_icon,
                        onButtonClick = { onEvent(LoginEvent.GoogleSignIn(context)) })

                }

                Spacer(modifier = Modifier.height(24.dp))

                // Bottom section with text and register link
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Don't have an account?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Register",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.clickable { onRegisterClick.invoke() }
                    )
                }

                if (imeState) {
                    Spacer(modifier = Modifier.height(350.dp))
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .safeContentPadding()
        )
    }

}


@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(
            modifier = Modifier,
            onRegisterClick = {},
            onEvent = {},
            state = LoginState(
                email = "",
                password = "",
                isLoading = false,


                ),
            onNavigateToHome = {}
        )
    }

}