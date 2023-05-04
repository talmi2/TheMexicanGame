package fr.delcey.mexicangame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import DataBase.DBHandler;
import DataBase.Player;

public class Go extends AppCompatActivity {
    private LinearLayout lnrDynamicEditTextHolder;
    DBHandler myDb;
    Button mStartButton, mBackButton;
    private MediaPlayer mClickSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go);

        mStartButton = findViewById(R.id.play_button);
        mBackButton = findViewById(R.id.back_button);
        mClickSound = MediaPlayer.create(this, R.raw.click);


        myDb = new DBHandler(this);

        Intent intent = getIntent();
        // receive the value by getStringExtra() method and
        // key must be same which is send by first activity
        String str = intent.getStringExtra("numPlayers");

        int number_of_editText = Integer.parseInt(str);

        lnrDynamicEditTextHolder = (LinearLayout) findViewById(R.id.lnrDynamicEditTextHolder);


        for (int i = 0; i < number_of_editText; i++) {
            LinearLayout linearLayout = new LinearLayout(Go.this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            EditText mPlayerName = new EditText(Go.this);
            mPlayerName.setId(1 + i);
            LinearLayout.LayoutParams playerNameParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f); // Set the width to 0, weight to 1
            mPlayerName.setLayoutParams(playerNameParams);
            mPlayerName.setHint("Players " + (i + 1));
            linearLayout.addView(mPlayerName);

            ImageView imageView = new ImageView(Go.this);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(100, 100, 0f); // Set the width and height to 100 pixels, weight to 0
            imageView.setLayoutParams(imageParams);
            imageView.setImageResource(R.drawable.gamer);
            linearLayout.addView(imageView);

            lnrDynamicEditTextHolder.addView(linearLayout);
        }

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickSound.start();


                startActivityForResult(new Intent(Go.this, MainActivity.class),5);
            }
        });
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int childCount = lnrDynamicEditTextHolder.getChildCount();

                for (int i = 0; i < childCount; i++) {
                    View v = lnrDynamicEditTextHolder.getChildAt(i);

                    if (v instanceof LinearLayout) {
                        EditText editText = (EditText) ((LinearLayout) v).getChildAt(0);
                        String strName = editText.getText().toString().trim();

                        if(TextUtils.isEmpty(strName)){
                            Player newPlayer = new Player(i,"Player ", 5, true);
                            myDb.insertData(newPlayer, i+1);
                        }
                        else{
                            Player newPlayer = new Player(i,strName, 5, true);
                            myDb.insertData(newPlayer, i+1);

                        }
                    }
                }
                Intent intent = new Intent(Go.this, Game.class);
                intent.putExtra("numberPlayers", childCount);

                startActivityForResult(intent, 5);

            }
        });

    }

}



