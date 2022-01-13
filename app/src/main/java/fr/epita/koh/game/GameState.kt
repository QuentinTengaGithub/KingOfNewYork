package fr.epita.koh.game

import fr.epita.koh.R

class GameState {
    private var onPlayerTurnChanged: ((Int, Boolean) -> Unit)? = null

    private var onPlayerInsideTokyoChanged: ((Int, Boolean) -> Unit)? = null

    private var onPlayerHealthChanged: ((Int, Int, Int) -> Unit)? = null

    private var onPlayerVictoryPointsChanged: ((Int, Int, Int) -> Unit)? = null

    private var onPlayerEnergyChanged: ((Int, Int, Int) -> Unit)? = null

    private var onPlayerStateChanged: ((Int, PlayerState) -> Unit)? = null

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
            PlayerState.RollDice -> {
                if (kingOfTheHill == currentPlayerTurn)
                    players[currentPlayerTurn].playerVictoryPoints += 2;
                setPlayerStage(PlayerState.EnterNewYork)
            };
            PlayerState.EnterNewYork -> setPlayerStage(PlayerState.BuyPowerCards);
            PlayerState.BuyPowerCards -> setPlayerStage(PlayerState.EndOfTurn);
            PlayerState.EndOfTurn -> {
                while (!setPlayerTurn((currentPlayerTurn + 1) % players.size));
            };
        }

        onPlayerStateChanged?.invoke(currentPlayerTurn, players[currentPlayerTurn].playerState);
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
            onPlayerInsideTokyoChanged?.invoke(playerId, false);
            kingOfTheHill = -1;
        }
    }

    private fun healPlayer(playerId: Int) {
        players[playerId].heal();
    }

    fun enterKingOfTheHill(playerId: Int) {
        onPlayerInsideTokyoChanged?.invoke(kingOfTheHill, false);
        kingOfTheHill = playerId;
        players[playerId].playerVictoryPoints += 1;
        onPlayerInsideTokyoChanged?.invoke(kingOfTheHill, true);
    }

    fun onPlayerStateChanged(block: (Int, PlayerState) -> Unit) {
        this.onPlayerStateChanged = block;
    }

    fun onPlayerInsideTokyoChanged(block: (Int, Boolean) -> Unit) {
        this.onPlayerInsideTokyoChanged = block;
    }

    fun onPlayerTurnChanged(block: (Int, Boolean) -> Unit) {
        this.onPlayerTurnChanged = block;
    }

    fun onPlayerHealthChanged(block: (Int, Int, Int) -> Unit) {
        this.onPlayerHealthChanged = block;
    }

    fun onPlayerEnergyChanged(block: (Int, Int, Int) -> Unit) {
        this.onPlayerEnergyChanged = block;
    }

    fun onPlayerVictoryPointsChanged(block: (Int, Int, Int) -> Unit) {
        this.onPlayerVictoryPointsChanged = block;
    }

    fun onCommittedDice(dice : Array<Dice>) {
        val current = players[currentPlayerTurn];

        var oneCount = 0;
        var twoCount = 0;
        var threeCount = 0;

        for (dice in dice) {
            when (dice.diceState) {
                DiceState.ONE -> ++oneCount
                DiceState.TWO -> ++twoCount
                DiceState.THREE -> ++threeCount
                DiceState.ATTACK -> attackPlayers(currentPlayerTurn)
                DiceState.HEAL -> if (kingOfTheHill != currentPlayerTurn) healPlayer(currentPlayerTurn)
                DiceState.ENERGY -> {
                    val lastEnergy = current.playerEnergy;
                    ++current.playerEnergy;
                    onPlayerEnergyChanged?.invoke(currentPlayerTurn, lastEnergy, current.playerEnergy);
                }
            }
        }

        val oldVP = current.playerVictoryPoints;

        if (oneCount >= 1) {
            current.playerVictoryPoints += oneCount;
            onPlayerVictoryPointsChanged?.invoke(currentPlayerTurn, oldVP, current.playerVictoryPoints);
        }
        else if (twoCount >= 2) {
            current.playerVictoryPoints += twoCount;
            onPlayerVictoryPointsChanged?.invoke(currentPlayerTurn, oldVP, current.playerVictoryPoints);
        }
        else if (threeCount >= 3) {
            current.playerVictoryPoints += threeCount;
            onPlayerVictoryPointsChanged?.invoke(currentPlayerTurn, oldVP, current.playerVictoryPoints);
        }
    }

    fun reset() {
        currentPlayerTurn = -1;

        for (i in 0 until 4) {
            onPlayerTurnChanged?.invoke(i, false);
            onPlayerHealthChanged?.invoke(i, 0, 10);
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

    fun getPlayer(id: Int): Player {
        return players[id];
    }

    fun nextStage() {
        nextPlayerStage();
    }
}