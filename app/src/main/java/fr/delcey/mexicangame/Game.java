package fr.delcey.mexicangame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.Random;

import DataBase.DBHandler;

public class Game extends AppCompatActivity {

    private DBHandler dbHandler;
    private ImageView dice1, dice2;
    private Button rollButton, next_player_button, comeback, btnviewAll;
    private EditText playerInput;
    private TextView playerName;
    DBHandler myDb;

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

        myDb = new DBHandler(this);
        viewAll();

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice();
            }
        });

        dbHandler = new DBHandler(this);
        playerName = findViewById(R.id.player_name);

        int playerId = 1;
        String player_Name = dbHandler.getPlayerName(playerId);
        playerName.setText(player_Name);
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
                    Cursor res = myDb.getAllData();
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
                        buffer.append("Name : " + res.getString(1) + "\n");
                        buffer.append("Score : " + "5" + "\n");
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
}