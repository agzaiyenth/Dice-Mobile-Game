package com.example.rollthedice.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun GameScreen(navController: NavController, mode: String) {
    var humanDice by rememberSaveable { mutableStateOf(List(5) { 1 }) }  // Start with 11111
    var computerDice by rememberSaveable { mutableStateOf(List(5) { 1 }) } // Start with 11111
    var humanScore by rememberSaveable { mutableStateOf(0) }
    var computerScore by rememberSaveable { mutableStateOf(0) }
    var hasThrown by rememberSaveable { mutableStateOf(false) }
    var rerollCount by rememberSaveable { mutableStateOf(0) }
    var selectedDice by rememberSaveable { mutableStateOf(List(5) { false }) }

    val targetScore = 101
    var isRolling by remember { mutableStateOf(false) }
    var rollingHumanDice by remember { mutableStateOf(humanDice) }

    // 🎯 Auto-score when rerollCount reaches 2
    LaunchedEffect(rerollCount) {
        if (hasThrown && rerollCount >= 2) {
            delay(2000L) // Wait for 2 seconds before scoring

            // Update score
            humanScore += humanDice.sum()
            computerScore += computerDice.sum()

            // Reset game state
            hasThrown = false
            rerollCount = 0
            selectedDice = List(5) { false }

            // Reset dice back to 11111
            humanDice = List(5) { 1 }
            computerDice = List(5) { 1 }
        }
    }

    LaunchedEffect(isRolling) {
        if (isRolling) {
            repeat(10) {
                rollingHumanDice = humanDice.mapIndexed { index, value ->
                    if (selectedDice[index]) value else Random.nextInt(1, 7)
                }
                delay(100L)
            }
            rollingHumanDice = humanDice
            isRolling = false
        }
    }

    fun throwDice() {
        humanDice = List(5) { Random.nextInt(1, 7) }
        computerDice = List(5) { Random.nextInt(1, 7) }
        hasThrown = true
        rerollCount = 0
    }

    fun rerollSelectedDice() {
        humanDice = humanDice.mapIndexed { index, value ->
            if (selectedDice[index]) value else Random.nextInt(1, 7) // Only change unselected dice
        }
        rerollCount++
    }

    fun scoreTurn() {
        humanScore += humanDice.sum()
        computerScore += computerDice.sum()

        // Reset game state
        hasThrown = false
        rerollCount = 0
        selectedDice = List(5) { false }

        // Reset dice back to 11111
        humanDice = List(5) { 1 }
        computerDice = List(5) { 1 }
    }

    fun toggleDiceSelection(index: Int) {
        selectedDice = selectedDice.toMutableList().also { it[index] = !it[index] }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                .background(Color(0xFF04056C), shape = RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Text(
                text = "H: $humanScore / C: $computerScore",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                .background(Color(0xFFDD2C00), shape = RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Text(
                text = "Target: $targetScore",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DiceRow(
                if (isRolling) rollingHumanDice else humanDice,
                selectedDice,
                ::toggleDiceSelection
            )
            Spacer(modifier = Modifier.height(16.dp))

            DiceRow(computerDice, List(5) { false }, null) // Computer dice remains static after first roll

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (!hasThrown) {
                            isRolling = true
                            throwDice()
                        } else if (rerollCount < 2) {
                            isRolling = true
                            rerollSelectedDice()
                        }
                    },
                    enabled = !isRolling && (!hasThrown || rerollCount < 2),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04056C))
                ) {
                    Text(if (!hasThrown) "Throw" else "Reroll (${2 - rerollCount})", color = Color.White)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = {
                        if (hasThrown && rerollCount <= 2) {
                            scoreTurn() // Player can score even before rerolling
                        }
                    },
                    enabled = hasThrown && !isRolling,
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

        if (humanScore >= targetScore || computerScore >= targetScore) {
            WinnerPopup(
                humanScore, computerScore,
                onDismiss = { navController.navigate("main_menu") }
            )
        }
    }
}
