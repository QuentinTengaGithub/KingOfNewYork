package fr.epita.koh.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import fr.epita.koh.GameActivity
import fr.epita.koh.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MoveToTopOfHill : Fragment() {

    companion object {
        fun newInstance() = MoveToTopOfHill()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_move_to_top_of_hill, container, false);
        val btn = view.findViewById<Button>(R.id.next_player_btn);
        val activity: GameActivity = activity as GameActivity

        if (activity.canBecomeKingOfNY()) {
            btn.setOnClickListener {
                activity.becomeKingOfNY();
            };
        } else {
            /*Timer().schedule(timerTask {
                activity.skipStage();
            }, 1000)*/
            activity.skipStage();
        }

        return view
    }
}