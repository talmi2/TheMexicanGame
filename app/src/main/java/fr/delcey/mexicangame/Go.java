package fr.delcey.mexicangame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import DataBase.DBHandler;

public class Go extends AppCompatActivity {
    private LinearLayout lnrDynamicEditTextHolder;
    DBHandler myDb;
    Button mStartButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go);

        mStartButton = findViewById(R.id.play_button);

        myDb = new DBHandler(this);

        Intent intent = getIntent();
        // receive the value by getStringExtra() method and
        // key must be same which is send by first activity
        String str = intent.getStringExtra("numPlayers");

        int number_of_editText = Integer.parseInt(str);

        lnrDynamicEditTextHolder = (LinearLayout) findViewById(R.id.lnrDynamicEditTextHolder);

        for (int i = 0; i < number_of_editText; i++) {
            EditText mPlayerName = new EditText(Go.this);
            mPlayerName.setId(1 + i);
            mPlayerName.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mPlayerName.setHint("numPlayers " + (i + 1));
            lnrDynamicEditTextHolder.addView(mPlayerName);

        }

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int childCount = lnrDynamicEditTextHolder.getChildCount();

                for (int i = 0; i < childCount; i++) {
                    View v = lnrDynamicEditTextHolder.getChildAt(i);

                    if (v instanceof EditText) {
                        EditText editText = (EditText) v;
                        String strName=editText.getText().toString().trim();
                        if(TextUtils.isEmpty(strName)){
                            myDb.insertData("Player ", i);
                        }
                        else{
                            myDb.insertData(editText.getText().toString(), i);
                        }
                    }
                }
                startActivityForResult(new Intent(Go.this, Game.class), 5);

            }
        });
    }

}



