package fr.epita.koh.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import fr.epita.koh.GameActivity
import fr.epita.koh.R

class NextPlayerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_next_player, container, false);
        val activity: GameActivity = activity as GameActivity

        view.findViewById<Button>(R.id.next_player_btn).setOnClickListener {
            activity.skipStage();
        }

        return view
    }
}