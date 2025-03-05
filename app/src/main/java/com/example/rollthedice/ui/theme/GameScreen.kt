package com.example.rollthedice.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rollthedice.viewmodel.DiceGameViewModel
import com.example.rollthedice.ui.theme.WinnerPopup


import androidx.compose.runtime.collectAsState

import androidx.compose.runtime.collectAsState

@Composable
fun GameScreen(navController: NavController, viewModel: DiceGameViewModel = remember { DiceGameViewModel() }) {
    val humanDice by viewModel.humanDice.collectAsState()
    val computerDice by viewModel.computerDice.collectAsState()
    val humanScore by viewModel.humanScore.collectAsState()
    val computerScore by viewModel.computerScore.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        Text("Target Score: ${viewModel.targetScore}")
        Text("H: $humanScore / C: $computerScore")

        // Display dice rows
        DiceRow(humanDice, viewModel::toggleDiceSelection)
        DiceRow(computerDice, null)

        // Buttons for Reroll and Score
        Row {
            Button(onClick = { viewModel.rerollSelectedDice() }, enabled = viewModel.rerolls < 2) {
                Text("Re-roll")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = { viewModel.scoreTurn() }) {
                Text("Score")
            }
        }

        // Show winner popup when the game ends
        if (humanScore >= viewModel.targetScore || computerScore >= viewModel.targetScore) {
            WinnerPopup(
                humanScore, computerScore,
                onDismiss = { navController.navigate("main_menu") }
            )
        }
    }
}
