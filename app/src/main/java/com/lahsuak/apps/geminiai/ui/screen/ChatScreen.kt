package com.lahsuak.apps.geminiai.ui.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.speech.RecognizerIntent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.ai.sample.util.UriSaver
import com.lahsuak.apps.geminiai.R
import com.lahsuak.apps.geminiai.ui.component.AnimatedBorderCard
import com.lahsuak.apps.geminiai.ui.component.RoundedTextField
import com.lahsuak.apps.geminiai.ui.model.ChatMessage
import com.lahsuak.apps.geminiai.ui.model.Role
import com.lahsuak.apps.geminiai.ui.viewmodel.ChatViewModel
import com.lahsuak.apps.geminiai.util.setClipboard
import com.lahsuak.apps.geminiai.util.shareText
import com.lahsuak.apps.geminiai.util.speakToAdd
import com.lahsuak.apps.geminiai.util.toCamelCase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatRoute(
    chatViewModel: ChatViewModel,
    onBackPress: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
//    val imageRequestBuilder = ImageRequest.Builder(LocalContext.current)
//    val imageLoader = ImageLoader.Builder(LocalContext.current).build()
    val chatUiState by chatViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    var userMessage by rememberSaveable { mutableStateOf("") }

    val speakLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            userMessage =
                data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0) ?: ""
        }
    }

    var openDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
            },
            confirmButton = {
                Button(onClick = {
                    chatViewModel.clearChat()
                    openDialog = false
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openDialog = false
                }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            title = {
                Text(stringResource(R.string.delete_chat))
            },
            text = { Text(text = stringResource(R.string.confirm_delete)) },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.chat_ai)) },
//                navigationIcon = {
//                    IconButton(onClick = {
//                        onBackPress()
//                    }) {
//                        Icon(Icons.Default.ArrowBack, stringResource(R.string.back))
//                    }
//                },
                actions = {
                    IconButton(onClick = {
                        openDialog = true
                    }) {
                        Icon(Icons.Default.Delete, stringResource(R.string.clear_chat))
                    }
                }
            )
        },
        bottomBar = {
            MessageInput(
                isResultPending = chatUiState.messages.lastOrNull()?.isPending ?: false && chatUiState.messages.lastOrNull()?.participant == Role.YOU,
                onSendMessage = { inputText, selectedItems ->
                    chatViewModel.sendMessage(context, inputText, selectedItems.map {
                        it.toString()
                    })
                },
                speakLauncher = speakLauncher,
                userMessage = userMessage,
                onValueChange = {
                    userMessage = it
                },
                resetScroll = {
                    coroutineScope.launch {
                        listState.scrollToItem(0)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Messages List
            ChatList(chatUiState.messages, listState)
        }
    }
}

@Composable
fun ChatList(
    chatMessages: List<ChatMessage>,
    listState: LazyListState,
) {
    LazyColumn(
        reverseLayout = true,
        state = listState
    ) {
        items(chatMessages.reversed()) { message ->
            ChatBubbleItem(message)
        }
    }
}

@Composable
fun ChatBubbleItem(
    chatMessage: ChatMessage,
) {
    val context = LocalContext.current
    val isGEMINIMessage = chatMessage.participant == Role.GEMINI ||
            chatMessage.participant == Role.ERROR

    val backgroundColor = when (chatMessage.participant) {
        Role.GEMINI -> MaterialTheme.colorScheme.primaryContainer
        Role.YOU -> MaterialTheme.colorScheme.tertiaryContainer
        Role.ERROR -> MaterialTheme.colorScheme.errorContainer
    }

    val bubbleShape = if (isGEMINIMessage) {
        RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
    }

    val horizontalAlignment = if (isGEMINIMessage) {
        Alignment.Start
    } else {
        Alignment.End
    }
    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = chatMessage.participant.name.toCamelCase(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row {
            BoxWithConstraints {
                AnimatedBorderCard(
                    animationDuration = 2000,
                    shape = bubbleShape,
                    containerColor = backgroundColor,
                    modifier = Modifier
                        .padding(1.dp)
                        .widthIn(0.dp, maxWidth * 0.9f),
                    gradient = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background
                        )
                    )
                ) {
                    Column {
                        LazyRow(
                            modifier = Modifier.padding(all = 8.dp)
                        ) {
                            items(chatMessage.imageUris) { imageUri ->
                                Box {
                                    AsyncImage(
                                        model = imageUri,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .requiredSize(72.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = chatMessage.text,
                            modifier = Modifier.padding(16.dp)
                        )
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = {
                                context.shareText(chatMessage.text)
                            }) {
                                Icon(
                                    Icons.Default.Share,
                                    contentDescription = stringResource(R.string.share)
                                )
                            }
                            IconButton(onClick = {
                                context.setClipboard(chatMessage.text)
                            }) {
                                Icon(
                                    painterResource(id = R.drawable.ic_copy),
                                    contentDescription = stringResource(R.string.copy)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MessageInput(
    isResultPending: Boolean,
    onSendMessage: (String, List<Uri>) -> Unit = { _, _ -> },
    speakLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    userMessage: String,
    onValueChange: (String) -> Unit,
    resetScroll: () -> Unit = {},
) {
    val context = LocalContext.current
    val imageUris = rememberSaveable(saver = UriSaver()) { mutableStateListOf() }
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { imageUri ->
        imageUri?.let {
            imageUris.add(it)
        }
    }
    val keyboard = LocalSoftwareKeyboardController.current
    var isMessageSent by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = isMessageSent) {
        imageUris.clear()
        isMessageSent = false
    }
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column {
            LazyRow(
                modifier = Modifier.padding(all = 8.dp)
            ) {
                items(imageUris) { imageUri ->
                    Box {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(4.dp)
                                .requiredSize(72.dp)
                        )
                        Icon(
                            Icons.Default.Close,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(R.string.remove_image),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                                .align(Alignment.TopEnd)
                                .clickable {
                                    imageUris.remove(imageUri)
                                }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundedTextField(
                    value = userMessage,
                    placeholder = { Text(stringResource(R.string.chat_label)) },
                    onValueChange = { onValueChange(it) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // hide keyboard when submit input
                            keyboard?.hide()
                        }
                    ),
                    leadingIcon = {
                        IconButton(
                            onClick = {
                                pickMedia.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .padding(all = 4.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                Icons.Rounded.Add,
                                contentDescription = stringResource(R.string.add_image),
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.85f)
                )
                IconButton(
                    onClick = {
                        if (userMessage.isNotBlank()) {
                            onSendMessage(userMessage, imageUris)
                            onValueChange("")
                            resetScroll()
                            isMessageSent = true
                        } else {
                            context.speakToAdd(speakLauncher)
                        }
                    },
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .fillMaxWidth()
                        .weight(0.15f)
                ) {
                    if (isResultPending) {
                        CircularProgressIndicator()
                    } else {
                        Icon(
                            if (userMessage.isNotBlank()) {
                                painterResource(R.drawable.ic_send)
                            } else {
                                painterResource(R.drawable.ic_voice)
                            },
                            contentDescription = stringResource(R.string.action_send),
                            tint = if (userMessage.isNotBlank())
                                MaterialTheme.colorScheme.surface
                            else
                                MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (userMessage.isNotBlank())
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant
                                )
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CodeText(text: String) {
    val codePattern = Regex("""```[^\n]*\n.*?\n```""", RegexOption.DOT_MATCHES_ALL)
    val matches = codePattern.findAll(text)

    var currentIndex = 0
    val annotatedString = AnnotatedString.Builder()

    for (match in matches) {
        // Append text before the code block
        annotatedString.append(text.substring(currentIndex, match.range.first))

        // Append the code block with a specific style
        annotatedString.pushStyle(SpanStyle(color = Color.Blue))
//        val s = match.value.substring(
//            annotatedString.length, annotatedString.length + match.value.length
//        )
        annotatedString.withStyle(style = ParagraphStyle(lineHeight = 50.sp)) {
            annotatedString.append(
                match.value
            )
            annotatedString.pop()
        }
        currentIndex = match.range.last + 1
    }

    // Append the remaining text after the last code block
    annotatedString.append(text.substring(currentIndex))

    SelectionContainer {
        Text(text = annotatedString.toAnnotatedString())
    }
}

@Composable
@Preview(showBackground = true)
fun CodeTextPreview() {
    CodeText("This is a code block:\n\n```kotlin\nfun main() {\n   println(\"Hello, Jetpack Compose!\")\n}\n```")
}
