package com.lahsuak.apps.geminiai.repo

import com.lahsuak.apps.geminiai.data.db.ChatDatabase
import com.lahsuak.apps.geminiai.data.model.ChatMessageEntity
import com.lahsuak.apps.geminiai.data.mapper.toChatMessage
import com.lahsuak.apps.geminiai.ui.model.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepositoryImpl(
    db: ChatDatabase,
) : ChatRepository {

    private val dao = db.chatDao

    override fun getAllChatMessages(): Flow<List<ChatMessage>> {
        return dao.getAllChatMessage().map { it.map { msg -> msg.toChatMessage() } }
    }

    override suspend fun insertSingleMessage(chatMessageEntity: ChatMessageEntity) {
        dao.insertSingleMessage(chatMessageEntity)
    }

    override suspend fun clearChat() {
        dao.deleteAllChatMessages()
    }
}