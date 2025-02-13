package com.murdeshwar.myrecipe.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun RecipeProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val user by profileViewModel.userDetails.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        user?.let {
            Text(text = "Name: ${it.name}")
            Text(text = "Email: ${it.email}")
            Text(text = "City: ${it.city ?: "N/A"}")
            Text(text = "Phone: ${it.phonenumber ?: "N/A"}")
        } ?: run {
            Text(text = "Loading...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
