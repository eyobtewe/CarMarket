package com.cars.cars_marketplace.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.domain.model.ChatMessage
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.usecase.SendChatMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed interface ChatUiState {
    object Idle : ChatUiState
    object Loading : ChatUiState
    data class Error(val message: String) : ChatUiState
}

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendChatMessageUseCase: SendChatMessageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Idle)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _recommendedCars = MutableStateFlow<List<Car>>(emptyList())
    val recommendedCars: StateFlow<List<Car>> = _recommendedCars.asStateFlow()

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()

    private var lastFailedMessage: String? = null

    fun sendMessage(text: String) {
        // Add user message immediately
        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            content = text,
            isUser = true,
            timestamp = System.currentTimeMillis()
        )
        _messages.value = _messages.value + userMessage

        // Send to AI and get response
        viewModelScope.launch {
            _uiState.value = ChatUiState.Loading
            _isTyping.value = true
            
            val result = sendChatMessageUseCase(text)
            when (result) {
                is Resource.Success -> {
                    lastFailedMessage = null
                    val aiMessage = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        content = result.data.message,
                        isUser = false,
                        timestamp = System.currentTimeMillis()
                    )
                    _messages.value = _messages.value + aiMessage
                    
                    // Update recommended cars if any
                    if (result.data.recommendedCars.isNotEmpty()) {
                        // Convert SimpleCar to Car (with minimal data)
                        val cars = result.data.recommendedCars.map { simpleCar ->
                            Car(
                                id = simpleCar.id,
                                make = simpleCar.make,
                                model = simpleCar.model,
                                year = 0, // Not available in SimpleCar
                                price = 0.0, // Not available in SimpleCar
                                description = "Recommended by AI"
                            )
                        }
                        _recommendedCars.value = cars
                    }
                    
                    _uiState.value = ChatUiState.Idle
                    _isTyping.value = false
                }
                is Resource.Error -> {
                    lastFailedMessage = text
                    val errorMessage = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        content = "Sorry, I couldn't process your request. Please try again.",
                        isUser = false,
                        timestamp = System.currentTimeMillis()
                    )
                    _messages.value = _messages.value + errorMessage
                    _uiState.value = ChatUiState.Error(result.message ?: "Unknown error")
                    _isTyping.value = false
                }
                else -> {
                    _uiState.value = ChatUiState.Error("Unknown state")
                    _isTyping.value = false
                }
            }
        }
    }

    fun retryLast() {
        val last = lastFailedMessage ?: return
        lastFailedMessage = null
        sendMessage(last)
    }
}