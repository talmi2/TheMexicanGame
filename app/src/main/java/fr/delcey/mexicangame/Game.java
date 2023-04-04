package fr.delcey.mexicangame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import DataBase.DBHandler;
import DataBase.Player;

public class Game extends AppCompatActivity {

    private ImageView dice1, dice2;
    private Button rollButton, next_player_button, comeback, btnviewAll, roll_again, mBackButton, mReveal;
    private EditText playerInput;
    private TextView playerName;
    DBHandler myDb;
    private Drawable previousDice1;
    private Drawable previousDice2;
    int currentPlayerId = 1 ;
    int currentScore = 1;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dice1 = findViewById(R.id.dice1);
        dice2 = findViewById(R.id.dice2);
        rollButton = findViewById(R.id.roll_button);
        playerName = findViewById(R.id.player_name);
        playerInput = findViewById(R.id.main_edittext_num1);
        next_player_button = findViewById(R.id.next_player);
        comeback = findViewById(R.id.back_button);
        btnviewAll = findViewById(R.id.view_all_button);
        roll_again = findViewById(R.id.roll_again);
        mBackButton = findViewById(R.id.back_button);
        mReveal = findViewById(R.id.print_game);

//        InputFilter[] filters = new InputFilter[1];
//        filters[0] = new InputFilter.LengthFilter(2);
//        playerInput.setFilters(filters);

        //int[] scoring = {32, 41, 42 ,43, 51, 52, 53, 54, 61, 62, 63, 64, 65, 11, 22, 33, 44, 55, 66, 21};


        next_player_button.setEnabled(false);
        roll_again.setEnabled(false);

        myDb = new DBHandler(this);
        viewAll();

        Player player = myDb.getPlayerName(currentPlayerId);
        currentPlayerId = player.getId();


        playerName = findViewById(R.id.player_name);
        playerName.setText(player.getName() + " - " + player.getScore());


        String strNumber = playerInput.getText().toString().trim();

        currentScore = calculateScore(strNumber);




        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(Game.this, MainActivity.class),5);
            }
        });

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice();
                // save the current image of dice1
                previousDice1 = dice1.getDrawable();

                // save the current image of dice2
                previousDice2 = dice2.getDrawable();


                roll_again.setEnabled(true);
                rollButton.setEnabled(false);
                mReveal.setEnabled(false);

            }
        });

        roll_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice();
                // save the current image of dice1
                previousDice1 = dice1.getDrawable();

                // save the current image of dice2
                previousDice2 = dice2.getDrawable();
                dice1.setVisibility(View.INVISIBLE);
                dice2.setVisibility(View.INVISIBLE);
                roll_again.setEnabled(false);
                mReveal.setEnabled(false);



            }
        });

        playerInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                next_player_button.setEnabled(!s.toString().isEmpty());
                if (!s.toString().isEmpty()) {
                        Intent intent = getIntent();
                        // receive the value by getStringExtra() method and
                        // key must be same which is send by first activity
                        int str = intent.getIntExtra("Score",currentScore);

                        int score_previous_player = str;
                        int input = Integer.parseInt(s.toString());
                        int score = calculateScore(String.valueOf(input));
                        if (score < score_previous_player) {
                            playerInput.setError("the score must be greater than or equal to that of the previous player " + getScore(score_previous_player));
                        }
//                        else if(score_previous_player == 21){
//                            joker
//                        }
//                        else if(score_previous_player == 20){
//                            mexicain
//                        }
                    }

                }
        });
        next_player_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strNumber = playerInput.getText().toString().trim();
                if(TextUtils.isEmpty(strNumber)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
                    builder.setTitle("Error");
                    builder.setMessage("You need yo enter a number between 2 and 9");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
                else{

                    int[] actual = getDiceScore(previousDice1, previousDice2);

                    if(actual[0] > actual[1]){
                        dice1.setImageDrawable(previousDice1);

                        // set the image of dice2 to the previous image
                        dice2.setImageDrawable(previousDice2);
                    }
                    else{
                        dice1.setImageDrawable(previousDice2);

                        // set the image of dice2 to the previous image
                        dice2.setImageDrawable(previousDice1);
                    }

                    dice1.setVisibility(View.INVISIBLE);
                    dice2.setVisibility(View.INVISIBLE);

                    rollButton.setEnabled(true);
                    roll_again.setEnabled(false);
                    mReveal.setEnabled(true);


                    nextPlayer();
                    // Add the player ID as an extra to the Intent
                    Intent intent = new Intent(Game.this, Game.class);
                    currentScore = calculateScore(strNumber);
                    intent.putExtra("Score", currentScore);

//                    } else { // If there is no player with the next ID in the database, show an error message
//                        Toast.makeText(Game.this, "No more players", Toast.LENGTH_SHORT).show();
////                        currentPlayerId--; // decrement current player ID as there are no more players
//                    }

//
                }


            }
        });
        mReveal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                dice1.setVisibility(View.VISIBLE);
                dice2.setVisibility(View.VISIBLE);


                int[] actual = getDiceScore(previousDice1, previousDice2);

                if(actual[0] > actual[1]){
                    dice1.setImageDrawable(previousDice1);

                    // set the image of dice2 to the previous image
                    dice2.setImageDrawable(previousDice2);
                }
                else{
                    dice1.setImageDrawable(previousDice2);

                    // set the image of dice2 to the previous image
                    dice2.setImageDrawable(previousDice1);
                }

                String p = String.valueOf(actual[0]) + String.valueOf(actual[1]);
                int c = calculateScore(p);
//                Intent intent = getIntent();
//                // receive the value by getStringExtra() method and
//                // key must be same which is send by first activity
//                int str = intent.getIntExtra("Score",currentScore);

                String s = playerInput.getText().toString().trim();
                //int score_previous_player = str;
                int input = Integer.parseInt(s);
                int score = calculateScore(String.valueOf(input));

                if(c <= score){
                    subtractPointFromPlayer(currentPlayerId);

                    System.out.println(" c moi " + currentPlayerId);
                    String toast = player.getName() + " actu lost a point";
                    System.out.println(player.getName());
                    Toast.makeText(Game.this, toast, Toast.LENGTH_SHORT).show();
                }
                else if(c > score){
                    //Player prev_player = myDb.getPreviousPlayerId(currentPlayerId);


                    String toast = player.getName() + " prev lost a point";
                    Toast.makeText(Game.this, toast, Toast.LENGTH_SHORT).show();
                    currentPlayerId = (currentPlayerId - 1);
                    subtractPointFromPlayer(currentPlayerId);


                    playerName.setText(player.getName() + " - " + player.getScore());

                    Intent intent = new Intent(Game.this, Game.class);
                }



                System.out.println("c mon score ancien " + c + " mon nouveau "+ score);

            }
        });
    }
    public void subtractPointFromPlayer(int playerId) {
        // Get the current score of the player
        int currentScore = myDb.getPlayerScore(playerId);


        // Subtract 1 from the score
        int newScore = currentScore - 1;
        // Update the player's score in the database
        myDb.updatePlayerScore(playerId, newScore);
        if(newScore == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
            builder.setTitle("You lost")
                    .setMessage("Would you like a last chance?")
                    .setPositiveButton("Yes, pleas", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Game.this, TruthOrDare.class);
                            startActivityForResult(intent,5);
                        }
                    })
                    .setNegativeButton("No, thank you", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Game.this, Game.class);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }


    }

    private int[] getDiceScore(Drawable dice1, Drawable dice2) {
        Drawable[] diceImages = {null,getResources().getDrawable(R.drawable.d_1), getResources().getDrawable(R.drawable.d_2),
                getResources().getDrawable(R.drawable.d_3), getResources().getDrawable(R.drawable.d_4),
                getResources().getDrawable(R.drawable.d_5), getResources().getDrawable(R.drawable.d_6)};

        int score1 = 0;
        if (dice1 != null) {
            for (int i = 1; i < diceImages.length; i++) {
                if (dice1.getConstantState().equals(diceImages[i].getConstantState())) {
                    score1 = i;
                    break;
                }
            }
        }

        int score2 = 0;
        if (dice2 != null) {
            for (int i = 1; i < diceImages.length; i++) {
                if (dice2.getConstantState().equals(diceImages[i].getConstantState())) {
                    score2 = i;
                    break;
                }
            }
        }

        return new int[] {score1, score2};
    }

    private void rollDice() {
        Random random = new Random();
        int diceValue1 = random.nextInt(6) + 1;
        int diceValue2 = random.nextInt(6) + 1;

        switch (diceValue1) {
            case 1:
                dice1.setImageResource(R.drawable.d_1);
                break;
            case 2:
                dice1.setImageResource(R.drawable.d_2);
                break;
            case 3:
                dice1.setImageResource(R.drawable.d_3);
                break;
            case 4:
                dice1.setImageResource(R.drawable.d_4);
                break;
            case 5:
                dice1.setImageResource(R.drawable.d_5);
                break;
            case 6:
                dice1.setImageResource(R.drawable.d_6);
                break;
        }

        switch (diceValue2) {
            case 1:
                dice2.setImageResource(R.drawable.d_1);
                break;
            case 2:
                dice2.setImageResource(R.drawable.d_2);
                break;
            case 3:
                dice2.setImageResource(R.drawable.d_3);
                break;
            case 4:
                dice2.setImageResource(R.drawable.d_4);
                break;
            case 5:
                dice2.setImageResource(R.drawable.d_5);
                break;
            case 6:
                dice2.setImageResource(R.drawable.d_6);
                break;
        }
    }

    public static int calculateScore(String input) {
        switch (input) {
            case "32":
                return 1;
            case "41":
                return 2;
            case "42":
                return 3;
            case "43":
                return 4;
            case "51":
                return 5;
            case "52":
                return 6;
            case "53":
                return 7;
            case "54":
                return 8;
            case "61":
                return 9;
            case "62":
                return 10;
            case "63":
                return 11;
            case "64":
                return 12;
            case "65":
                return 13;
            case "11":
                return 14;
            case "22":
                return 15;
            case "33":
                return 16;
            case "44":
                return 17;
            case "55":
                return 18;
            case "66":
                return 19;
            case "21":
                return 20;
            case "31":
                return 21;
            default:
                return 1;
        }
    }

    public static int getScore(int input) {
        switch (input) {
            case 1:
                return 32;
            case 2:
                return 41;
            case 3:
                return 42;
            case 4:
                return 43;
            case 5:
                return 51;
            case 6:
                return 52;
            case 7:
                return 53;
            case 8:
                return 54;
            case 9:
                return 61;
            case 10:
                return 62;
            case 11:
                return 63;
            case 12:
                return 64;
            case 13:
                return 65;
            case 14:
                return 11;
            case 15:
                return 22;
            case 16:
                return 33;
            case 17:
                return 44;
            case 18:
                return 55;
            case 19:
                return 66;
            case 20:
                return 21;
            case 21:
                return 31;
            default:
                return 32;
        }
    }




    public void viewAll(){
        btnviewAll.setOnClickListener(
            new View.OnClickListener(){
                public void onClick(View v) {
                    Intent intent = getIntent();
                    int count = intent.getIntExtra("numberPlayers",0);
                    Cursor res = myDb.getAllData(count);
                    if (res.getCount() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
                        builder.setTitle("Error");
                        builder.setMessage("Nothing found");
                        builder.setPositiveButton("OK", null);
                        builder.show();
                        return;
                    }
                    StringBuffer buffer = new StringBuffer();
                    while (res.moveToNext()) {
                        String playerName = res.getString(1);
                        String playerScore = res.getString(2);
                        buffer.append("Name : " + playerName + "\n");
                        buffer.append("Score : " + playerScore  + "\n");
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
                    builder.setTitle("Data");
                    builder.setMessage(buffer.toString());
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            }
        );
    }
    public void nextPlayer(){
        Player player = myDb.getPlayerName(currentPlayerId);
        Player nextPlayer = myDb.getNextPlayerId(currentPlayerId);
        Intent intent = getIntent();
        int count = intent.getIntExtra("numberPlayers",0);

        if (currentPlayerId == count){
            currentPlayerId = 1;
            playerName.setText(player.getName() + " - " + player.getScore());
        }
        else{
            currentPlayerId = (currentPlayerId + 1);
            currentPlayerId = nextPlayer.getId();
            playerName.setText(nextPlayer.getName() + " - " + nextPlayer.getScore());

        }
    }

}