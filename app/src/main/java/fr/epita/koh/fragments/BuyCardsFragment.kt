package fr.epita.koh.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.epita.koh.R

class BuyCardsFragment : Fragment() {

    companion object {
        fun newInstance() = BuyCardsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.buy_cards_fragment, container, false)
    }
}