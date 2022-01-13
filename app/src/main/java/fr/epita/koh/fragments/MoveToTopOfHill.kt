package fr.epita.koh.fragments

import android.opengl.Visibility
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
        val btn = view.findViewById<Button>(R.id.back_to_menu_btn);
        val activity: GameActivity = activity as GameActivity

        val stayPutBtn = view.findViewById<Button>(R.id.dont_go_to_hill);

        if (activity.isFirstRound())
        {
            stayPutBtn.visibility = View.INVISIBLE;
            stayPutBtn.isActivated = false;
        }
        else
        {
            stayPutBtn.setOnClickListener {
                activity.skipStage();
            }
        }

        if (activity.canBecomeKingOfNY()) {
            btn.setOnClickListener {
                activity.becomeKingOfNY();
            };
        } else {
            activity.skipStage();
        }

        return view
    }
}