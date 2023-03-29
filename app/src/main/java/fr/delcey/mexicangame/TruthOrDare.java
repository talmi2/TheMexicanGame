package com.ilan.truthordare;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class TruthOrDare extends AppCompatActivity {

    private Button truthButton;
    private Button dareButton;
    private TextView promptTextView;
    private Button iDidItButton;
    private Button iDidntDoItButton;

    private final String[] truthPrompts = {
            "What is your biggest fear?",
            "Have you ever cheated on a test or exam?",
            "What is the most embarrassing thing that has ever happened to you?",
            "What is your biggest regret in life so far?",
            "Have you ever lied to your best friend?",
            "What is the most trouble you have ever gotten into with the law?",
            "Have you ever stolen anything before?",
            "What is your most embarrassing childhood memory?",
            "Have you ever been in love with someone who didn't love you back?",
            "What is your most embarrassing habit?",
            "Have you ever been in a physical fight with someone?",
            "What is the most embarrassing thing in your internet search history?",
            "Have you ever been rejected by someone you asked out?",
            "What is the biggest secret you've ever kept from your family?",
            "Have you ever intentionally hurt someone emotionally?",
            "What is the most money you've ever spent on something frivolous?",
            "Have you ever had a crush on someone your friend was dating?",
            "What is the most inappropriate thing you've ever said to someone?",
            "Have you ever cheated on a significant other?",
            "What is the most embarrassing thing you've ever worn in public?"
    };

    private final String[] darePrompts = {
            "Go outside and yell \"I love pizza!\" as loud as you can.",
            "Do a dance in the middle of the room to a song chosen by another player.",
            "Text a random number and say \"I'm a unicorn, do you want to be my friend?\"",
            "Eat a spoonful of hot sauce or mustard.",
            "Put on a blindfold and let another player feed you something from the kitchen.",
            "Do a handstand against a wall for at least 10 seconds.",
            "Give a piggyback ride to another player around the room.",
            "Call a family member and sing \"Happy Birthday\" to them, even if it's not their birthday.",
            "Stand on one foot for 30 seconds while closing your eyes.",
            "Brush your teeth with hot sauce instead of toothpaste.",
            "Speak in a funny accent for one minutes.",
            "Let another player draw a mustache on your face with a marker.",
            "Do five push-ups with someone sitting on your back.",
            "Text your crush and confess your love to them (or say something funny if you don't have a crush).",
            "Use your elbows to type out a message on your phone and send it to a friend.",
            "Do a cartwheel or a somersault in the middle of the room.",
            "Sing a song in a public place (if playing outside) or in front of everyone (if playing inside).",
            "Draw a picture blindfolded and have the other players guess what it is."

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truth_or_dare);

        truthButton = findViewById(R.id.truth_button);
        dareButton = findViewById(R.id.dare_button);
        promptTextView = findViewById(R.id.prompt_textview);
        iDidItButton = findViewById(R.id.did_it_button);
        iDidntDoItButton = findViewById(R.id.didnt_do_it_button);

        iDidItButton.setEnabled(false);
        iDidntDoItButton.setEnabled(false);

        truthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRandomPrompt(truthPrompts);
                iDidItButton.setEnabled(true);
                iDidntDoItButton.setEnabled(true);
                truthButton.setEnabled(false);
                dareButton.setEnabled(false);
            }
        });

        dareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRandomPrompt(darePrompts);
                iDidItButton.setEnabled(true);
                iDidntDoItButton.setEnabled(true);
                truthButton.setEnabled(false);
                dareButton.setEnabled(false);
            }
        });

        iDidItButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast iDidItToast = Toast.makeText(TruthOrDare.this, "Awesome! You can return to the game!", Toast.LENGTH_LONG);
                iDidItToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.START, 500, 500);
                iDidItToast.show();

                iDidntDoItButton.setEnabled(false);
                iDidItButton.setEnabled(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3500);
            }
        });

        iDidntDoItButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast didntDoItToast = Toast.makeText(TruthOrDare.this, "Too bad! You're eliminated!", Toast.LENGTH_LONG);
                didntDoItToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                didntDoItToast.show();

                iDidItButton.setEnabled(false);
                iDidntDoItButton.setEnabled(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3500);
            }
        });
    }

    private void displayRandomPrompt(String[] prompts) {
        Random random = new Random();
        int index = random.nextInt(prompts.length);
        promptTextView.setText(prompts[index]);
    }
}
