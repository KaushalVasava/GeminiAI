package com.lahsuak.apps.geminiai.data.mapper

import com.lahsuak.apps.geminiai.data.model.ChatMessageEntity
import com.lahsuak.apps.geminiai.ui.model.ChatMessage
import java.util.Date


fun ChatMessageEntity.toChatMessage(): ChatMessage {
    return ChatMessage(
        isPending = isPending,
        text = text,
        participant = role,
        id = id
    )
}

fun ChatMessage.toChatMessageEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        id = id,
        text = text,
        role = participant,
        time = Date(),
        isMine = false,
        isPending = isPending,
        isFavorite = false
    )
}