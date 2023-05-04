package fr.delcey.mexicangame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {
    private static TextView mAboutUsTextView;
    private MediaPlayer mClickSound;

    Button mBackButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        mAboutUsTextView = findViewById(R.id.main_textview_about_us);
        mBackButton = findViewById(R.id.back);
        mClickSound = MediaPlayer.create(this, R.raw.click);


        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickSound.start();
                startActivityForResult(new Intent(AboutUs.this, MainActivity.class),5);
            }
        });
    }
}