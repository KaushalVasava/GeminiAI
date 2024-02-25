package com.lahsuak.apps.geminiai.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lahsuak.apps.geminiai.R
import com.lahsuak.apps.geminiai.ui.model.Role
import com.lahsuak.apps.geminiai.ui.viewmodel.GroupViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    groupViewModel: GroupViewModel,
    onItemClicked: (routeId: String, groupId: String) -> Unit = { _, _ -> },
) {
    val list by groupViewModel.groupFlow.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onItemClicked("chat", UUID.randomUUID().toString())
            }) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    ) { paddingValue ->
        LazyColumn(
            Modifier
                .padding(paddingValue)
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            items(list.reversed()) {
                Card(
                    modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                    onClick = {
                        onItemClicked("chat", it.id)
                    }) {
                    Text(
                        text = it.chats.lastOrNull{
                            it.role == Role.YOU
                        }?.text ?: "",
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}