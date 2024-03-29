package fr.delcey.mexicangame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.viewpager.widget.ViewPager;



public class Rules extends AppCompatActivity {
    private static TextView mTextView;
    private MediaPlayer mClickSound;

    Button mBackButton;
    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        mBackButton = findViewById(R.id.back_button);
        mClickSound = MediaPlayer.create(this, R.raw.click);


        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new CustomPagerAdapter(this));

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickSound.start();
                startActivityForResult(new Intent(Rules.this, MainActivity.class),5);
            }
        });

    }
}