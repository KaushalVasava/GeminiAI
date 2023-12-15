package com.lahsuak.apps.geminiai.ui.model

import java.util.UUID

enum class Role {
    USER, MODEL, ERROR
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    var text: String = "",
    val participant: Role = Role.USER,
    var isPending: Boolean = false
)
