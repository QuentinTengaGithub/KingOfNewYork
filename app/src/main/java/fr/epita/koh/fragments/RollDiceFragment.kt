package fr.epita.koh.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.card.MaterialCardView
import fr.epita.koh.GameActivity
import fr.epita.koh.R
import fr.epita.koh.game.Dice
import fr.epita.koh.game.DiceState

class RollDiceFragment : Fragment() {

    companion object {
        fun newInstance() = RollDiceFragment()
    }

    private val dice = arrayOf<Dice>(
        Dice(), Dice(), Dice(), Dice(), Dice(), Dice()
    );

    private val diceCards = mutableListOf<MaterialCardView>();

    private val diceImages = mutableListOf<ImageView>();

    private var throws = 3;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.roll_dice_fragment, container, false);

        view.findViewById<Button>(R.id.commit_dice_btn)
            .setOnClickListener { onCommitDicePressed() };

        view.findViewById<Button>(R.id.roll_dice_fragment_btn)
            .setOnClickListener { onRollDicePressed() };

        diceCards.clear();

        diceCards.add(view.findViewById(R.id.dice_0));
        diceCards.add(view.findViewById(R.id.dice_1));
        diceCards.add(view.findViewById(R.id.dice_2));
        diceCards.add(view.findViewById(R.id.dice_3));
        diceCards.add(view.findViewById(R.id.dice_4));
        diceCards.add(view.findViewById(R.id.dice_5));

        diceImages.clear();

        diceImages.add(view.findViewById(R.id.dice_0_img));
        diceImages.add(view.findViewById(R.id.dice_1_img));
        diceImages.add(view.findViewById(R.id.dice_2_img));
        diceImages.add(view.findViewById(R.id.dice_3_img));
        diceImages.add(view.findViewById(R.id.dice_4_img));
        diceImages.add(view.findViewById(R.id.dice_5_img));

        for ((i, card) in diceCards.withIndex()) {
            card.setOnClickListener{
                onDiceClicked(i);
            }
        }

        updateDiceImages();

        return view;
    }

    private fun onCommitDicePressed() {
        val activity: GameActivity = activity as GameActivity
        activity.onCommittedDice(dice);
    }

    private fun updateDiceImages() {
        for ((i, dice) in dice.withIndex()) {
            when (dice.diceState) {
                DiceState.ONE -> diceImages[i].setImageResource(R.drawable.dice_2);
                DiceState.TWO -> diceImages[i].setImageResource(R.drawable.dice_4);
                DiceState.THREE -> diceImages[i].setImageResource(R.drawable.dice_3);
                DiceState.ATTACK -> diceImages[i].setImageResource(R.drawable.dice_6);
                DiceState.HEAL -> diceImages[i].setImageResource(R.drawable.dice_5);
                DiceState.ENERGY -> diceImages[i].setImageResource(R.drawable.dice_1);
            }
        }
    }

    private fun onDiceClicked(index : Int) {
        val d = dice[index];
        val card = diceCards[index];

        d.diceSelected = !d.diceSelected;

        if (d.diceSelected) {
            card.strokeWidth = 15;
        } else {
            card.strokeWidth = 0;
        }
    }

    private fun onRollDicePressed() {
        if (throws > 0) {
            throws -= 1;

            for (dice in dice) {
                if (!dice.diceSelected)
                    dice.generateRandomState();
            }

            updateDiceImages();
        }

        if (throws <= 0) {
            requireView().findViewById<Button>(R.id.roll_dice_fragment_btn).isEnabled = false;
        }
    }
}