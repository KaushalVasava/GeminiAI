package com.lahsuak.apps.geminiai.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lahsuak.apps.geminiai.data.model.ChatMessageEntity
import com.lahsuak.apps.geminiai.data.model.GroupEntity
import com.lahsuak.apps.geminiai.repo.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GroupViewModel(
    private val chatRepository: ChatRepository,
) : ViewModel() {
    private val _groupFlow = MutableStateFlow<List<GroupEntity>>(emptyList())
    val groupFlow: StateFlow<List<GroupEntity>> = _groupFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.getAllGroups().collectLatest {
                _groupFlow.value = it
            }
        }
    }

    fun addChatToGroup(groupId: String, list: List<ChatMessageEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.insertChatsInGroup(
                GroupEntity(
                    id = groupId,
                    list
                )
            )
        }
    }
}