package fr.epita.koh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import fr.epita.koh.fragments.*
import fr.epita.koh.game.Dice
import fr.epita.koh.game.GameState
import fr.epita.koh.game.PlayerState

class GameActivity : AppCompatActivity() {

    private val gameState = GameState(this);

    private val playerCards = mutableListOf<MaterialCardView>();

    private val playerImages = mutableListOf<ImageView>();

    private val playerHealths = mutableListOf<Chip>();

    private val playerPoints = mutableListOf<Chip>();

    private var insideTokyoImage : ImageView? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        insideTokyoImage = findViewById(R.id.KingOfTheHillImage);

        // Reset & load all the views
        playerImages.clear();
        playerImages.add(findViewById(R.id.PlayerOne));
        playerImages.add(findViewById(R.id.PlayerTwo));
        playerImages.add(findViewById(R.id.PlayerThree));
        playerImages.add(findViewById(R.id.PlayerFour));

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
        gameState.onPlayerTurnChanged { id, hisTurn -> onPlayerTurnChanged(id, hisTurn); };
        gameState.onPlayerHealthChanged { id, old, new -> onPlayerHealthChanged(id, old, new); };
        gameState.onPlayerInsideTokyoChanged { id, inside -> onPlayerInsideTokyoChanged(id, inside); };
        gameState.onPlayerStateChanged { id, state -> onPlayerStateUpdate(id, state); };
        gameState.onPlayerVictoryPointsChanged { id, o, n -> onPlayerVPChanged(id, o, n); };

        // Resets the state
        gameState.reset();
    }

    fun getMenuScreen() {
        val intent = Intent(this, MainActivity::class.java).apply {}
        startActivity(intent)
    }

    fun onCommittedDice(dice : Array<Dice>) {
        gameState.onCommittedDice(dice);
        gameState.nextStage();
    }

    private fun onPlayerStateUpdate(id: Int, state: PlayerState) {
        when (state) {
            PlayerState.RollDice -> changeScreen(RollDiceFragment());
            PlayerState.EnterNewYork -> changeScreen(MoveToTopOfHill());
            PlayerState.BuyPowerCards -> changeScreen(BuyCardsFragment());
            PlayerState.Winner -> changeScreen(GameFinishedFragment());
            else -> changeScreen(NextPlayerFragment());
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
        if (id < 0) {
            if (inside) insideTokyoImage!!.setImageResource(0);
        } else {
            val card = playerCards[id];

            if (inside) {

                if (gameState.getPlayer(id).playerDead)
                    card.alpha = 0f;
                else card.alpha = 0.5f;

                insideTokyoImage!!.setImageDrawable(playerImages[id].drawable)
            } else {
                card.alpha = 1f;
            }
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

    private fun onPlayerVPChanged(id : Int, old : Int, new : Int)
    {
        if (new >= 20) {
            gameState.winGame();
        }
        val chip = playerPoints[id];
        chip.text = new.toString();

        if (new > old)
            shakeView(chip);
    }

    private fun onPlayerHealthChanged(id : Int, oldHealth : Int, newHealth : Int)
    {
        val healthChip = playerHealths[id];
        val card = playerCards[id];
        healthChip.text = newHealth.toString();

        if (oldHealth > newHealth)
            shakeView(healthChip);

        if (gameState.getPlayer(id).playerDead)
            card.alpha = 0f;
        else {
            if (gameState.isKing(id))
                card.alpha = 0.5f;
            else card.alpha = 1f;
        };
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

    fun becomeKingOfNY() {
        gameState.enterKingOfTheHill();
        skipStage();
    }

    fun skipStage() {
        gameState.nextStage();
    }

    fun isFirstRound() : Boolean {
        return gameState.isFirstRound();
    }

    fun canBecomeKingOfNY(): Boolean {
        return gameState.canBecomeKingOfNY();
    }

    fun currentPlayerID() : Int {
        return gameState.getCurrentTurn();
    }
}