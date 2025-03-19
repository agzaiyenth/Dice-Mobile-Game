package com.example.rollthedice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class DiceGameViewModel(private val mode: String) : ViewModel() {
    private val _humanDice = MutableStateFlow(List(5) { 1 })
    val humanDice = _humanDice.asStateFlow()

    private val _computerDice = MutableStateFlow(List(5) { 1 })
    val computerDice = _computerDice.asStateFlow()

    private val _humanScore = MutableStateFlow(0)
    val humanScore = _humanScore.asStateFlow()

    private val _computerScore = MutableStateFlow(0)
    val computerScore = _computerScore.asStateFlow()

    private val _selectedDice = MutableStateFlow(List(5) { false })
    val selectedDice = _selectedDice.asStateFlow()

    private val _hasThrown = MutableStateFlow(false)
    val hasThrown = _hasThrown.asStateFlow()

    private val _rerollCount = MutableStateFlow(0)
    val rerollCount = _rerollCount.asStateFlow()

    val targetScore = 101

    fun toggleDiceSelection(index: Int) {
        _selectedDice.value = _selectedDice.value.toMutableList().also {
            it[index] = !it[index]
        }
    }

    fun throwDice() {
        _humanDice.value = List(5) { Random.nextInt(1, 7) }
        _hasThrown.value = true
        _rerollCount.value = 0
        computerTurn()
    }

    fun rerollSelectedDice() {
        if (_rerollCount.value < 2) {
            _humanDice.value = _humanDice.value.mapIndexed { index, value ->
                if (_selectedDice.value[index]) value else Random.nextInt(1, 7)
            }
            _rerollCount.value++

            if (_rerollCount.value >= 2) {
                viewModelScope.launch {
                    delay(2000L)
                    scoreTurn()
                }
            }
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
        _computerDice.value = if (mode == "hard") hardModeStrategy() else easyModeStrategy()
    }

    private fun easyModeStrategy(): List<Int> {
        return List(5) { Random.nextInt(1, 7) }
    }

    private fun hardModeStrategy(): List<Int> {
        val currentDice = _computerDice.value
        val shouldPlaySmart = Random.nextBoolean()

        return if (shouldPlaySmart) {
            currentDice.map { if (it < 4) Random.nextInt(4, 7) else it }
        } else {
            List(5) { Random.nextInt(1, 7) }
        }
    }

    private fun resetForNextTurn() {
        _humanDice.value = List(5) { 1 }
        _computerDice.value = List(5) { 1 }
        _selectedDice.value = List(5) { false }
        _hasThrown.value = false
        _rerollCount.value = 0
    }
}
