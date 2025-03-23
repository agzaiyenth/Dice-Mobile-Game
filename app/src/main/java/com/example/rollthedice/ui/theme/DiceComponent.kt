package com.example.rollthedice.ui.theme

import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rollthedice.R

@Composable
fun DiceRow(dice: List<Int>, selectedDice: List<Boolean>, onClick: ((Int) -> Unit)?) {
    Row {
        dice.forEachIndexed { index, value ->
            DiceImage(
                value = value,
                isSelected = selectedDice[index],
                onClick = onClick?.let { { onClick(index) } }
            )
        }
    }
}

@Composable
fun DiceImage(value: Int, isSelected: Boolean, onClick: (() -> Unit)?) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .padding(4.dp)
            .background(if (isSelected) Color.LightGray else Color.Transparent)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
    ) {
        Image(
            painter = painterResource(id = diceDrawable(value)),
            contentDescription = "Dice $value",
            modifier = Modifier.fillMaxSize()
        )
    }
}

fun diceDrawable(value: Int): Int {
    return when (value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
}
