package com.lahsuak.apps.geminiai.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lahsuak.apps.geminiai.ui.model.Role
import java.util.Date

@Entity(tableName = "chat_table")
@TypeConverters(ListConverters::class)
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val role: Role,
    val text: String,
    val time: Date?,
    val isMine: Boolean,
    val isFavorite: Boolean,
    val isPending: Boolean,
    val imageUris: List<String>,
)

class ListConverters {
    private var gson: Gson = Gson()
    @TypeConverter
    fun stringToSomeObjectList(data: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<String>): String {
        return gson.toJson(someObjects)
    }
}