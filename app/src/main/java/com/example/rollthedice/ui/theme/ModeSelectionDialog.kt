package com.example.rollthedice.ui.theme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ModeSelectionDialog(onModeSelected: (String) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Difficulty Mode") },
        text = {
            Column {
                Text("Choose your game mode:")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onModeSelected("easy") }) {
                    Text("Easy Mode")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onModeSelected("hard") }) {
                    Text("Hard Mode")
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
