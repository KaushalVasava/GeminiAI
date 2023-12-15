package com.lahsuak.apps.geminiai.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lahsuak.apps.geminiai.ui.model.Role
import java.util.Date

@Entity(tableName = "chat_table")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val role: Role,
    val text: String,
    val time: Date?,
    val isMine: Boolean,
    val isFavorite: Boolean,
    val isPending : Boolean,
)