package fr.epita.koh.game

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import fr.epita.koh.GameActivity
import fr.epita.koh.R
import java.util.*
import kotlin.math.max

class GameState(ctx : Context) {
    private var onPlayerTurnChanged: ((Int, Boolean) -> Unit)? = null

    private var onPlayerInsideTokyoChanged: ((Int, Boolean) -> Unit)? = null

    private var onPlayerHealthChanged: ((Int, Int, Int) -> Unit)? = null

    private var onPlayerVictoryPointsChanged: ((Int, Int, Int) -> Unit)? = null

    private var onPlayerEnergyChanged: ((Int, Int, Int) -> Unit)? = null

    private var onPlayerStateChanged: ((Int, PlayerState) -> Unit)? = null

    private val ctx : Context = ctx;

    private var playersLeft = 4;

    private var isFirstRound = true;

    private val cardsList = arrayOf(
        Card("Attack & Heal", "Attack&Heal", 7, EnumSet.of(
            CardEffect.ExtraAttack,
            CardEffect.ExtraHealTimes2
        ), R.drawable.card_0_img),

        Card("Extra Attack", "Attack all players", 4, EnumSet.of(
            CardEffect.ExtraAttack
        ), R.drawable.card_1_img),

        Card("Extra Heal", "Heal the buyer", 3, EnumSet.of(
            CardEffect.ExtraHealTimes2
        ), R.drawable.card_2_img),

        Card("Permanent Discount", "Permanent Discount for the buyer", 5, EnumSet.of(
            CardEffect.PermanentCostDiscount
        ), R.drawable.card_3_img),

        Card("Extra Dice Roll", "The player can roll the dice one extra time", 2, EnumSet.of(
            CardEffect.ExtraDiceRoll
        ), R.drawable.card_4_img),

        Card("Extra Point", "Give 3 victory points", 4, EnumSet.of(
            CardEffect.ExtraVictoryPointsTimes2
        ), R.drawable.card_5_img),

        Card("Extra Victory Points & Heal", "Give 3 victory points and 2 heal points", 5, EnumSet.of(
            CardEffect.ExtraVictoryPointsTimes2,
            CardEffect.ExtraHealTimes2
        ), R.drawable.card_6_img),

        Card("Extra Point & Dice", "Give 3 victory points and roll the dice one extra time", 6, EnumSet.of(
            CardEffect.ExtraVictoryPointsTimes2,
            CardEffect.ExtraDiceRoll
        ), R.drawable.card_1_img),

        Card("Extra Heal & Permanent Discount", "Give heal points and permanent discount", 8, EnumSet.of(
            CardEffect.ExtraVictoryPointsTimes2,
            CardEffect.PermanentCostDiscount
        ), R.drawable.card_2_img),

        Card("Extra Heal & Attack", "Heal points and attack players", 7, EnumSet.of(
            CardEffect.ExtraHealTimes2,
            CardEffect.ExtraAttack
        ), R.drawable.card_3_img),

        Card("Extra Point & Heal & Attack & Roll Dice & Permanent Discount", "MASTER CARD", 11, EnumSet.of(
            CardEffect.ExtraVictoryPointsTimes2,
            CardEffect.ExtraHealTimes2,
            CardEffect.ExtraDiceRoll,
            CardEffect.ExtraAttack,
            CardEffect.PermanentCostDiscount
        ), R.drawable.card_4_img)
    );

    private val players = arrayOf(
        Player(true),
        Player(false),
        Player(false),
        Player(false));

    private var currentPlayerTurn = -1;

    private var kingOfTheHill = -1;

    init {
        setPlayerTurn(0);
        isFirstRound = true;
    }

    private fun setPlayerTurn(newTurn : Int) : Boolean {
        isFirstRound = false;
        onPlayerTurnChanged?.invoke(newTurn, true);

        if (currentPlayerTurn >= 0) {
            players[currentPlayerTurn].playerState = PlayerState.WaitingTurn;
            onPlayerTurnChanged?.invoke(currentPlayerTurn, false);
        }

        currentPlayerTurn = newTurn;
        players[currentPlayerTurn].playerState = PlayerState.RollDice;

        if (kingOfTheHill == currentPlayerTurn) {
            val lastVP = players[currentPlayerTurn].playerVictoryPoints;
            players[currentPlayerTurn].playerVictoryPoints += 2;
            onPlayerVictoryPointsChanged?.invoke(currentPlayerTurn, lastVP,
                players[currentPlayerTurn].playerVictoryPoints)
        }

        return !players[currentPlayerTurn].playerDead;
    }

    private fun setPlayerStage(newStage : PlayerState) {
        players[currentPlayerTurn].playerState = newStage;
    }

    private fun nextPlayerStage() {
        when (players[currentPlayerTurn].playerState) {
            PlayerState.RollDice -> setPlayerStage(PlayerState.EnterNewYork)
            PlayerState.EnterNewYork -> setPlayerStage(PlayerState.BuyPowerCards);
            PlayerState.BuyPowerCards -> setPlayerStage(PlayerState.EndOfTurn);
            PlayerState.EndOfTurn -> {
                val startTurn = currentPlayerTurn;

                for (i in 0 until 4) {
                    if (setPlayerTurn((currentPlayerTurn + 1) % players.size)) {
                        break
                    };
                }

                if (startTurn == currentPlayerTurn) winGame();
            };
        }

        onPlayerStateChanged?.invoke(currentPlayerTurn, players[currentPlayerTurn].playerState);
    }

    private fun attackPlayers(attackerId : Int, count : Int) {
        if (kingOfTheHill == attackerId) {
            // Attack everyone else
            for (i in 0 until 4) {
                if (i != attackerId) {
                    val victim = players[i];
                    val oldHealth = victim.playerHealth;

                    victim.attack(count);

                    onPlayerHealthChanged?.invoke(i, oldHealth, victim.playerHealth);

                    if (oldHealth > 0 && victim.playerDead) onPlayerDead(i);
                }
            }
        } else if (kingOfTheHill >= 0) {
            // Attack the king of the hill
            val victim = players[kingOfTheHill];
            val oldHealth = victim.playerHealth;

            victim.attack(count);

            onPlayerHealthChanged?.invoke(kingOfTheHill, oldHealth, victim.playerHealth);

            if (victim.playerDead) {
                if (oldHealth > 0)
                    onPlayerDead(kingOfTheHill);
            }
            else {
                val dialogBuilder = AlertDialog.Builder(ctx)

                // set message of alert dialog
                dialogBuilder.setMessage("Le joueur " + (kingOfTheHill + 1) +
                        " peut choisir de quitter New York.")
                    // if the dialog is cancelable
                    .setCancelable(false)
                    // positive button text and action
                    .setPositiveButton("Rester", DialogInterface.OnClickListener {
                            d, _ -> run {d.cancel();}
                    })
                    // negative button text and action
                    .setNegativeButton("Quitter New York", DialogInterface.OnClickListener {
                            d, _ -> run {
                            enterKingOfTheHill(-1)
                            setPlayerStage(PlayerState.EnterNewYork);
                            onPlayerStateChanged?.invoke(currentPlayerTurn, PlayerState.EnterNewYork);
                            d.cancel();
                        }
                    })

                // create dialog box
                val alert = dialogBuilder.create();
                // set title for alert dialog box
                alert.setTitle("Quitter New York?");
                // show alert dialog
                alert.show();
            }
        }
    }

    private fun onPlayerDead(playerId: Int) {
        playersLeft -= 1;

        if (playerId == kingOfTheHill) {
            //King of the hill died, free spot
            onPlayerInsideTokyoChanged?.invoke(-1, true);
            kingOfTheHill = -1;
        }

        if (playersLeft == 1) winGame();
    }

    private fun healPlayer(playerId: Int) {
        val oldH = players[playerId].playerHealth;
        players[playerId].heal();
        onPlayerHealthChanged?.invoke(playerId, oldH, players[playerId].playerHealth);
    }

    private fun enterKingOfTheHill(playerId: Int) {
        if (kingOfTheHill >= 0)
            onPlayerInsideTokyoChanged?.invoke(kingOfTheHill, false);

        kingOfTheHill = playerId;

        if (kingOfTheHill >= 0) {
            val lastVP = players[playerId].playerVictoryPoints;
            players[playerId].playerVictoryPoints += 1;

            onPlayerVictoryPointsChanged?.invoke(
                playerId,
                lastVP,
                players[playerId].playerVictoryPoints
            )
        }

        onPlayerInsideTokyoChanged?.invoke(kingOfTheHill, true);
    }

    fun hasEnoughEnergy(card : Card) : Boolean {
        val player = players[currentPlayerTurn];
        val cost = max(1, card.cardCost - getCardDiscount());
        return player.playerEnergy >= cost;
    }

    private fun addVictoryPoints(playerId : Int, count : Int) {
        val lastVP = players[playerId].playerVictoryPoints;
        players[playerId].playerVictoryPoints += count;

        onPlayerVictoryPointsChanged?.invoke(
            playerId,
            lastVP,
            players[playerId].playerVictoryPoints
        )
    }

    fun buyCard(card : Card) : Boolean {
        if (!hasEnoughEnergy(card)) return  false;

        var attackCount = 0;
        val cost = max(1, card.cardCost - getCardDiscount());

        val current = players[currentPlayerTurn];
        val lastEnergy = current.playerEnergy;
        current.playerEnergy -= cost;
        onPlayerEnergyChanged?.invoke(currentPlayerTurn, lastEnergy, current.playerEnergy);

        for (fx in card.cardEffect) {
            when(fx) {
                CardEffect.ExtraAttack -> attackCount += 1;
                CardEffect.ExtraHealTimes2 -> {
                    healPlayer(currentPlayerTurn)
                    healPlayer(currentPlayerTurn)
                }
                CardEffect.ExtraVictoryPointsTimes2 -> {
                    addVictoryPoints(currentPlayerTurn, 2);
                }
                CardEffect.ExtraDiceRoll -> {
                    players[currentPlayerTurn].extraDiceRoll += 1;
                }
                CardEffect.TakeAway2VictoryPointsAll -> {
                    addVictoryPoints(0, -2);
                    addVictoryPoints(1, -2);
                    addVictoryPoints(2, -2);
                    addVictoryPoints(3, -2);
                }
                CardEffect.StealKingSpot -> {
                    enterKingOfTheHill(currentPlayerTurn);
                }
                CardEffect.PermanentCostDiscount -> {
                    players[currentPlayerTurn].cardDiscount += 1;
                }
            }
        }

        if (attackCount > 0) {
            attackPlayers(currentPlayerTurn, attackCount);
        }

        return true;
    }

    fun getCardDiscount() : Int {
        if (currentPlayerTurn < 0) return  0;
        return players[currentPlayerTurn].cardDiscount;
    }

    fun getExtraDiceRoll() : Int {
        if (currentPlayerTurn < 0) return  0;
        return players[currentPlayerTurn].extraDiceRoll;
    }

    fun isFirstRound() : Boolean {
        return isFirstRound;
    }

    fun winGame() {
        setPlayerStage(PlayerState.Winner);
        onPlayerStateChanged?.invoke(currentPlayerTurn, PlayerState.Winner);
    }

    fun enterKingOfTheHill() {
        enterKingOfTheHill(currentPlayerTurn);
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
        var attackCount = 0;

        for (dice in dice) {
            when (dice.diceState) {
                DiceState.ONE -> ++oneCount
                DiceState.TWO -> ++twoCount
                DiceState.THREE -> ++threeCount
                DiceState.ATTACK -> ++attackCount;
                DiceState.HEAL -> if (kingOfTheHill != currentPlayerTurn) healPlayer(currentPlayerTurn)
                DiceState.ENERGY -> {
                    val lastEnergy = current.playerEnergy;
                    ++current.playerEnergy;
                    onPlayerEnergyChanged?.invoke(currentPlayerTurn, lastEnergy, current.playerEnergy);
                }
            }
        }

        if (attackCount > 0)
            attackPlayers(currentPlayerTurn, attackCount);

        val oldVP = current.playerVictoryPoints;

        if (oneCount >= 1) {
            current.playerVictoryPoints += oneCount;
            onPlayerVictoryPointsChanged?.invoke(currentPlayerTurn, oldVP, current.playerVictoryPoints);
        }

        if (twoCount >= 2) {
            current.playerVictoryPoints += twoCount;
            onPlayerVictoryPointsChanged?.invoke(currentPlayerTurn, oldVP, current.playerVictoryPoints);
        }

        if (threeCount >= 3) {
            current.playerVictoryPoints += threeCount;
            onPlayerVictoryPointsChanged?.invoke(currentPlayerTurn, oldVP, current.playerVictoryPoints);
        }
    }

    fun reset() {
        currentPlayerTurn = -1;
        playersLeft = 4;

        for (i in 0 until 4) {
            onPlayerTurnChanged?.invoke(i, false);
            onPlayerHealthChanged?.invoke(i, 0, 10);
            onPlayerVictoryPointsChanged?.invoke(i, 0, 0);
            onPlayerEnergyChanged?.invoke(i, 0, 0);
        }

        for (p in players) {
            p.reset();
        }

        setPlayerTurn(0);
        isFirstRound = true;
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

    fun canBecomeKingOfNY(): Boolean {
        return kingOfTheHill < 0;
    }

    fun isKing(id: Int): Boolean {
        return kingOfTheHill == id;
    }

    fun getCurrentTurn() : Int {
        return currentPlayerTurn;
    }
}