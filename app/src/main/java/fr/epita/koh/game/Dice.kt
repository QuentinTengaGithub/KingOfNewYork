package fr.epita.koh.game

import kotlin.random.Random
import kotlin.random.nextInt

class Dice {
    var diceState : DiceState = DiceState.ONE;

    var diceNumber : Int = 0

    var diceSelected : Boolean = false;

    init {
        generateRandomState();
        diceSelected = false;
    }

    fun generateRandomState() {
        diceNumber = Random.nextInt(0, 6);
        when (diceNumber) {
            0 -> diceState = DiceState.ONE;
            1 -> diceState = DiceState.TWO;
            2 -> diceState = DiceState.THREE;
            3 -> diceState = DiceState.ATTACK;
            4 -> diceState = DiceState.HEAL;
            5 -> diceState = DiceState.ENERGY;
        }
    }
}

enum class DiceState {
    ONE,
    TWO,
    THREE,
    ATTACK,
    HEAL,
    ENERGY
}