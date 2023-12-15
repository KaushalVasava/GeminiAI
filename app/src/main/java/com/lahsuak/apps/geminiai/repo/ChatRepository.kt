package com.lahsuak.apps.geminiai.repo

import com.lahsuak.apps.geminiai.data.model.ChatMessageEntity
import com.lahsuak.apps.geminiai.ui.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun getAllChatMessages() : Flow<List<ChatMessage>>

    suspend fun insertSingleMessage(
        chatMessageEntity: ChatMessageEntity
    )

    suspend fun clearChat()
}