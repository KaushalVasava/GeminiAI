package com.lahsuak.apps.geminiai.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lahsuak.apps.geminiai.R
import com.lahsuak.apps.geminiai.ui.model.Role
import com.lahsuak.apps.geminiai.ui.viewmodel.GroupViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
        if (list.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "null",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    "Start a new chat",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValue)
                    .padding(top = 16.dp, bottom = 16.dp)
            ) {
                val groupItems = list.sortedByDescending {
                    it.chats.last().time!!.time
                }.groupBy {
                    getDate(it.chats.last().time!!.time)
                }
                groupItems.forEach { (date, items) ->
                    stickyHeader {
                        Text(
                            date,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 12.dp),
                        )
                    }
                    items(items, key = { item -> item.id }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    onItemClicked("chat", it.id)
                                }
                        ) {
                            Text(
                                text = it.chats.lastOrNull {
                                    it.role == Role.YOU
                                }?.text ?: "",
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Text(
                                text = it.chats.lastOrNull {
                                    it.role == Role.GEMINI
                                }?.text?.take(100) ?: "",
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getDate(date: Long): String {
    val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
    return sdf.format(date)
}