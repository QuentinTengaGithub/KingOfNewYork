package fr.epita.koh.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import fr.epita.koh.GameActivity
import fr.epita.koh.R

class BuyCardsFragment : Fragment() {

    companion object {
        fun newInstance() = BuyCardsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.buy_cards_fragment, container, false);
        val activity: GameActivity = activity as GameActivity

        view.findViewById<Button>(R.id.shop_quit).setOnClickListener {
            activity.skipStage();
        }

        return view
    }
}