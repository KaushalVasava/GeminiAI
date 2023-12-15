package com.lahsuak.apps.geminiai.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lahsuak.apps.geminiai.ui.screen.ChatRoute
import com.lahsuak.apps.geminiai.ui.viewmodel.ChatViewModel
import com.lahsuak.apps.geminiai.ui.screen.PhotoReasoningRoute
import com.lahsuak.apps.geminiai.ui.viewmodel.PhotoReasoningViewModel
import com.lahsuak.apps.geminiai.ui.screen.SummarizeRoute
import com.lahsuak.apps.geminiai.ui.viewmodel.SummarizeViewModel
import com.lahsuak.apps.geminiai.ui.screen.MenuScreen
import com.lahsuak.apps.geminiai.ui.theme.GeminiAITheme

class MainActivity : ComponentActivity() {
    private val summariseViewModel: SummarizeViewModel by viewModel()
    private val photoReasoningViewModel: PhotoReasoningViewModel by viewModel()
    private val chatViewModel: ChatViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeminiAITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "menu") {
                        composable("menu") {
                            MenuScreen(onItemClicked = { routeId ->
                                navController.navigate(routeId)
                            })
                        }
                        composable("summarize") {
                            SummarizeRoute(summariseViewModel) {
                                navController.popBackStack()
                            }
                        }
                        composable("photo_reasoning") {
                            PhotoReasoningRoute(photoReasoningViewModel) {
                                navController.popBackStack()
                            }
                        }
                        composable("chat") {
                            ChatRoute(chatViewModel) {
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}
