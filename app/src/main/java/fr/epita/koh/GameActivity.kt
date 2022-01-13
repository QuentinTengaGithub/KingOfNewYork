package fr.epita.koh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import fr.epita.koh.fragments.BuyCardsFragment
import fr.epita.koh.fragments.MoveToTopOfHill
import fr.epita.koh.fragments.RollDiceFragment
import fr.epita.koh.game.Dice
import fr.epita.koh.game.GameState
import fr.epita.koh.game.PlayerState

class GameActivity : AppCompatActivity() {

    private val gameState = GameState();

    private val playerCards = mutableListOf<MaterialCardView>();

    private val playerHealths = mutableListOf<Chip>();

    private val playerPoints = mutableListOf<Chip>();

    private val rollDiceScreen = RollDiceFragment();

    private val moveToTopScreen = MoveToTopOfHill();

    private val buyPowerCardsScreen = BuyCardsFragment();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Reset & load all the views
        playerCards.clear();
        playerCards.add(findViewById(R.id.PlayerOneCard));
        playerCards.add(findViewById(R.id.PlayerTwoCard));
        playerCards.add(findViewById(R.id.PlayerThreeCard));
        playerCards.add(findViewById(R.id.PlayerFourCard));

        playerHealths.clear();
        playerHealths.add(findViewById(R.id.PlayerOneHealth));
        playerHealths.add(findViewById(R.id.PlayerTwoHealth));
        playerHealths.add(findViewById(R.id.PlayerThreeHealth));
        playerHealths.add(findViewById(R.id.PlayerFourHealth));

        playerPoints.clear();
        playerPoints.add(findViewById(R.id.PlayerOnePoints));
        playerPoints.add(findViewById(R.id.PlayerTwoPoints));
        playerPoints.add(findViewById(R.id.PlayerThreePoints));
        playerPoints.add(findViewById(R.id.PlayerFourPoints));

        // Game events
        gameState.onPlayerTurnChanged { id, hisTurn -> onPlayerTurnChanged(id, hisTurn) };
        gameState.onPlayerHealthChanged { id, old, new -> onPlayerHealthChanged(id, old, new) };
        gameState.onPlayerInsideTokyoChanged { id, inside -> onPlayerInsideTokyoChanged(id, inside) };
        gameState.onPlayerStateChanged { id, state -> onPlayerStateUpdate(id, state) };

        // Resets the state
        gameState.reset();
    }

    fun onCommittedDice(dice : Array<Dice>) {
        gameState.onCommittedDice(dice);
        gameState.nextStage();
    }

    private fun onPlayerStateUpdate(id: Int, state: PlayerState) {
        when (state) {
            PlayerState.RollDice -> changeScreen(rollDiceScreen);
            PlayerState.EnterNewYork -> changeScreen(moveToTopScreen);
            PlayerState.BuyPowerCards -> changeScreen(buyPowerCardsScreen);
            else -> changeScreen(rollDiceScreen);
        }

        println(state);
    }

    private fun changeScreen(frag : Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.userInterfaceFragment, frag)
            .commit();
    }

    private fun onPlayerInsideTokyoChanged(id : Int, inside : Boolean)
    {
        val card = playerCards[id];

        if (inside || gameState.getPlayer(id).playerDead) {
            card.alpha = 0.5f;
        } else {
            card.alpha = 1f;
        }
    }

    private fun onPlayerTurnChanged(id : Int, hisTurn : Boolean)
    {
        val card = playerCards[id];

        if (hisTurn) {
            bounceView(card);
            card.strokeWidth = 15;
        } else {
            resetBounceView(card);
            card.strokeWidth = 0;
        }
    }

    private fun onPlayerHealthChanged(id : Int, oldHealth : Int, newHealth : Int)
    {
        val healthChip = playerHealths[id];
        healthChip.text = newHealth.toString();

        if (oldHealth > newHealth)
            shakeView(healthChip);
    }

    private fun shakeView(view : View) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

    private fun bounceView(view : View) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce));
    }

    private fun resetBounceView(view : View) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce_reset));
    }

    private fun onRollDice() {
        gameState.play();
    }

    fun getDiceScreen(view: View) {
        onRollDice();
    }
}