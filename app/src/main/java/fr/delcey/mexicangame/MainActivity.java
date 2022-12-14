package fr.delcey.mexicangame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.*;
import android.view.View;
import android.widget.*;

import fr.delcey.mexicangame.R;

public class MainActivity extends AppCompatActivity {

    TextView mGreetingTextView, mPlayerTextView;
    EditText mNumEditText;
    Button mGoButton, mAboutUsButton, mRulesButton ;





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


        mGoButton.setEnabled(false);

        String playerNum = mNumEditText.getText().toString();


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
                mGoButton.setEnabled(!s.toString().isEmpty());

            }
        });

        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strNumber=mNumEditText.getText().toString().trim();
                if(TextUtils.isEmpty(strNumber) ||(Integer.parseInt(strNumber) < 1 && Integer.parseInt(strNumber)>9)){

                }
                else{
                    Intent intent = new Intent(MainActivity.this,Go.class);
                    intent.putExtra("numPlayers", strNumber);
                    startActivityForResult(intent,5);
                }
//                    startActivityForResult(new Intent(MainActivity.this,Go.class),5);
            }
        });
        mAboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AboutUs.class),5);
            }
        });
        mRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, Rules.class),5);
            }
        });


    }
}