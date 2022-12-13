package fr.delcey.mexicangame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class Rules extends AppCompatActivity {
    private static TextView mTextView;

    public void onCreate(Bundle savedInstanceState) {

        mTextView = findViewById(R.id.main_textview_rules);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
    }
}