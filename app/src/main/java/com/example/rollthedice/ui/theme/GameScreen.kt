package com.example.rollthedice.ui.theme

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.app.Activity
import kotlin.random.Random

/**
 * -------------------------------
 * Computer Player Strategy: Current Version
 * -------------------------------
 *
 * Strategy Description:
 * - The computer's behavior in hard mode is partially randomized.
 * - It randomly decides whether to play smart or randomly on each turn using:
 *     val shouldPlaySmart = Random.nextBoolean()
 *
 * If shouldPlaySmart is TRUE:
 * - The computer follows a simple heuristic:
 *     - For each die value:
 *         - If the die is less than 4 (i.e., 1, 2, or 3), it is rerolled with a random value from 4 to 6.
 *         - If the die is 4, 5, or 6, it is kept.
 *     - This aims to increase the chances of higher total values in a round.
 *
 * If shouldPlaySmart is FALSE:
 * - The computer simply rolls all 5 dice randomly, simulating a non-strategic move.
 *
 * Advantages:
 * - Slightly better than full random rolling, especially when smart play is chosen.
 * - Easy to implement and adds variety through randomness.
 *
 * Disadvantages:
 * - The decision to play smart is left to chance, which leads to inconsistency.
 * - Only applies logic at the first roll — no reroll strategy is implemented for the computer player.
 * - Doesn’t adapt based on the current score gap, target score, or game pressure.
 * - Can lead to frequent ties or losses, especially against a human player using rerolls.
 *
 * Summary:
 * - This is a basic hybrid strategy using randomness and limited logic.
 * - To improve performance, it needs reroll rounds and score-aware adaptations.
 */


@Composable
fun GameScreen(activity: Activity, mode: String, targetScore: Int){

    var humanDice by rememberSaveable { mutableStateOf(List(5) { 1 }) }
    var computerDice by rememberSaveable { mutableStateOf(List(5) { 1 }) }
    var humanScore by rememberSaveable { mutableStateOf(0) }
    var computerScore by rememberSaveable { mutableStateOf(0) }
    var hasThrown by rememberSaveable { mutableStateOf(false) }
    var rerollCount by rememberSaveable { mutableStateOf(0) }
    var selectedDice by rememberSaveable { mutableStateOf(List(5) { false }) }
    var winner by rememberSaveable { mutableStateOf<String?>(null) }
    var gameOver by rememberSaveable { mutableStateOf(false) }
    var isRolling by remember { mutableStateOf(false) }
    var rollingHumanDice by remember { mutableStateOf(humanDice) }
    var isComputerRolling by remember { mutableStateOf(false) }
    var rollingComputerDice by remember { mutableStateOf(computerDice) }
    val handler = Handler(Looper.getMainLooper())

    fun easyModeStrategy(): List<Int> = List(5) { Random.nextInt(1, 7) }

    fun hardModeStrategy(): List<Int> {
        val shouldPlaySmart = Random.nextBoolean()
        return if (shouldPlaySmart) {
            computerDice.map { if (it < 4) Random.nextInt(4, 7) else it }
        } else {
            List(5) { Random.nextInt(1, 7) }
        }
    }

    fun scoreTurn() {
        humanScore += humanDice.sum()
        computerScore += computerDice.sum()

        if (humanScore >= targetScore || computerScore >= targetScore) {
            when {
                humanScore >= targetScore && computerScore >= targetScore -> {
                    if (humanScore > computerScore) {
                        winner = "Human"
                        gameOver = true
                    } else if (computerScore > humanScore) {
                        winner = "Computer"
                        gameOver = true
                    }
                    // If both scores are equal → tie → play another round
                }
                humanScore >= targetScore -> {
                    winner = "Human"
                    gameOver = true
                }
                computerScore >= targetScore -> {
                    winner = "Computer"
                    gameOver = true
                }
            }
        }
        if (!gameOver) {
            hasThrown = false
            rerollCount = 0
            selectedDice = List(5) { false }
            humanDice = List(5) { 1 }
            computerDice = List(5) { 1 }
        }
    }

    fun toggleDiceSelection(index: Int) {
        selectedDice = selectedDice.toMutableList().also { it[index] = !it[index] }
    }

    fun animateHumanDice(times: Int = 10, delayMillis: Long = 100L, onEnd: () -> Unit) {
        if (times == 0) {
            rollingHumanDice = humanDice
            onEnd()
            return
        }
        rollingHumanDice = humanDice.mapIndexed { index, value ->
            if (selectedDice[index]) value else Random.nextInt(1, 7)
        }
        handler.postDelayed({
            animateHumanDice(times - 1, delayMillis, onEnd)
        }, delayMillis)
    }

    fun animateComputerDice(times: Int = 10, delayMillis: Long = 100L, onEnd: () -> Unit) {
        if (times == 0) {
            rollingComputerDice = computerDice
            onEnd()
            return
        }
        rollingComputerDice = List(5) { Random.nextInt(1, 7) }
        handler.postDelayed({
            animateComputerDice(times - 1, delayMillis, onEnd)
        }, delayMillis)
    }

    fun throwDice() {
        isRolling = true
        isComputerRolling = true
        humanDice = List(5) { Random.nextInt(1, 7) }
        computerDice = if (mode == "hard") hardModeStrategy() else easyModeStrategy()
        hasThrown = true
        rerollCount = 0

        animateHumanDice(onEnd = { isRolling = false })
        animateComputerDice(onEnd = { isComputerRolling = false })
    }

    fun rerollSelectedDice() {
        isRolling = true
        humanDice = humanDice.mapIndexed { index, value ->
            if (selectedDice[index]) value else Random.nextInt(1, 7)
        }
        rerollCount++
        animateHumanDice(onEnd = {
            isRolling = false
            if (rerollCount >= 2) {
                handler.postDelayed({
                    scoreTurn()
                }, 2000)
            }
        })
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
            Text("Human Dice", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            DiceRow(
                if (isRolling) rollingHumanDice else humanDice,
                selectedDice,
                ::toggleDiceSelection
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Computer Dice", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            DiceRow(
                if (isComputerRolling) rollingComputerDice else computerDice,
                List(5) { false },
                null
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (!hasThrown) {
                            throwDice()
                        } else if (rerollCount < 2) {
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
                            scoreTurn()
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

        if (gameOver && winner != null) {
            WinnerPopup(
                winner = winner,
                onDismiss = {
                    activity.finish()
                }
            )
        }
    }
}
