package com.lahsuak.apps.geminiai.data.dao

import androidx.room.*
import com.lahsuak.apps.geminiai.data.model.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Upsert
    suspend fun insertSingleMessage(chatMessage: ChatMessageEntity)

    @Query("DELETE FROM chat_table")
    suspend fun deleteAllChatMessages()

    @Query("SELECT * FROM chat_table")
    fun getAllChatMessage(): Flow<List<ChatMessageEntity>>
}