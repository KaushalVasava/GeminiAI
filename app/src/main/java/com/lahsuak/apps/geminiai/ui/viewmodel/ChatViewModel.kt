package com.lahsuak.apps.geminiai.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.type.asTextOrNull
import com.lahsuak.apps.geminiai.data.mapper.toChatMessageEntity
import com.lahsuak.apps.geminiai.repo.ChatRepository
import com.lahsuak.apps.geminiai.repo.GeminiAIRepo
import com.lahsuak.apps.geminiai.ui.model.ChatMessage
import com.lahsuak.apps.geminiai.ui.model.states.ChatUiState
import com.lahsuak.apps.geminiai.ui.model.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    geminiAIRepo: GeminiAIRepo,
    private val chatRepository: ChatRepository,
) : ViewModel() {
    private val generativeModel = geminiAIRepo.getGenerativeModel(geminiAIRepo.provideConfig())
    private val chat =
        generativeModel.startChat()

    private val _uiState: MutableStateFlow<ChatUiState> =
        MutableStateFlow(ChatUiState(chat.history.map { content ->
            // Map the initial messages
            ChatMessage(
                text = content.parts.first().asTextOrNull() ?: "",
                participant = if (content.role == "user") Role.YOU else Role.GEMINI,
                isPending = false
            )
        }))
    val uiState: StateFlow<ChatUiState> =
        _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            chatRepository.getAllChatMessages().collect {
                _uiState.value = ChatUiState(it)
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            chatRepository.clearChat()
            _uiState.value = ChatUiState()
        }
    }
    fun sendMessage(userMessage: String) {
        // Add a pending message

        val record = ChatMessage(
            text = userMessage,
            participant = Role.YOU,
            isPending = true
        )



        viewModelScope.launch {

            chatRepository.insertSingleMessage(
                record.toChatMessageEntity()
            )

            try {
                val response = chat.sendMessage(userMessage)

                _uiState.value.replaceLastPendingMessage()

                response.text?.let { modelResponse ->
                    /* _uiState.value.addMessage(
                         ChatMessage(
                             text = modelResponse,
                             role = Role.AI,
                             isPending = false
                         )
                     )*/

                    val newMessage = record.apply { isPending = false }
//                    Log.e(TAG, "sendMessage: $newMessage")
                    chatRepository.insertSingleMessage(newMessage.toChatMessageEntity())
                    chatRepository.insertSingleMessage(
                        ChatMessage(
                            text = modelResponse,
                            participant = Role.GEMINI,
                            isPending = false
                        ).toChatMessageEntity()
                    )
                }
            } catch (e: Exception) {
                _uiState.value.replaceLastPendingMessage()
                _uiState.value.addMessage(
                    ChatMessage(
                        text = e.localizedMessage,
                        participant = Role.ERROR
                    )
                )
            }
        }
    }

}