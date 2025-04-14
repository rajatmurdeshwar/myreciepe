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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.murdeshwar.myrecipe.R
import com.murdeshwar.myrecipe.data.source.LoginUser
import com.murdeshwar.myrecipe.data.source.User
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LoginScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onSignUpSuccess: () -> Unit,
    isOfflineState: StateFlow<Boolean>
) {

    val uiState by userViewModel.uiState.collectAsState()
    var isLoginScreen by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }

    val isOffline by isOfflineState.collectAsStateWithLifecycle()

    // If user is not connected to the internet show a snack bar to inform them.
    val notConnectedMessage = stringResource(R.string.not_connected)
    LaunchedEffect(isOffline) {
        if (isOffline) {
            snackbarHostState.showSnackbar(
                message = notConnectedMessage,
                duration = SnackbarDuration.Indefinite,
            )
        }
    }

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
                LoginComposable{ loginUser -> userViewModel.login(loginUser) }
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
    modifier: Modifier = Modifier,
    onSignUpSubmitClick: (User) -> Unit,
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
        modifier = modifier.fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
            label = { Text("Name", color = MaterialTheme.colorScheme.onSurface) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            value = name,
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
            onValueChange = {
                name = it
                nameError = when {
                    it.isEmpty() -> "Name cannot be empty"
                    else -> null
                }
            },
            isError = nameError != null,
            supportingText = { nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
            label = { Text("Phone", color = MaterialTheme.colorScheme.onSurface) },
            value = phone,
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
            onValueChange = {
                phone = it
                phoneError = when {
                    it.isEmpty() -> "Phone cannot be empty"
                    it.length != 10 || !it.all { char -> char.isDigit() } -> "Enter a valid 10-digit phone number"
                    else -> null
                }
            },
            isError = phoneError != null,
            supportingText = { phoneError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
            label = { Text("City", color = MaterialTheme.colorScheme.onSurface) },
            value = city,
            leadingIcon = { Icon(Icons.Default.Map, contentDescription = null) },
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
            onValueChange = {
                city = it
                cityError = when {
                    it.isEmpty() -> "City cannot be empty"
                    else -> null
                }
            },
            isError = cityError != null,
            supportingText = { cityError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
            label = { Text("Email", color = MaterialTheme.colorScheme.onSurface) },
            value = email,
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
            onValueChange = {
                email = it
                emailError = when {
                    it.isEmpty() -> "Email cannot be empty"
                    !Patterns.EMAIL_ADDRESS.matcher(it).matches() -> "Enter a valid email address"
                    else -> null
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError != null,
            supportingText = { emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
            label = { Text("Password", color = MaterialTheme.colorScheme.onSurface) },
            value = password,
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
            onValueChange = {
                password = it
                passwordError = when {
                    it.isEmpty() -> "Password cannot be empty"
                    it.length < 8 -> "Password must be at least 8 characters"
                    else -> null
                }
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError != null,
            supportingText = { passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        Button(
            modifier = Modifier.padding(top = 8.dp),
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
    modifier: Modifier = Modifier,
    onLoginSubmitClick: (LoginUser) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth().padding(vertical = 8.dp),
            label = { Text("Email", color = MaterialTheme.colorScheme.onSurface) },
            leadingIcon = { Icon(Icons.Filled.Email, null) },
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
            value = username,
            onValueChange = {
                username = it
                usernameError = when {
                    it.isEmpty() -> "Email cannot be empty"
                    !Patterns.EMAIL_ADDRESS.matcher(it).matches() -> "Enter a valid email address"
                    else -> null
                }
            },
            isError = usernameError != null,
            supportingText = {
                usernameError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        )

        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text("Password", color = MaterialTheme.colorScheme.onSurface) },
            leadingIcon = { Icon(Icons.Filled.Lock, null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible}) {
                    val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    Icon(imageVector = icon, contentDescription = "Toggle password visibility")

                }
            },
            value = password,
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
            onValueChange = {
                password = it
                passwordError = when {
                    it.isEmpty() -> "Password cannot be empty"
                    it.length < 8 -> "Password must be at least 8 characters"
                    else -> null
                }
            },
            isError = passwordError != null,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            supportingText = {
                passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        )

        Button(
            modifier = modifier.padding(top = 16.dp),
            onClick = {
                if (usernameError == null && passwordError == null) {
                    isLoading = false
                    val loginUser = LoginUser(username, password)
                    onLoginSubmitClick(loginUser)
                }
            },
            enabled = usernameError == null && passwordError == null
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = modifier.size(24.dp))
            } else {
                Text(text = "Log In", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
