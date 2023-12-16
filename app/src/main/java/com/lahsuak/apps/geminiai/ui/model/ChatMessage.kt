package com.lahsuak.apps.geminiai.ui.model

import java.util.UUID

enum class Role {
    YOU, GEMINI, ERROR
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    var text: String = "",
    val participant: Role = Role.YOU,
    var isPending: Boolean = false
)
