package com.example.rollthedice.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DiceGameViewModel : ViewModel() {

    // Wrapping dice states in MutableStateFlow
    private val _humanDice = MutableStateFlow<List<Int>>(List(5) { (1..6).random() })
    val humanDice: StateFlow<List<Int>> = _humanDice

    private val _computerDice = MutableStateFlow<List<Int>>(List(5) { (1..6).random() })
    val computerDice: StateFlow<List<Int>> = _computerDice

    private val _humanScore = MutableStateFlow(0)
    val humanScore: StateFlow<Int> = _humanScore

    private val _computerScore = MutableStateFlow(0)
    val computerScore: StateFlow<Int> = _computerScore

    var rerolls = 0
    var targetScore = 101
    private val selectedDice = mutableSetOf<Int>()

    // Function to toggle dice selection
    fun toggleDiceSelection(index: Int) {
        if (index in selectedDice) selectedDice.remove(index) else selectedDice.add(index)
    }

    // Function to reroll only the selected dice
    fun rerollSelectedDice() {
        if (rerolls < 2) {
            _humanDice.value = _humanDice.value.mapIndexed { index, value ->
                if (index in selectedDice) value else (1..6).random()
            }
            rerolls++
        }
    }

    // Function to score and update total score
    fun scoreTurn() {
        _humanScore.value += _humanDice.value.sum()
        playComputerTurn()
        rerolls = 0
        selectedDice.clear()
    }

    // Function for the computer's turn
    private fun playComputerTurn() {
        _computerDice.value = List(5) { (1..6).random() }
        val rerolls = (0..2).random()
        repeat(rerolls) {
            _computerDice.value = _computerDice.value.map { if ((0..1).random() == 1) (1..6).random() else it }
        }
        _computerScore.value += _computerDice.value.sum()
    }
}
