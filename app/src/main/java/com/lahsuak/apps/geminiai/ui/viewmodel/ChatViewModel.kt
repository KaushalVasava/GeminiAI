package com.lahsuak.apps.geminiai.ui.viewmodel

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Precision
import com.google.ai.client.generativeai.type.content
import com.lahsuak.apps.geminiai.data.mapper.toChatMessageEntity
import com.lahsuak.apps.geminiai.repo.ChatRepository
import com.lahsuak.apps.geminiai.repo.GeminiAIRepo
import com.lahsuak.apps.geminiai.ui.model.ChatMessage
import com.lahsuak.apps.geminiai.ui.model.Role
import com.lahsuak.apps.geminiai.ui.model.states.ChatUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(
    private val geminiAIRepo: GeminiAIRepo,
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val generativeModel = geminiAIRepo.getGenerativeModel(
        "gemini-pro",
        geminiAIRepo.provideConfig()
    )
    private val chat = generativeModel.startChat()

    private val _uiState: MutableStateFlow<ChatUiState> =
        MutableStateFlow(ChatUiState(emptyList()))
    val uiState: StateFlow<ChatUiState> = _uiState
    
    fun fetchChats(groupId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.getAllChatMessages(groupId).collect {
                _uiState.value = ChatUiState(it)
            }
        }
    }

    fun deleteChat(groupId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.deleteChats(groupId)
            _uiState.value = ChatUiState()
        }
    }

    fun sendMessage(
        context: Context,
        userMessage: String,
        groupId: String,
        selectedImages: List<String>,
    ) {
        // Add a pending message
        viewModelScope.launch(Dispatchers.IO) {
            val imageRequestBuilder = ImageRequest.Builder(context)
            val imageLoader = ImageLoader.Builder(context).build()
            val bitmaps = selectedImages.mapNotNull {
                val imageRequest = imageRequestBuilder
                    .data(it)
                    // Scale the image down to 768px for faster uploads
                    .size(size = 768)
                    .precision(Precision.EXACT)
                    .build()
                try {
                    val result = imageLoader.execute(imageRequest)
                    if (result is SuccessResult) {
                        return@mapNotNull (result.drawable as BitmapDrawable).bitmap
                    } else {
                        return@mapNotNull null
                    }
                } catch (e: Exception) {
                    return@mapNotNull null
                }
            }
            val record = ChatMessage(
                text = userMessage,
                participant = Role.YOU,
                isPending = true,
                imageUris = selectedImages,
                id = groupId
            )

            chatRepository.insertSingleMessage(record.toChatMessageEntity())
            try {
                if (selectedImages.isEmpty()) {
                    val response = chat.sendMessage(userMessage)
                    _uiState.value.replaceLastPendingMessage()
                    response.text?.let { modelResponse ->
                        val newMessage = record.apply { isPending = false }
                        chatRepository.insertSingleMessage(newMessage.toChatMessageEntity())
                        chatRepository.insertSingleMessage(
                            ChatMessage(
                                text = modelResponse,
                                participant = Role.GEMINI,
                                isPending = false,
                                imageUris = emptyList(),
                                id = groupId
                            ).toChatMessageEntity()
                        )
                    }
                }
                else {
                    val prompt =
                        "Look at the image(s), and then answer the following question: $userMessage"

                    val inputContent = content {
                        for (bitmap in bitmaps) {
                            image(bitmap)
                        }
                        text(prompt)
                    }

                    val generativeModel = geminiAIRepo.getGenerativeModel(
                        "gemini-pro-vision",
                        geminiAIRepo.provideConfig()
                    )
                    var outputContent = ""

                    _uiState.value.replaceLastPendingMessage()
                    val newMessage = record.apply { isPending = false }
                    generativeModel.generateContentStream(inputContent)
                        .collectLatest { response ->
                            outputContent += response.text
                            delay(2000)//it is temp I will remove it
                            chatRepository.insertSingleMessage(newMessage.toChatMessageEntity())
                            chatRepository.insertSingleMessage(
                                ChatMessage(
                                    text = outputContent,
                                    participant = Role.GEMINI,
                                    isPending = false,
                                    imageUris = emptyList(),
                                    id = groupId
                                ).toChatMessageEntity()
                            )
                        }
                }
            } catch (e: Exception) {
                _uiState.value.replaceLastPendingMessage()
                _uiState.value.addMessage(
                    ChatMessage(
                        text = e.localizedMessage.toString(),
                        participant = Role.ERROR,
                        isPending = false,
                        imageUris = emptyList(),
                        id = groupId
                    )
                )
            }
        }
    }
}