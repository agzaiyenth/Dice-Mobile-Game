package com.example.rollthedice.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class DiceGameViewModel : ViewModel() {
    private val _humanDice = MutableStateFlow(List(5) { 1 })
    val humanDice = _humanDice.asStateFlow()

    private val _computerDice = MutableStateFlow(List(5) { 1 })
    val computerDice = _computerDice.asStateFlow()

    private val _humanScore = MutableStateFlow(0)
    val humanScore = _humanScore.asStateFlow()

    private val _computerScore = MutableStateFlow(0)
    val computerScore = _computerScore.asStateFlow()

    private val _selectedDice = MutableStateFlow(List(5) { false })
    private val _hasThrown = MutableStateFlow(false)
    val hasThrown = _hasThrown.asStateFlow()

    val targetScore = 101

    fun toggleDiceSelection(index: Int) {
        _selectedDice.value = _selectedDice.value.toMutableList().also {
            it[index] = !it[index]
        }
    }

    fun throwDice() {
        _humanDice.value = List(5) { Random.nextInt(1, 7) }
        _hasThrown.value = true
        computerTurn()
    }

    fun rerollSelectedDice() {
        _humanDice.value = _humanDice.value.mapIndexed { index, value ->
            if (_selectedDice.value[index]) value else Random.nextInt(1, 7)
        }
    }

    fun scoreTurn() {
        _humanScore.value += _humanDice.value.sum()
        _computerScore.value += _computerDice.value.sum()

        if (_humanScore.value < targetScore && _computerScore.value < targetScore) {
            resetForNextTurn()
        }
    }

    private fun computerTurn() {
        _computerDice.value = List(5) { Random.nextInt(1, 7) }
    }

    private fun resetForNextTurn() {
        _selectedDice.value = List(5) { false }
        _hasThrown.value = false
    }
}
