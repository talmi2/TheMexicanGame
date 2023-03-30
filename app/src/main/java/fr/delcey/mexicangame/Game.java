package fr.delcey.mexicangame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;

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

        final int[] scoring = {32, 41, 42 ,43, 51, 52, 53, 54, 61, 62, 63, 64, 65, 11, 22, 33, 44, 55, 66, 21};

        next_player_button.setEnabled(false);
        roll_again.setEnabled(false);

        myDb = new DBHandler(this);
        viewAll();

        Player player = myDb.getPlayerName(currentPlayerId);
        currentPlayerId = player.getId();


        playerName = findViewById(R.id.player_name);
        playerName.setText(player.getName() + " - " + player.getScore());




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


            }
        });

        playerInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                next_player_button.setEnabled(!s.toString().isEmpty());
                if (!s.toString().isEmpty()) {
                    int input = Integer.parseInt(s.toString());
                    int lastOrder = scoring[0];
                    if (input < lastOrder) {
                        playerInput.setError("the score must be greater than or equal to that of the previous player  " + lastOrder);
                    }
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

                    dice1.setImageDrawable(previousDice1);

                    // set the image of dice2 to the previous image
                    dice2.setImageDrawable(previousDice2);;

                    dice1.setVisibility(View.INVISIBLE);
                    dice2.setVisibility(View.INVISIBLE);

                        rollButton.setEnabled(true);
                        roll_again.setEnabled(false);

                        nextPlayer();
                        // Add the player ID as an extra to the Intent
                        Intent intent = new Intent(Game.this, Game.class);

//                    } else { // If there is no player with the next ID in the database, show an error message
//                        Toast.makeText(Game.this, "No more players", Toast.LENGTH_SHORT).show();
////                        currentPlayerId--; // decrement current player ID as there are no more players
//                    }

//                    intent.putExtra("numPlayers", strNumber);
//                    startActivityForResult(intent,5);
                }

//                Intent intent = new Intent(Game.this,Game.class);

            }
        });
        mReveal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                dice1.setVisibility(View.VISIBLE);
                dice2.setVisibility(View.VISIBLE);

                dice1.setImageDrawable(previousDice1);

                // set the image of dice2 to the previous image
                dice2.setImageDrawable(previousDice2);

            }
        });
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