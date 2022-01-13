package fr.epita.koh

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import fr.epita.koh.game.GameState

class GameActivity : AppCompatActivity() {

    private val gameState = GameState();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameState.reset();

        findViewById<Button>(R.id.roll_dice).setOnClickListener{ onRollDice(); }
    }

    private fun onRollDice() {
        gameState.play();
    }
}