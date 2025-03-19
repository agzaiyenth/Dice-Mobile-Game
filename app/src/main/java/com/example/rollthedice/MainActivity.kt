package com.example.rollthedice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rollthedice.ui.theme.MainMenuScreen
import com.example.rollthedice.ui.theme.GameScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "main_menu") {
                composable("main_menu") { MainMenuScreen(navController) }
                composable("game/{mode}/{targetScore}") { backStackEntry ->
                    val mode = backStackEntry.arguments?.getString("mode") ?: "easy"
                    val targetScore = backStackEntry.arguments?.getString("targetScore")?.toIntOrNull() ?: 101
                    GameScreen(navController, mode, targetScore)
                }
            }
        }
    }
}

