package fr.epita.koh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import fr.epita.koh.game.GameState

class GameActivity : AppCompatActivity() {

    private val gameState = GameState();

    private val playerCards = mutableListOf<MaterialCardView>();

    private val playerHealths = mutableListOf<Chip>();

    private val playerPoints = mutableListOf<Chip>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

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

        gameState.onPlayerTurnChanged { id, hisTurn -> onPlayerTurnChanged(id, hisTurn) };
        gameState.onPlayerHealthChanged { id, old, new -> onPlayerHealthChanged(id, old, new) };

        gameState.reset();
    }

    private fun onPlayerTurnChanged(id : Int, hisTurn : Boolean)
    {
        val card = playerCards[id];

        if (hisTurn) {
            card.strokeWidth = 15;
        } else {
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

    private fun onRollDice() {
        gameState.play();
    }

    fun getDiceScreen(view: View) {
        /*val intent =  Intent(this, DiceActivity::class.java).apply {
        }
        startActivity(intent)*/

        onRollDice();

        //findViewById<Button>(R.id.roll_dice).startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
    }
}