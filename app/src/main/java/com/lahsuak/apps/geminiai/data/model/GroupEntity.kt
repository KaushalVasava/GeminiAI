package com.lahsuak.apps.geminiai.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "group_table")
@TypeConverters(ChatMessageConverters::class)
data class GroupEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val chats: List<ChatMessageEntity>,
)

class ChatMessageConverters {
    private var gson: Gson = Gson()

    @TypeConverter
    fun stringToChatMessageList(data: String): List<ChatMessageEntity> {
        val listType = object : TypeToken<List<ChatMessageEntity>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun chatMessageListToString(someObjects: List<ChatMessageEntity>): String {
        return gson.toJson(someObjects)
    }
}