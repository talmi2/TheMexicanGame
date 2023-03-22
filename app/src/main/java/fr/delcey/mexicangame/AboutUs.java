package fr.delcey.mexicangame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {
    private static TextView mAboutUsTextView;
    Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        mAboutUsTextView = findViewById(R.id.main_textview_about_us);
        mBackButton = findViewById(R.id.back);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(AboutUs.this, MainActivity.class),5);
            }
        });
    }
}