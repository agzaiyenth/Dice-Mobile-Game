package com.example.rollthedice.ui.theme


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun WinnerPopup(humanScore: Int, computerScore: Int, onDismiss: () -> Unit) {
    val winnerText = when {
        humanScore >= 101 && computerScore < 101 -> "🎉 You Win!"
        computerScore >= 101 && humanScore < 101 -> "😢 You Lose!"
        else -> "It's a Tie! 🎲"
    }

    val winnerColor = when {
        humanScore >= 101 && computerScore < 101 -> Color.Green
        computerScore >= 101 && humanScore < 101 -> Color.Red
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
