package com.murdeshwar.myrecipe.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.murdeshwar.myrecipe.R
import com.murdeshwar.myrecipe.data.source.LoginUser
import com.murdeshwar.myrecipe.data.source.User

@Composable
fun LoginScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onSignUpSuccess: () -> Unit ){

    val uiState by userViewModel.uiState.collectAsState()
    var isLoginScreen by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle UI Events
    LaunchedEffect(uiState) {
        when (uiState) {
            is UserViewModel.UIEvent.Success -> {
                snackbarHostState.showSnackbar((uiState as UserViewModel.UIEvent.Success).message)
                userViewModel.resetUIState() // Reset state
                if (isLoginScreen) onLoginSuccess() else onSignUpSuccess() // Trigger navigation
            }
            is UserViewModel.UIEvent.Error -> {
                snackbarHostState.showSnackbar((uiState as UserViewModel.UIEvent.Error).message)
                userViewModel.resetUIState() // Reset state
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(0.5f))

            Image(
                painter = painterResource(id = R.drawable.home_app),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .wrapContentSize(Alignment.Center),
                text = "EzeTap",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.weight(0.5f))

            if (isLoginScreen) {
                LoginComposable { loginUser -> userViewModel.login(loginUser) }
            } else {
                SignUpComposable { user -> userViewModel.signup(user) }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            TextButton(onClick = { isLoginScreen = !isLoginScreen }) {
                Text(text = if (isLoginScreen) "Don't have an account? Sign Up" else "Already have an account? Log In")

            }
            Spacer(modifier = Modifier.weight(0.5f))

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpComposable(
    onSignUpSubmitClick: (User) -> Unit) {

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            label = { Text("Name") },
            value = name,
            onValueChange = {name = it},
            singleLine = true)
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            label = { Text("Phone") },
            value = phone,
            onValueChange = {phone = it},
            singleLine = true)
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            label = { Text("City") },
            value = city,
            onValueChange = {city = it},
            singleLine = true)
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            label = { Text("Email") },
            value = email,
            onValueChange = {email = it},
            singleLine = true)
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            label = { Text("Password") },
            value = password,
            onValueChange = {password = it},
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible}) {
                    Icon(imageVector = if (passwordVisible) Icons.Filled.Lock else Icons.Outlined.Lock,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )

                }
            })
        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                val user = User(name,phone,city,email, password)

                onSignUpSubmitClick(user)
            }) {
            Text(text = "Submit")
        }

    }

}

@Composable
fun LoginComposable(
    onLoginSubmitClick: (LoginUser) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text("Username") },
            leadingIcon = { Icon(Icons.Filled.Person, null) },
            value = username,
            onValueChange = { username = it }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Filled.Lock ,null) },
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                val loginUser = LoginUser(username,password)
                onLoginSubmitClick(loginUser)
            }
        ) {
            Text(text = "Log In")
        }

    }

}

@Preview
@Composable
private fun LoginScreenPreview() {

}