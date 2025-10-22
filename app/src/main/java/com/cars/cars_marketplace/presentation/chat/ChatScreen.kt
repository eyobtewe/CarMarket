package com.cars.cars_marketplace.presentation.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.domain.model.ChatMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, viewModel: ChatViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val recommendedCars by viewModel.recommendedCars.collectAsState()
    val isTyping by viewModel.isTyping.collectAsState()
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
        // Messages list
        LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages, key = { it.id }) { message -> ChatMessageBubble(message = message) }

            // Show recommended cars if available
            if (recommendedCars.isNotEmpty()) {
                item {
                    Text(
                            text = "Recommended Cars:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(recommendedCars, key = { it.id }) { car ->
                    RecommendedCarCard(
                            car = car,
                            onClick = { carId -> navController.navigate("detail/$carId") }
                    )
                }
            }
        }

        // Typing indicator
        if (isTyping) {
            Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("AI is typing...")
            }
        }

        // Input field
        Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    label = { Text("Ask about cars...") },
                    modifier = Modifier.weight(1f),
                    enabled = uiState !is ChatUiState.Loading
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            viewModel.sendMessage(messageText)
                            messageText = ""
                        }
                    },
                    enabled = messageText.isNotBlank() && uiState !is ChatUiState.Loading
            ) { Text("Send") }
        }

        if (uiState is ChatUiState.Error) {
            val msg = (uiState as ChatUiState.Error).message
            Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = msg, color = MaterialTheme.colorScheme.error)
                TextButton(onClick = { viewModel.retryLast() }) { Text("Retry") }
            }
        }
    }
}

@Composable
fun ChatMessageBubble(message: ChatMessage) {
    Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
                modifier = Modifier.widthIn(max = 280.dp),
                colors =
                        CardDefaults.cardColors(
                                containerColor =
                                        if (message.isUser) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surfaceVariant
                        )
        ) {
            Text(
                    text = message.content,
                    modifier = Modifier.padding(12.dp),
                    color =
                            if (message.isUser) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun RecommendedCarCard(car: Car, onClick: (String) -> Unit) {
    Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            onClick = { onClick(car.id) }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                    text = "${car.make} ${car.model} (${car.year})",
                    style = MaterialTheme.typography.titleMedium
            )
            Text(
                    text = "$${car.price}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
            )
            if (car.description != null) {
                Text(
                        text = car.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2
                )
            }
        }
    }
}
