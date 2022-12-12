package fr.delcey.mexicangame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.*;
import android.view.View;
import android.widget.*;

import fr.delcey.mexicangame.R;

public class MainActivity extends AppCompatActivity {

    TextView mGreetingTextView, mPlayerTextView;
    EditText mNumEditText;
    Button mPlayButton, mAboutUsButton, mRulesButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mGreetingTextView = findViewById(R.id.main_textview_greeting);
        mPlayerTextView = findViewById(R.id.main_textview_players);

        mNumEditText = findViewById(R.id.main_edittext_num);

        mPlayButton = findViewById(R.id.go_button);
        mAboutUsButton = findViewById(R.id.about_us_button);
        mRulesButton = findViewById(R.id.rules_button);

        mPlayButton.setEnabled(false);

        mNumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPlayButton.setEnabled(!s.toString().isEmpty());

            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}