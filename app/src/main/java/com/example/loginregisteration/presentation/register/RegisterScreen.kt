package com.example.loginregisteration.presentation.register

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.loginregisteration.R
import com.example.loginregisteration.presentation.common.BackButton
import com.example.loginregisteration.presentation.common.MyTextField
import com.example.loginregisteration.presentation.common.PasswordTextField
import com.example.loginregisteration.presentation.common.rememberImeState
import com.example.loginregisteration.presentation.login.LoginEvent
import com.example.loginregisteration.presentation.login.LoginState
import com.example.loginregisteration.ui.theme.AppTheme

@Composable
fun RegisterScreen(
    state: RegisterState,
    onEvent : (RegisterEvent) -> Unit,
    onNavigateToLogin: () -> Unit
) {

    val imeState by rememberImeState()
    var visible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current



    LaunchedEffect(key1 = state.isSuccess) {
        if (state.isSuccess) {
            Toast.makeText(context, "Verification email sent. Please check your inbox.", Toast.LENGTH_SHORT).show()
            onNavigateToLogin.invoke()
        }
    }
    LaunchedEffect(key1 = state.errorMessage) {
        if (state.errorMessage != null) {
            Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
    Surface() {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
        ) {


            //Header Section
            Spacer(modifier = Modifier.height(40.dp))
            BackButton(modifier = Modifier.size(55.dp)) {
                onNavigateToLogin.invoke()
            }
            AnimatedVisibility (!imeState) {
                Column (modifier = Modifier
                    .fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.CenterHorizontally)
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
                text = "Register",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Enter your Personal Information",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                ),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(20.dp))


            // Form Section

            // Form Section

            Spacer(modifier = Modifier.height(5.dp))

            MyTextField(
                value = state.name,
                onValueChange = {
                    onEvent(RegisterEvent.UserNameChanged(it))
                },
                placeholder = "Enter your name",
                isError = state.nameError != null,
                label = "Username",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            AnimatedVisibility(
                visible = state.nameError != null,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 10.dp)
            ) {
                Text(
                    text = state.nameError ?: "",
                    color = Color.Red,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            MyTextField(
                value = state.email,
                onValueChange = {
                    onEvent(RegisterEvent.EmailChanged(it))
                },
                placeholder = "Enter your email",
                isError = state.emailError != null,
                label = "Email",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
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
                    onEvent(RegisterEvent.PasswordChanged(it))
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
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
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
            Spacer(modifier = Modifier.height(20.dp))



            PasswordTextField(
                value = state.confirmPassword,
                onValueChange = {
                    onEvent(RegisterEvent.ConfirmPasswordChanged(it))
                },
                placeholder = "Enter confirm password",
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
                isError = state.confirmPasswordError != null,
                label = "Confirm Password",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            AnimatedVisibility(
                visible = state.confirmPasswordError != null,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 10.dp)
            ) {
                Text(
                    text = state.confirmPasswordError ?: "",
                    color = Color.Red,
                )
            }


            Spacer(modifier = Modifier.height(30.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                onClick = {
                    focusManager.clearFocus()
                    onEvent(RegisterEvent.Register)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                }
                else{
                    Text(text = "Register", modifier = Modifier.padding(vertical = 10.dp))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom section with text and register link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Already have an account?", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.clickable {
                        onNavigateToLogin.invoke()
                    }
                )
            }

            if (imeState){
                Spacer(modifier = Modifier.height(350.dp))
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    AppTheme {
        RegisterScreen(
            state = RegisterState(),
            onEvent = {},
            onNavigateToLogin = {},
        )
    }

}