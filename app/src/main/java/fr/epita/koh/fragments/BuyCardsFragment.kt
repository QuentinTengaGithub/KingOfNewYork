package fr.epita.koh.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.chip.Chip
import fr.epita.koh.GameActivity
import fr.epita.koh.R
import fr.epita.koh.game.Card
import fr.epita.koh.game.CardEffect
import java.util.*

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
        val discount = activity.getDiscount();
        val carte_0 = Card("FstCard", "ntm", 5, EnumSet.of(CardEffect.ExtraAttack), R.drawable.card_0_img);
        val carte_1 = Card("SecCard", "Heal", 3, EnumSet.of(CardEffect.ExtraHealTimes2), R.drawable.card_1_img);
        val carte_2 = Card("ThirdCard", "Victory", 5, EnumSet.of(CardEffect.ExtraVictoryPointsTimes2), R.drawable.card_2_img);

        val chip_1 = view.findViewById<Chip>(R.id.CardOneCost);
        if (carte_0.cardCost - discount < 1) {
            chip_1.setText("1");
        }
        else {
            val discount_price = carte_0.cardCost - discount;
            chip_1.setText(discount_price.toString());
        }
        val chip_2 = view.findViewById<Chip>(R.id.CardTwoCost);
        if (carte_1.cardCost - discount < 1) {
            chip_2.setText("1");
        }
        else {
            val discount_price = carte_1.cardCost - discount;
            chip_2.setText(discount_price.toString());
        }
        val chip_3 = view.findViewById<Chip>(R.id.CardThreeCost);
        if (carte_2.cardCost - discount < 1) {
            chip_3.setText("1");
        }
        else {
            val discount_price = carte_2.cardCost - discount;
            chip_3.setText(discount_price.toString());
        }



        return view
    }
}