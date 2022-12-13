package fr.delcey.mexicangame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Go extends AppCompatActivity {
    private static TextView mGoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go);
        mGoTextView = findViewById(R.id.main_textview_go);

    }
}