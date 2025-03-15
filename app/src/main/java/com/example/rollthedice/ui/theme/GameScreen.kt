package com.example.rollthedice.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rollthedice.viewmodel.DiceGameViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.Alignment

@Composable
fun GameScreen(navController: NavController, mode: String) {
    val viewModel = remember { DiceGameViewModel(mode) }
    val humanDice by viewModel.humanDice.collectAsState()
    val computerDice by viewModel.computerDice.collectAsState()
    val humanScore by viewModel.humanScore.collectAsState()
    val computerScore by viewModel.computerScore.collectAsState()
    val hasThrown by viewModel.hasThrown.collectAsState()
    val rerollCount by viewModel.rerollCount.collectAsState()
    val selectedDice by viewModel.selectedDice.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                .background(Color(0xFF04056C), shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ){
            Column {
                Text("H: $humanScore / C: $computerScore", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 10.dp)
                .background(Color(0xFFDD2C00), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text("Target: ${viewModel.targetScore}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Mode: ${if (mode == "hard") "Hard Mode" else "Easy Mode"}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(24.dp))
            DiceRow(humanDice, selectedDice, viewModel::toggleDiceSelection)
            Spacer(modifier = Modifier.height(16.dp))
            DiceRow(computerDice, List(5) { false }, null)

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (!hasThrown) viewModel.throwDice()
                        else viewModel.rerollSelectedDice()
                    },
                    enabled = !hasThrown || rerollCount < 2,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04056C))
                ) {
                    Text(if (!hasThrown) "Throw" else "Reroll (${2 - rerollCount})", color = Color.White)
                }
                Button(
                    onClick = { viewModel.scoreTurn() },
                    enabled = hasThrown,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04056C))
                ) {
                    Text("Score", color = Color.White)
                }
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