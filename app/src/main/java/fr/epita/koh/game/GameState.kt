package fr.epita.koh.game

class GameState {
    private var onPlayerTurnChanged: ((Int, Boolean) -> Unit)? = null

    private var onPlayerHealthChanged: ((Int, Int, Int) -> Unit)? = null

    private val players = arrayOf(
        Player(true),
        Player(false),
        Player(false),
        Player(false));

    private var currentPlayerTurn = -1;

    private var kingOfTheHill = -1;

    init {
        setPlayerTurn(0);
    }

    private fun setPlayerTurn(newTurn : Int) : Boolean {
        onPlayerTurnChanged?.invoke(newTurn, true);

        if (currentPlayerTurn >= 0) {
            players[currentPlayerTurn].playerState = PlayerState.WaitingTurn;
            onPlayerTurnChanged?.invoke(currentPlayerTurn, false);
        }

        currentPlayerTurn = newTurn;
        players[currentPlayerTurn].playerState = PlayerState.RollDice;

        return !players[currentPlayerTurn].playerDead;
    }

    private fun setPlayerStage(newStage : PlayerState) {
        players[currentPlayerTurn].playerState = newStage;
    }

    private fun nextPlayerStage() {
        when (players[currentPlayerTurn].playerState) {
            PlayerState.RollDice -> setPlayerStage(PlayerState.EnterNewYork);
            PlayerState.EnterNewYork -> setPlayerStage(PlayerState.BuyPowerCards);
            PlayerState.BuyPowerCards -> setPlayerStage(PlayerState.EndOfTurn);
            PlayerState.EndOfTurn -> {
                while (!setPlayerTurn((currentPlayerTurn + 1) % players.size));
            };
        }
    }

    private fun attackPlayers(attackerId : Int) {
        if (kingOfTheHill == attackerId) {
            // Attack everyone else
            for (i in 0 until 4) {
                if (i != attackerId) {
                    val victim = players[i];
                    val oldHealth = victim.playerHealth;

                    victim.attack();

                    if (victim.playerDead) onPlayerDead(i);

                    onPlayerHealthChanged?.invoke(i, oldHealth, victim.playerHealth);
                }
            }
        } else if (kingOfTheHill >= 0) {
            // Attack the king of the hill
            val victim = players[kingOfTheHill];
            val oldHealth = victim.playerHealth;

            victim.attack();

            if (victim.playerDead) onPlayerDead(kingOfTheHill);

            onPlayerHealthChanged?.invoke(kingOfTheHill, oldHealth, victim.playerHealth);
        }
    }

    private fun onPlayerDead(playerId: Int) {
        if (playerId == kingOfTheHill) {
            //King of the hill died, free spot
            kingOfTheHill = -1;
        }
    }

    fun onPlayerTurnChanged(block: (Int, Boolean) -> Unit) {
        this.onPlayerTurnChanged = block;
    }

    fun onPlayerHealthChanged(block: (Int, Int, Int) -> Unit) {
        this.onPlayerHealthChanged = block;
    }

    fun reset() {
        currentPlayerTurn = -1;

        for (i in 0 until 4) {
            onPlayerTurnChanged?.invoke(i, false);
            onPlayerHealthChanged?.invoke(i, 20, 10);
        }

        for (p in players) {
            p.reset();
        }

        setPlayerTurn(0);
    }

    fun play() {
        if (players[currentPlayerTurn].play())
            nextPlayerStage();
    }
}