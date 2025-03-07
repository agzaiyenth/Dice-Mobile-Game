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
fun GameScreen(navController: NavController, viewModel: DiceGameViewModel = remember { DiceGameViewModel() }) {
    val humanDice by viewModel.humanDice.collectAsState()
    val computerDice by viewModel.computerDice.collectAsState()
    val humanScore by viewModel.humanScore.collectAsState()
    val computerScore by viewModel.computerScore.collectAsState()
    val hasThrown by viewModel.hasThrown.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        Text("Target Score: ${viewModel.targetScore}")
        Text("H: $humanScore / C: $computerScore")

        // Display dice rows
        DiceRow(humanDice, viewModel::toggleDiceSelection)
        DiceRow(computerDice, null)

        Row {
              Button(onClick = {
                if (!hasThrown) {
                    viewModel.throwDice()
                } else {
                    viewModel.rerollSelectedDice()
                }
            }) {
                Text(if (!hasThrown) "Throw" else "Reroll")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(onClick = { viewModel.scoreTurn() }) {
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
