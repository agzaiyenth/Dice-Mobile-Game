package com.example.rollthedice.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun ModeSelectionDialog(onModeSelected: (String, Int) -> Unit, onDismiss: () -> Unit) {
    var selectedMode by remember { mutableStateOf("easy") } // Default mode
    var targetScore by remember { mutableStateOf(TextFieldValue("101")) } // Default target score

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Mode & Target Score") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Choose your game mode:", color = Color.Black)

                // Mode Selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { selectedMode = "easy" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedMode == "easy") Color(0xFF04056C) else Color.Gray
                        )
                    ) {
                        Text("Easy", color = Color.White)
                    }
                    Button(
                        onClick = { selectedMode = "hard" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedMode == "hard") Color(0xFF04056C) else Color.Gray
                        )
                    ) {
                        Text("Hard", color = Color.White)
                    }
                }

                // Target Score Input
                Text("Enter Target Score:", color = Color.Black)
                OutlinedTextField(
                    value = targetScore,
                    onValueChange = { newValue ->
                        if (newValue.text.all { it.isDigit() }) { // Only allow numbers
                            targetScore = newValue
                        }
                    },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val score = targetScore.text.toIntOrNull() ?: 101
                    if (score > 1) { // Ensure valid score
                        onModeSelected(selectedMode, score)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04056C))
            ) {
                Text("Start Game", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancel", color = Color.White)
            }
        }
    )
}
