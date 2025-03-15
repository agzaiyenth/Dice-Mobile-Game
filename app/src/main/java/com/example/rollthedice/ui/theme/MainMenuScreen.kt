package com.example.rollthedice.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.example.rollthedice.R

@Composable
fun MainMenuScreen(navController: NavController) {
    var showModeDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                onClick = { showModeDialog = true },
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04056C))
            ) {
                Text("Start Game", fontSize = 18.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04056C))
            ) {
                Text("About", fontSize = 18.sp, color = Color.White)
            }
        }
    }

    if (showDialog) {
        showAboutDialog(onDismiss = { showDialog = false })
    }

    if (showModeDialog) {
        ModeSelectionDialog(
            onModeSelected = { mode ->
                navController.navigate("game/$mode")
                showModeDialog = false
            },
            onDismiss = { showModeDialog = false }
        )
    }
}

@Composable
fun showAboutDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("About") },
        text = {
            Text(
                "I confirm that I understand what plagiarism is and have read and\n" +
                        "understood the section on Assessment Offences in the Essential\n" +
                        "The work that I have submitted is\n" +
                        "entirely my own. Any work from other authors is duly referenced\n" +
                        "and acknowledged."
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04056C))
            ) {
                Text("OK", color = Color.White)
            }
        }
    )
}
