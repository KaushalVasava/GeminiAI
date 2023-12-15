package com.lahsuak.apps.geminiai.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lahsuak.apps.geminiai.R
import com.lahsuak.apps.geminiai.ui.model.states.SummarizeUiState
import com.lahsuak.apps.geminiai.ui.viewmodel.SummarizeViewModel
import com.lahsuak.apps.geminiai.ui.theme.GeminiAITheme

@Composable
internal fun SummarizeRoute(
    summarizeViewModel: SummarizeViewModel,
    onBackPress: () -> Unit,
) {
    val summarizeUiState by summarizeViewModel.uiState.collectAsState()

    SummarizeScreen(
        summarizeUiState,
        onBackPress,
        onSummarizeClicked = { inputText ->
            summarizeViewModel.summarizeStreaming(inputText)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummarizeScreen(
    uiState: SummarizeUiState = SummarizeUiState.Loading,
    onBackPress: () -> Unit = {},
    onSummarizeClicked: (String) -> Unit = {},
) {
    var textToSummarize by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.text_generative_ai)) },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPress()
                    }) {
                        Icon(Icons.Default.ArrowBack, stringResource(R.string.back))
                    }
                })
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .verticalScroll(rememberScrollState()),
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                OutlinedTextField(
                    value = textToSummarize,
                    label = { Text(stringResource(R.string.summarize_label)) },
                    placeholder = { Text(stringResource(R.string.summarize_hint)) },
                    onValueChange = { textToSummarize = it },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
                TextButton(
                    onClick = {
                        if (textToSummarize.isNotBlank()) {
                            onSummarizeClicked(textToSummarize)
                        }
                    },
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 16.dp)
                        .align(Alignment.End)
                ) {
                    Text(stringResource(R.string.action_go))
                }
            }
            when (uiState) {
                SummarizeUiState.Initial -> {
                    // Nothing is shown
                }

                SummarizeUiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is SummarizeUiState.Success -> {
                    Card(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(all = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = "Person Icon",
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier
                                    .requiredSize(36.dp)
                                    .drawBehind {
                                        drawCircle(color = Color.White)
                                    }
                            )
                            Text(
                                text = uiState.outputText,
                                color = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                is SummarizeUiState.Error -> {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(all = 16.dp)
                        )
                    }
                }
            }
        }
    }

}

@Composable
@Preview(showSystemUi = true)
fun SummarizeScreenPreview() {
    GeminiAITheme(darkTheme = true) {
        SummarizeScreen()
    }
}
