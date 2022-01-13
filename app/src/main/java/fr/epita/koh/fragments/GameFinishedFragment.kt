package fr.epita.koh.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import fr.epita.koh.GameActivity
import fr.epita.koh.MainActivity
import fr.epita.koh.R

class GameFinishedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_finished, container, false);
        val activity: GameActivity = activity as GameActivity

        view.findViewById<Button>(R.id.back_to_menu_btn)
            .setOnClickListener {
                activity.getMenuScreen();
            }

        view.findViewById<TextView>(R.id.winner_txt).text =
            "Le joueur " + activity.currentPlayerID() + " à gagné!";

        return view;
    }
}