package fr.epita.koh.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import fr.epita.koh.GameActivity
import fr.epita.koh.R
import fr.epita.koh.game.Card

class BuyCardsFragment : Fragment() {

    companion object {
        fun newInstance() = BuyCardsFragment()
    }

    private fun updateCardCost(view : View, card : Card, discount : Int, rid : Int) {
        val cardCost = view.findViewById<Chip>(rid);
        if (card.cardCost - discount < 1) {
            cardCost.text = "1";
        }
        else {
            val cost = card.cardCost - discount;
            cardCost.text = cost.toString();
        }
    }

    private fun updateCardImg(view : View, card : Card, rid : Int) {
        val view = view.findViewById<ImageView>(rid);
        view.setImageResource(card.cardImageId);
    }

    private fun updateCardTitle(view : View, card : Card, rid : Int) {
        val view = view.findViewById<TextView>(rid);
        view.text = card.cardTitle;
    }

    private fun updateCardDesc(view : View, card : Card, rid : Int) {
        val view = view.findViewById<TextView>(rid);
        view.text = card.cardDescription;
    }

    private fun updateUI(view : View) {
        val activity: GameActivity = activity as GameActivity

        val discount = activity.getDiscount();

        val carte_0 = activity.getGameState().getCard(0);
        val carte_1 = activity.getGameState().getCard(1);
        val carte_2 = activity.getGameState().getCard(2);

        updateCardCost(view, carte_0, discount, R.id.CardOneCost);
        updateCardCost(view, carte_1, discount, R.id.CardTwoCost);
        updateCardCost(view, carte_2, discount, R.id.CardThreeCost);

        updateCardImg(view, carte_0, R.id.CardOneImg);
        updateCardImg(view, carte_1, R.id.CardTwoImg);
        updateCardImg(view, carte_2, R.id.CardThreeImg);

        updateCardTitle(view, carte_0, R.id.CardOneTitle);
        updateCardDesc(view, carte_0, R.id.CardOneDesc);

        updateCardTitle(view, carte_1, R.id.CardTwoTitle);
        updateCardDesc(view, carte_1, R.id.CardTwoDesc);

        updateCardTitle(view, carte_2, R.id.CardThreeTitle);
        updateCardDesc(view, carte_2, R.id.CardThreeDesc);

        val cardOne = view.findViewById<MaterialCardView>(R.id.CardOne);
        val cardTwo = view.findViewById<MaterialCardView>(R.id.CardTwo);
        val cardThree = view.findViewById<MaterialCardView>(R.id.CardThree);

        if (activity.hasEnoughEnergy(carte_0)) {
            cardOne.isActivated = true;
            cardOne.alpha = 1f;
            cardOne.setOnClickListener {
                activity.getGameState().buyCard(carte_0);
                activity.getGameState().shuffleCardId(0);
                updateUI(view);
            }
        } else {
            cardOne.alpha = 0.5f;
            cardOne.isActivated = false
        };

        if (activity.hasEnoughEnergy(carte_1)) {
            cardTwo.isActivated = true;
            cardTwo.alpha = 1f;
            cardTwo.setOnClickListener {
                activity.getGameState().buyCard(carte_1);
                activity.getGameState().shuffleCardId(1);
                updateUI(view);
            }
        } else {
            cardTwo.alpha = 0.5f;
            cardTwo.isActivated = false
        };

        if (activity.hasEnoughEnergy(carte_2)) {
            cardThree.isActivated = true;
            cardThree.alpha = 1f;
            cardThree.setOnClickListener {
                activity.getGameState().buyCard(carte_2);
                activity.getGameState().shuffleCardId(2);
                updateUI(view);
            }
        } else {
            cardThree.alpha = 0.5f;
            cardThree.isActivated = false
        };
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

        updateUI(view);

        return view
    }
}