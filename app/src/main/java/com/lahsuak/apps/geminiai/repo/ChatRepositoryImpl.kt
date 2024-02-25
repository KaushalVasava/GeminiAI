package com.lahsuak.apps.geminiai.repo

import com.lahsuak.apps.geminiai.data.db.ChatDatabase
import com.lahsuak.apps.geminiai.data.model.ChatMessageEntity
import com.lahsuak.apps.geminiai.data.mapper.toChatMessage
import com.lahsuak.apps.geminiai.data.model.GroupEntity
import com.lahsuak.apps.geminiai.ui.model.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepositoryImpl(
    db: ChatDatabase,
) : ChatRepository {

    private val dao = db.chatDao
    override fun getAllGroups(): Flow<List<GroupEntity>> {
        return dao.getAllGroups()
    }

    override suspend fun insertChatsInGroup(groupEntity: GroupEntity) {
        dao.insertChatsInGroup(groupEntity)
    }

    override suspend fun updateChatsInGroup(groupEntity: GroupEntity) {
        dao.updateChatsInGroup(groupEntity)
    }

    override suspend fun getById(groupId: Int): GroupEntity {
        return dao.getById(groupId)
    }

    override fun getAllChatMessages(id: String): Flow<List<ChatMessage>> {
        return dao.getAllChatMessage(id).map { it.map { msg -> msg.toChatMessage() } }
    }

    override suspend fun insertSingleMessage(chatMessageEntity: ChatMessageEntity) {
        dao.insertSingleMessage(chatMessageEntity)
    }

    override suspend fun deleteChats(groupId: String) {
        dao.deleteChatMessages(groupId)
    }
}