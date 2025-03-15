package com.example.rollthedice.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ModeSelectionDialog(onModeSelected: (String) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Difficulty Mode") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally, // Align buttons center
                verticalArrangement = Arrangement.spacedBy(12.dp) // Add spacing between buttons
            ) {
                Text("Choose your game mode:", color = Color.Black)
                Button(
                    onClick = { onModeSelected("easy") },
                    modifier = Modifier.fillMaxWidth(0.7f), // Ensures uniform button size
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04056C))
                ) {
                    Text("Easy Mode", color = Color.White)
                }
                Button(
                    onClick = { onModeSelected("hard") },
                    modifier = Modifier.fillMaxWidth(0.7f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04056C))
                ) {
                    Text("Hard Mode", color = Color.White)
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(0.7f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04056C))
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        }
    )
}
