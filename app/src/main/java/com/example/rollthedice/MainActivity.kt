package com.example.rollthedice

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import com.example.rollthedice.ui.theme.ModeSelectionDialog
import com.example.rollthedice.ui.theme.showAboutDialog

/**
 * AGZAIYENTH GANARAJ - 20230746 w2051756
 * I confirm that I understand what plagiarism is and have read and understood the
 * section on Assessment Offences in the Essential Information for Students. The work
 * that I have submitted is entirely my own. Any work from other authors is duly
 * referenced and acknowledge
 */


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showModeDialog by remember { mutableStateOf(false) }
            var showAboutDialog by remember { mutableStateOf(false) }

            if (showModeDialog) {
                ModeSelectionDialog(
                    onModeSelected = { mode, targetScore ->
                        val intent = Intent(this, GameActivity::class.java).apply {
                            putExtra("mode", mode)
                            putExtra("targetScore", targetScore)
                        }
                        startActivity(intent)
                        showModeDialog = false
                    },
                    onDismiss = { showModeDialog = false }
                )
            }

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
                        onClick = { showAboutDialog = true },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04056C))
                    ) {
                        Text("About", fontSize = 18.sp, color = Color.White)
                    }
                }
            }

            if (showAboutDialog) {
                showAboutDialog {
                    showAboutDialog = false
                }
            }
        }
    }
}
