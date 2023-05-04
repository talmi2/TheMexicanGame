package fr.delcey.mexicangame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



import android.annotation.SuppressLint;
import android.content.Intent;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.*;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    TextView mGreetingTextView, mPlayerTextView;
    EditText mNumEditText;
    Button mGoButton, mAboutUsButton, mRulesButton ;
    private MediaPlayer mClickSound;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mGreetingTextView = findViewById(R.id.main_textview_greeting);
        mPlayerTextView = findViewById(R.id.main_textview_players);

        mNumEditText = findViewById(R.id.main_edittext_num);

        mGoButton = findViewById(R.id.go_button);
        mAboutUsButton = findViewById(R.id.about_us_button);
        mRulesButton = findViewById(R.id.rules_button);

        mClickSound = MediaPlayer.create(this, R.raw.click);


        mGoButton.setEnabled(false);

        mNumEditText.setText("");

        mNumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().isEmpty()) {
                    mGoButton.setEnabled(false);
                    mNumEditText.setError(null);
                } else {

                        int num = Integer.parseInt(s.toString());
                        if (num >= 2 && num <= 9) {
                            mGoButton.setEnabled(true);
                            mNumEditText.setError(null);
                        } else {
                            mGoButton.setEnabled(false);
                            mNumEditText.setError("You need to enter a number between 2 and 9");
                        }
                    }

                }
        });

        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickSound.start();

                String strNumber = mNumEditText.getText().toString().trim();
//                if(TextUtils.isEmpty(strNumber) || (Integer.parseInt(strNumber) < 2 || Integer.parseInt(strNumber)>9)){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                    builder.setTitle("Error");
//                    builder.setMessage("You need yo enter a number between 2 and 9");
//                    builder.setPositiveButton("OK", null);
//                    builder.show();
//                }
//                else{
                    Intent intent = new Intent(MainActivity.this,Go.class);
                    intent.putExtra("numPlayers", strNumber);
                    startActivityForResult(intent,5);
               // }
            }
        });
        mAboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickSound.start();

                startActivityForResult(new Intent(MainActivity.this, AboutUs.class),5);
            }
        });
        mRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickSound.start();

                startActivityForResult(new Intent(MainActivity.this, Rules.class),5);
            }
        });


    }

}