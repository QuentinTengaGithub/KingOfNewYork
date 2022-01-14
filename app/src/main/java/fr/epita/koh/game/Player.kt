package fr.epita.koh.game

class Player(isHuman : Boolean) {

    var extraDiceRoll: Int = 0
    var playerEnergy : Int = 0

    var playerVictoryPoints : Int = 0

    private val isHuman: Boolean = isHuman;

    var playerState : PlayerState = PlayerState.WaitingTurn;

    var playerHealth : Int = 10;

    var playerDead : Boolean = false;

    fun reset() {
        playerState = PlayerState.WaitingTurn;
        playerHealth = 10
        playerDead = false;
        extraDiceRoll = 0;
    }

    fun attack(count : Int) {
        playerHealth -= count;

        if (playerHealth <= 0) {
            playerDead = true;
        }
    }

    fun play() : Boolean {
        println(playerState);
        return true;
    }

    fun heal() {
        ++playerHealth;

        if (playerHealth > 10) {
            playerHealth = 10;
        }
    }
}