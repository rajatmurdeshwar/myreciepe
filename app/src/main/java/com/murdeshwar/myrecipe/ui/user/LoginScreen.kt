package com.murdeshwar.myrecipe.ui.user

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
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
    onSignUpSuccess: () -> Unit ) {

    val uiState by userViewModel.uiState.collectAsState()
    var isLoginScreen by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle UI Events
    LaunchedEffect(uiState) {
        when (uiState) {
            is UserViewModel.UIEvent.Success -> {
                snackbarHostState.showSnackbar((uiState as UserViewModel.UIEvent.Success).message)
                userViewModel.saveLoginState(true)
                if (isLoginScreen) onLoginSuccess() else onSignUpSuccess() // Trigger navigation
                userViewModel.resetUIState() // Reset state
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
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(0.5f))

            Image(
                painter = painterResource(id = R.drawable.recipe_book_00),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .wrapContentSize(Alignment.Center),
                text = "Recipe Maker",
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



            TextButton(onClick = { isLoginScreen = !isLoginScreen }) {
                Text(
                    text = if (isLoginScreen) "Don't have an account? Sign Up" else "Already have an account? Log In",
                    color = MaterialTheme.colorScheme.onSurface)

            }


        }
    }
}

@Composable
fun SignUpComposable(
    onSignUpSubmitClick: (User) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var cityError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            label = { Text("Name", color = MaterialTheme.colorScheme.onSurface) },
            value = name,
            onValueChange = {
                name = it
                nameError = if (it.isEmpty()) "Name cannot be empty" else null
            },
            singleLine = true,
            isError = nameError != null,
            supportingText = { nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            label = { Text("Phone", color = MaterialTheme.colorScheme.onSurface) },
            value = phone,
            onValueChange = {
                phone = it
                phoneError = if (it.length != 10 || !it.all { char -> char.isDigit() }) "Enter a valid 10-digit phone number" else null
            },
            singleLine = true,
            isError = phoneError != null,
            supportingText = { phoneError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            label = { Text("City", color = MaterialTheme.colorScheme.onSurface) },
            value = city,
            onValueChange = {
                city = it
                cityError = if (it.isEmpty()) "City cannot be empty" else null
            },
            singleLine = true,
            isError = cityError != null,
            supportingText = { cityError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            label = { Text("Email", color = MaterialTheme.colorScheme.onSurface) },
            value = email,
            onValueChange = {
                email = it
                emailError = if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) "Enter a valid email address" else null
            },
            singleLine = true,
            isError = emailError != null,
            supportingText = { emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            label = { Text("Password", color = MaterialTheme.colorScheme.onSurface) },
            value = password,
            onValueChange = {
                password = it
                passwordError = if (it.length < 8) "Password must be at least 8 characters" else null
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Lock else Icons.Outlined.Lock,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            isError = passwordError != null,
            supportingText = { passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                val user = User(name, phone, city, email, password)
                onSignUpSubmitClick(user)
            },
            enabled = nameError == null && phoneError == null && cityError == null && emailError == null && passwordError == null
        ) {
            Text(text = "Submit")
        }
    }
}


@Composable
fun LoginComposable(
    onLoginSubmitClick: (LoginUser) -> Unit
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text("Username", color = MaterialTheme.colorScheme.onSurface) },
            leadingIcon = { Icon(Icons.Filled.Person, null) },
            value = username,
            onValueChange = {
                username = it
            usernameError = if (it.isEmpty()) {
                    "Username cannot be empty"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                    "Enter a valid email address"
                } else {
                    null
                }
            },
            isError = usernameError != null,
            supportingText = {
                if (usernameError != null) {
                    Text(usernameError!!, color = MaterialTheme.colorScheme.error)
                }
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text("Password", color = MaterialTheme.colorScheme.onSurface) },
            leadingIcon = { Icon(Icons.Filled.Lock ,null) },
            value = password,
            onValueChange = {
                password = it
                passwordError = if (it.length < 8) {
                    "Password must be at least 8 characters"
                } else {
                    null
                }
                            },
            isError = passwordError != null,
            visualTransformation = PasswordVisualTransformation(),
            supportingText = {
                if (passwordError != null) {
                    Text(passwordError!!, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                if (usernameError == null && passwordError == null) {
                    val loginUser = LoginUser(username, password)
                    onLoginSubmitClick(loginUser)
                }
            },
            enabled = usernameError == null && passwordError == null
        ) {
            Text(text = "Log In")
        }

    }

}

@Preview
@Composable
private fun LoginScreenPreview() {

}