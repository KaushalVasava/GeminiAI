package com.lahsuak.apps.geminiai.data.dao

import androidx.room.*
import com.lahsuak.apps.geminiai.data.model.ChatMessageEntity
import com.lahsuak.apps.geminiai.data.model.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Upsert
//        (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatsInGroup(group: GroupEntity)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateChatsInGroup(group: GroupEntity)

    @Query("SELECT * FROM group_table")
    fun getAllGroups(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM group_table WHERE id=:groupId")
    suspend fun getById(groupId: Int): GroupEntity

    @Query("DELETE FROM group_table")
    suspend fun deleteAllGroups()

    @Upsert
    suspend fun insertSingleMessage(chatMessage: ChatMessageEntity)

    @Query("DELETE FROM chat_table")
    suspend fun deleteAllChatMessages()

    @Query("SELECT * FROM chat_table WHERE id=:id")
    fun getAllChatMessage(id: String): Flow<List<ChatMessageEntity>>
}