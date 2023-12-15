package com.lahsuak.apps.geminiai.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lahsuak.apps.geminiai.data.converter.Converters
import com.lahsuak.apps.geminiai.data.dao.ChatDao
import com.lahsuak.apps.geminiai.data.model.ChatMessageEntity

@Database(
    entities = [ChatMessageEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    Converters::class
)
abstract class ChatDatabase : RoomDatabase() {
    abstract val chatDao: ChatDao

    companion object {
        private const val DB_NAME = "chat_database.db"

        @Volatile
        private var INSTANCE: ChatDatabase? = null

        fun getInstance(context: Context): ChatDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context).also { INSTANCE = it }
            }

        private fun build(context: Context) =
            Room.databaseBuilder(context.applicationContext, ChatDatabase::class.java, DB_NAME)
                .build()
    }
}
