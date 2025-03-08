package com.example.rollthedice.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ModeSelectionDropdown(selectedMode: String, onModeSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val modes = listOf("Easy Mode", "Hard Mode")

    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp)
    ) {
        Button(onClick = { expanded = true }) {
            Text(selectedMode)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            modes.forEach { mode ->
                DropdownMenuItem(
                    text = { Text(mode) },
                    onClick = {
                        onModeSelected(mode)
                        expanded = false
                    }
                )
            }
        }
    }
}
