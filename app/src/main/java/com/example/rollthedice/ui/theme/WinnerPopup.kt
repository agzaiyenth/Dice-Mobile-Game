package com.example.rollthedice.ui.theme


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun WinnerPopup(
    winner: String?,
    onDismiss: () -> Unit
) {
    val winnerText = when (winner) {
        "Human" -> "You Win!"
        "Computer" -> "You Lose!"
        else -> "It's a Tie!"
    }

    val winnerColor = when (winner) {
        "Human" -> Color.Green
        "Computer" -> Color.Red
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
