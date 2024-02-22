package com.lahsuak.apps.geminiai.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lahsuak.apps.geminiai.ui.screen.ChatRoute
import com.lahsuak.apps.geminiai.ui.screen.MenuScreen
import com.lahsuak.apps.geminiai.ui.theme.GeminiAITheme
import com.lahsuak.apps.geminiai.ui.viewmodel.ChatViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
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

                    NavHost(navController = navController, startDestination = "chat") {
//                        composable("menu") {
//                            MenuScreen(onItemClicked = { routeId ->
//                                navController.navigate(routeId)
//                            })
//                        }
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
