package com.example.rollthedice.ui.theme


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rollthedice.R
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun DiceRow(dice: List<Int>, onClick: ((Int) -> Unit)?) {
    Row {
        dice.forEachIndexed { index, value ->  // ✅ Now this works because dice is a List<Int>
            DiceImage(value, onClick?.let { { onClick(index) } })
        }
    }
}


@Composable
fun DiceImage(value: Int, onClick: (() -> Unit)?) {
    Image(
        painter = painterResource(id = diceDrawable(value)),
        contentDescription = "Dice $value",
        modifier = Modifier
            .size(64.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
    )
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
