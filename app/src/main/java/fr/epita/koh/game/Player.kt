package fr.epita.koh.game

class Player(isHuman : Boolean) {

    private val isHuman: Boolean = isHuman;

    var playerState : PlayerState = PlayerState.WaitingTurn;

    var playerHealth : Int = 10;

    var playerDead : Boolean = false;

    fun reset() {
        playerState = PlayerState.WaitingTurn;
        playerHealth = 10
        playerDead = false;
    }

    fun attack() {
        --playerHealth;

        if (playerHealth < 0) {
            playerDead = true;
        }
    }

    fun play() : Boolean {
        println(playerState);
        return true;
    }
}