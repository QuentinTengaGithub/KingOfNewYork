package fr.epita.koh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fr.epita.koh.R
import fr.epita.koh.game.GameState

class GameActivity : AppCompatActivity() {

    val gameState = GameState();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
    }
    fun getDiceScreen(view: View) {
        val intent =  Intent(this, DiceActivity::class.java).apply {
        }
        startActivity(intent)
    }
}