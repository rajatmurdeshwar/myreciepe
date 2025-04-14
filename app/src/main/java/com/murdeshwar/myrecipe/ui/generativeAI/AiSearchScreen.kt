package com.murdeshwar.myrecipe.ui.generativeAI

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiSearchScreen(sheetState: SheetState, onDismiss: () -> Unit, viewModel: AiSearchViewModel) {
    var textFieldValue by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val chatMessages = remember { mutableStateListOf<Pair<String, Boolean>>() }
    var lastResponse by remember { mutableStateOf<String?>(null) }


    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(min = 200.dp, max = 600.dp)
        ) {

            // LazyColumn is hidden if no messages exist
            if (chatMessages.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .animateContentSize(), // Smooth expansion
                    reverseLayout = true
                ) {
                    items(chatMessages.size) { id ->
                        ChatBubble(text = chatMessages[id].first, isUser = chatMessages[id].second)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    label = { Text("Enter text", color = MaterialTheme.colorScheme.onSurface) },
                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (textFieldValue.isNotBlank()) {
                            chatMessages.add(0,textFieldValue to true) // Add user input
                            viewModel.sendPrompt( textFieldValue)
                            textFieldValue = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Send")
                }
            }

            when (uiState) {
                is UiState.Loading ->  CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                is UiState.Success -> {
                    val responseText = (uiState as UiState.Success).outputText
                    if (responseText != lastResponse) { // Prevent duplicate entries
                        chatMessages.add(0, responseText to false)
                        lastResponse = responseText
                    }
                }
                is UiState.Error -> {
                    val errorMessage = (uiState as UiState.Error).errorMessage
                }
                else -> {}
            }
        }
    }
}



