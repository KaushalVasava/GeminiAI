package com.lahsuak.apps.geminiai.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lahsuak.apps.geminiai.R
import kotlin.random.Random

data class MenuItem(
    val routeId: String,
    val titleResId: Int,
    val descriptionResId: Int,
    @DrawableRes
    val imageResId: Int,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onItemClicked: (routeId: String) -> Unit = { },
) {
    val menuItems = listOf(
        MenuItem(
            "summarize",
            R.string.menu_summarize_title,
            R.string.menu_summarize_description,
            R.drawable.ic_text
        ),
        MenuItem(
            "photo_reasoning",
            R.string.menu_reason_title,
            R.string.menu_reason_description,
            R.drawable.ic_image_text
        ),
        MenuItem(
            "chat",
            R.string.menu_chat_title,
            R.string.menu_chat_description,
            R.drawable.ic_chat
        )
    )
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
        }
    ) { paddingValue ->
        LazyColumn(
            Modifier
                .padding(paddingValue)
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            items(menuItems) { menuItem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                    onClick = {
                        onItemClicked(menuItem.routeId)
                    }
                ) {
                    Row(
                        Modifier
                            .drawBehind {
                                drawRoundRect(
                                    Brush.linearGradient(
                                        listOf(
                                            Color.Blue.copy(0.1f),
                                            Color.Cyan.copy(0.1f),
                                            Color.Green.copy(0.1f),
                                        )
                                    )
                                )
                            }
                            .padding(16.dp)
                            .fillMaxWidth()
                            .drawBehind {
                                repeat(Random.nextInt(6, 10)) {
                                    drawCircle(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                Color.Red,
                                                Color.Blue
                                            )
                                        ),
                                        alpha = 0.4f,
                                        radius = Random
                                            .nextInt(5, 100)
                                            .toFloat(),
                                        center = Offset(
                                            Random
                                                .nextInt(
                                                    size.minDimension.toInt(),
                                                    size.maxDimension.toInt()
                                                )
                                                .toFloat(),
                                            Random
                                                .nextInt(10, size.maxDimension.toInt())
                                                .toFloat()
                                        )
                                    )
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(painterResource(id = menuItem.imageResId), contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(menuItem.titleResId),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = stringResource(menuItem.descriptionResId),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MenuScreenPreview() {
    MenuScreen()
}
