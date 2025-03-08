package com.example.rollthedice.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rollthedice.viewmodel.DiceGameViewModel
import androidx.compose.runtime.collectAsState
@Composable
fun GameScreen(navController: NavController, mode: String) { // ✅ Mode is passed and fixed
    val viewModel = remember { DiceGameViewModel(mode) } // ✅ Pass mode to ViewModel
    val humanDice by viewModel.humanDice.collectAsState()
    val computerDice by viewModel.computerDice.collectAsState()
    val humanScore by viewModel.humanScore.collectAsState()
    val computerScore by viewModel.computerScore.collectAsState()
    val hasThrown by viewModel.hasThrown.collectAsState()
    val rerollCount by viewModel.rerollCount.collectAsState()
    val selectedDice by viewModel.selectedDice.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        Text("Target Score: ${viewModel.targetScore}")
        Text("Mode: ${if (mode == "hard") "Hard Mode" else "Easy Mode"}")
        Text("H: $humanScore / C: $computerScore")

        DiceRow(humanDice, selectedDice, viewModel::toggleDiceSelection)
        DiceRow(computerDice, List(5) { false }, null)

        Row {
            Button(
                onClick = {
                    if (!hasThrown) viewModel.throwDice()
                    else viewModel.rerollSelectedDice()
                },
                enabled = !hasThrown || rerollCount < 2
            ) {
                Text(if (!hasThrown) "Throw" else "Reroll (${2 - rerollCount})")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = { viewModel.scoreTurn() },
                enabled = hasThrown
            ) {
                Text("Score")
            }
        }

        if (humanScore >= viewModel.targetScore || computerScore >= viewModel.targetScore) {
            WinnerPopup(
                humanScore, computerScore,
                onDismiss = { navController.navigate("main_menu") }
            )
        }
    }
}







