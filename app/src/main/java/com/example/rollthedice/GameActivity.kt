package com.example.rollthedice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.rollthedice.ui.theme.GameScreen

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mode = intent.getStringExtra("mode") ?: "easy"
        val targetScore = intent.getIntExtra("targetScore", 101)

        setContent {
            GameScreen(activity = this, mode = mode, targetScore = targetScore)
        }
    }
}
