package fr.epita.koh;

import android.annotation.SuppressLint;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import fr.epita.koh.databinding.ActivityDiceBinding;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DiceActivity extends AppCompatActivity {
    public static Button button;
    public static ImageButton button_1, button_2, button_3, button_4, button_5, button_6;
    public static TextView textView;
    public static ImageView img1, img2, img3, img4, img5, img6;
    public int nb_dice_selectioned = 0;
    int[] intArray = new int[]{ -1, -1, -1, -1, -1, -1 };
    int[] secondArray = new int[]{ -1, -1, -1, -1, -1, -1 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        // array to store dice images
        final int dice[] = {R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3,
                R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6};

        // linking the roll button from its id..
        button = findViewById(R.id.btVar1);
        button_1 = findViewById(R.id.ivVar1);
        button_2 = findViewById(R.id.ivVar2);
        button_3 = findViewById(R.id.ivVar3);
        button_4 = findViewById(R.id.ivVar4);
        button_5 = findViewById(R.id.ivVar5);
        button_6 = findViewById(R.id.ivVar6);

        // linking the result textview from its id..
        textView = findViewById(R.id.tvVar1);

        // linking both the imageView of
        // the dice images from its id..
        img1 = findViewById(R.id.ivVar1);
        img2 = findViewById(R.id.ivVar2);
        img3 = findViewById(R.id.ivVar3);
        img4 = findViewById(R.id.ivVar4);
        img5 = findViewById(R.id.ivVar5);
        img6 = findViewById(R.id.ivVar6);

        // call the on click function
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // generate two random numbers
                // using Random function
                Random random = new Random();
                int num1 = random.nextInt(6);
                Random random1 = new Random();
                int num2 = random.nextInt(6);
                Random random2 = new Random();
                int num3 = random.nextInt(6);
                Random random3 = new Random();
                int num4 = random.nextInt(6);
                Random random4 = new Random();
                int num5 = random.nextInt(6);
                Random random5 = new Random();
                int num6 = random.nextInt(6);
                intArray[0] = num1;
                intArray[1] = num2;
                intArray[2] = num3;
                intArray[3] = num4;
                intArray[4] = num5;
                intArray[5] = num6;

                // the bigger number will be displayed in the
                // textView as the winner otherwise draw..
                if (num1 > num2) {
                    textView.setText("WINNER : Player 1");
                } else if (num2 > num1) {
                    textView.setText("WINNER : Player 2");
                } else {
                    textView.setText("RESULT : Draw");
                }
                // set the images from the array by the index
                // position of the numbers generated
                if (secondArray[0] == -1) {
                    img1.setImageResource(dice[num1]);
                }
                if (secondArray[1] == -1) {
                    img2.setImageResource(dice[num2]);
                }
                if (secondArray[2] == -1) {
                    img3.setImageResource(dice[num3]);
                }
                if (secondArray[3] == -1) {
                    img4.setImageResource(dice[num4]);
                }
                if (secondArray[4] == -1) {
                    img5.setImageResource(dice[num5]);
                }
                if (secondArray[5] == -1) {
                    img6.setImageResource(dice[num6]);
                }
                button_1.setBackgroundColor(Color.parseColor("#2596be"));
                button_2.setBackgroundColor(Color.parseColor("#2596be"));
                button_3.setBackgroundColor(Color.parseColor("#2596be"));
                button_4.setBackgroundColor(Color.parseColor("#2596be"));
                button_5.setBackgroundColor(Color.parseColor("#2596be"));
                button_6.setBackgroundColor(Color.parseColor("#2596be"));
                nb_dice_selectioned = 0;
                button_1.setEnabled(true);
                button_2.setEnabled(true);
                button_3.setEnabled(true);
                button_4.setEnabled(true);
                button_5.setEnabled(true);
                button_6.setEnabled(true);
                for (int i = 0; i < 6; i++) {
                    secondArray[i] = -1;
                }


            }

        });
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nb_dice_selectioned  < 3){
                    button_1.setBackgroundColor(Color.parseColor("#F8AF6B"));
                    nb_dice_selectioned++;
                    Integer x = nb_dice_selectioned;
                    String y = x.toString();
                    textView.setText(y);
                    secondArray[0] = intArray[0];
                }
                else {
                    button_1.setEnabled(false);
                    button_2.setEnabled(false);
                    button_3.setEnabled(false);
                    button_4.setEnabled(false);
                    button_5.setEnabled(false);
                    button_6.setEnabled(false);
                }
            }
        });
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nb_dice_selectioned  < 3){
                    button_2.setBackgroundColor(Color.parseColor("#F8AF6B"));
                    nb_dice_selectioned++;
                    Integer x = nb_dice_selectioned;
                    String y = x.toString();
                    textView.setText(y);
                    secondArray[1] = intArray[1];
                }
                else {
                    button_1.setEnabled(false);
                    button_2.setEnabled(false);
                    button_3.setEnabled(false);
                    button_4.setEnabled(false);
                    button_5.setEnabled(false);
                    button_6.setEnabled(false);
                }
            }
        });
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nb_dice_selectioned  < 3){
                    button_3.setBackgroundColor(Color.parseColor("#F8AF6B"));
                    nb_dice_selectioned++;
                    Integer x = nb_dice_selectioned;
                    String y = x.toString();
                    textView.setText(y);
                    secondArray[2] = intArray[2];
                }
                else {
                    button_1.setEnabled(false);
                    button_2.setEnabled(false);
                    button_3.setEnabled(false);
                    button_4.setEnabled(false);
                    button_5.setEnabled(false);
                    button_6.setEnabled(false);
                }
            }
        });
        button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nb_dice_selectioned  < 3){
                    button_4.setBackgroundColor(Color.parseColor("#F8AF6B"));
                    nb_dice_selectioned++;
                    Integer x = nb_dice_selectioned;
                    String y = x.toString();
                    textView.setText(y);
                    secondArray[3] = intArray[3];
                }
                else {
                    button_1.setEnabled(false);
                    button_2.setEnabled(false);
                    button_3.setEnabled(false);
                    button_4.setEnabled(false);
                    button_5.setEnabled(false);
                    button_6.setEnabled(false);
                }
            }
        });
        button_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nb_dice_selectioned  < 3){
                    button_5.setBackgroundColor(Color.parseColor("#F8AF6B"));
                    nb_dice_selectioned++;
                    Integer x = nb_dice_selectioned;
                    String y = x.toString();
                    textView.setText(y);
                    secondArray[4] = intArray[4];
                }
                else {
                    button_1.setEnabled(false);
                    button_2.setEnabled(false);
                    button_3.setEnabled(false);
                    button_4.setEnabled(false);
                    button_5.setEnabled(false);
                    button_6.setEnabled(false);
                }
            }
        });
        button_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nb_dice_selectioned  < 3){
                    button_6.setBackgroundColor(Color.parseColor("#F8AF6B"));
                    nb_dice_selectioned++;
                    Integer x = nb_dice_selectioned;
                    String y = x.toString();
                    textView.setText(y);
                    secondArray[5] = intArray[5];
                }
                else {
                    button_1.setEnabled(false);
                    button_2.setEnabled(false);
                    button_3.setEnabled(false);
                    button_4.setEnabled(false);
                    button_5.setEnabled(false);
                    button_6.setEnabled(false);
                }
            }
        });

    }
}