package fr.epita.koh.game

class Player(isHuman : Boolean) {

    private val isHuman: Boolean = isHuman

    var playerState : PlayerState = PlayerState.WaitingTurn

    fun reset() {
        playerState = PlayerState.WaitingTurn;
    }

    fun play() : Boolean {
        println(playerState);
        return true;
    }
}