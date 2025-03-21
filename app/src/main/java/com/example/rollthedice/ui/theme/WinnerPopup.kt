package com.example.rollthedice.ui.theme


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun WinnerPopup(humanScore: Int, computerScore: Int, targetScore: Int, onDismiss: () -> Unit) {
    val winnerText = when {
        humanScore >= targetScore && computerScore < targetScore -> "You Win!"
        computerScore >= targetScore && humanScore < targetScore -> "You Lose!"
        else -> "It's a Tie!"
    }

    val winnerColor = when {
        humanScore >= targetScore && computerScore < targetScore -> Color.Green
        computerScore >= targetScore && humanScore < targetScore -> Color.Red
        else -> Color.Blue
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = winnerText, color = winnerColor) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

