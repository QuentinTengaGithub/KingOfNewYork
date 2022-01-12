package fr.epita.koh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.epita.koh.R
import fr.epita.koh.game.GameState

class GameActivity : AppCompatActivity() {

    val gameState = GameState();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
    }
}