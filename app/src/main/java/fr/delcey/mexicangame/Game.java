package fr.delcey.mexicangame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.*;
import java.util.List;
import java.util.Random;
import DataBase.DBHandler;
import DataBase.Player;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;


public class Game extends AppCompatActivity {

    private ImageView dice1, dice2;
    private Button rollButton, next_player_button, comeback, btnviewAll, roll_again, mBackButton, mReveal;
    private EditText playerInput;
    private TextView playerName;
    DBHandler myDb;
    private Drawable previousDice1;
    private Drawable previousDice2;
    int currentPlayerId = 1;
    int currentScore = 1;
    private MediaPlayer mDiceSound, mMexicanSong, mLoserSound, mViewDataSound, mClickSound;
    private MessageDialog messageDialog;
    private WinnerDialog winnerDialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dice1 = findViewById(R.id.dice1);
        dice2 = findViewById(R.id.dice2);
        rollButton = findViewById(R.id.roll_button);
        playerName = findViewById(R.id.player_name);
        playerInput = findViewById(R.id.main_edittext_num1);
        next_player_button = findViewById(R.id.next_player);
        comeback = findViewById(R.id.back_button);
        btnviewAll = findViewById(R.id.view_all_button);
        roll_again = findViewById(R.id.roll_again);
        mBackButton = findViewById(R.id.back_button);
        mReveal = findViewById(R.id.print_game);

        mDiceSound = MediaPlayer.create(this, R.raw.dice_sound);
        mMexicanSong = MediaPlayer.create(this, R.raw.mexican_song);
        mLoserSound = MediaPlayer.create(this, R.raw.loser);
        mViewDataSound = MediaPlayer.create(this, R.raw.data3);
        mClickSound = MediaPlayer.create(this, R.raw.click);
        winnerDialog = new WinnerDialog(this);


        messageDialog = new MessageDialog(this);


        next_player_button.setEnabled(false);
        roll_again.setEnabled(false);

        myDb = new DBHandler(this);
        viewAll();

        playerName = findViewById(R.id.player_name);
        List<Player> activePlayers = myDb.getActivePlayers(false);
        if (activePlayers.size() == 1) {
            Player winner = activePlayers.get(0);
            winnerDialog.showMessage("Congratulation " + winner.getName() + " you have won", 3000);
        }
        else {
            for (Player players : activePlayers) {
                playerName.setText(myDb.getPlayerName(currentPlayerId).getName() + " : " + myDb.getPlayerName(currentPlayerId).getScore() + " point(s) left");
            }
        }
//        Player player = myDb.getPlayerName(currentPlayerId);
//        currentPlayerId = player.getId();
//
//
//        playerName = findViewById(R.id.player_name);
//        List<Player> activePlayers = myDb.getActivePlayers(false);
//        for (Player players : activePlayers) {
//                playerName.setText(players.getName() + " : " + players.getScore() + " point(s) left");
//        }
        System.out.println("coucou 1 ");


        String strNumber = playerInput.getText().toString().trim();

        currentScore = calculateScore(strNumber);
        Player player = myDb.getPlayerName(currentPlayerId);
        currentPlayerId = player.getId();


        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickSound.start();

                startActivityForResult(new Intent(Game.this, MainActivity.class), 5);
            }
        });

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateDice();


                roll_again.setEnabled(true);
                rollButton.setEnabled(false);
                mReveal.setEnabled(false);

            }
        });

        roll_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice();
                // save the current image of dice1
                previousDice1 = dice1.getDrawable();

                // save the current image of dice2
                previousDice2 = dice2.getDrawable();
                dice1.setVisibility(View.INVISIBLE);
                dice2.setVisibility(View.INVISIBLE);
                roll_again.setEnabled(false);
                mReveal.setEnabled(false);

            }
        });

        playerInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String inputText = s.toString().trim();
                if (inputText.length() == 2) {
                    char firstDigit = inputText.charAt(0);
                    char secondDigit = inputText.charAt(1);

                    if (firstDigit < secondDigit) {
                        String result = secondDigit + "" + firstDigit;
                        playerInput.setText(result); // set the updated value back to the EditText
                    }
                }
                if (playerInput.getText().toString().trim() == "31" || calculateScore(inputText) == 0) {
                    // Display an error message or take other actions
                    playerInput.setError("invalid score ");
                    next_player_button.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                next_player_button.setEnabled(!s.toString().isEmpty());
                if (!s.toString().isEmpty()) {
                    Intent intent = getIntent();
                    // receive the value by getStringExtra() method and
                    // key must be same which is send by first activity
                    int str = intent.getIntExtra("Score", currentScore);

                    int score_previous_player = str;
                    int input = Integer.parseInt(s.toString());

                    int score = calculateScore(String.valueOf(input));
                    if (score < score_previous_player && score_previous_player != 21) {
                        playerInput.setError("the score must be greater than or equal to that of the previous player " + getScore(score_previous_player));
                        next_player_button.setEnabled(false);
                    }
                }

            }
        });
        next_player_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickSound.start();
                String strNumber = playerInput.getText().toString().trim();

                int[] actual = getDiceScore(previousDice1, previousDice2);

                if (actual[0] > actual[1]) {
                    dice1.setImageDrawable(previousDice1);

                    // set the image of dice2 to the previous image
                    dice2.setImageDrawable(previousDice2);
                } else {
                    dice1.setImageDrawable(previousDice2);

                    // set the image of dice2 to the previous image
                    dice2.setImageDrawable(previousDice1);
                }

                dice1.setVisibility(View.INVISIBLE);
                dice2.setVisibility(View.INVISIBLE);

                rollButton.setEnabled(true);
                roll_again.setEnabled(false);
                mReveal.setEnabled(true);
                next_player_button.setEnabled(false);
//
//                if (activePlayers.size() == 1) {
//                    // Only one player left, do not move to the next player
//                    Player player = activePlayers.get(0);
//                    winnerDialog.showMessage("Congratulation, "+ player.getName() +" turn !", 3000);
//                    playerName.setText(player.getName() + " : " + player.getScore() + " point(s) left");
//                } else {
                // Move to the next player
                nextPlayer();

                Intent intent = new Intent(Game.this, Game.class);
                currentScore = calculateScore(strNumber);
                intent.putExtra("Score", currentScore);
                // }

//                    // Get the list of active players (excluding the eliminated player)
//
//                    nextPlayer();
//
//                    // Add the player ID as an extra to the Intent
//                    Intent intent = new Intent(Game.this, Game.class);
//                    currentScore = calculateScore(strNumber);
//                    intent.putExtra("Score", currentScore);
            }
        });
        mReveal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickSound.start();

                dice1.setVisibility(View.VISIBLE);
                dice2.setVisibility(View.VISIBLE);


                int[] actual = getDiceScore(previousDice1, previousDice2);

                if (actual[0] > actual[1]) {
                    dice1.setImageDrawable(previousDice1);

                    // set the image of dice2 to the previous image
                    dice2.setImageDrawable(previousDice2);
                } else {
                    dice1.setImageDrawable(previousDice2);
                    int temp = actual[0];
                    actual[0] = actual[1];
                    actual[1] = temp;
                    // set the image of dice2 to the previous image
                    dice2.setImageDrawable(previousDice1);
                }

                String p = String.valueOf(actual[0]) + String.valueOf(actual[1]);
                int c = calculateScore(p);

                String s = playerInput.getText().toString().trim();
                int input = Integer.parseInt(s);
                int score = calculateScore(String.valueOf(input));
                if (c >= score && c != 21 && c != 20) {
                    subtractPointFromPlayer(currentPlayerId);
                    String current_player_name = myDb.getCurrentPlayerName(currentPlayerId);
                    String toast = current_player_name + " lost a point";
                    int updatedScore = myDb.getPlayerScore(currentPlayerId);
                    playerName.setText(current_player_name + " : " + updatedScore + " point(s) left");
                    System.out.println("coucou 2 ");


                    Toast.makeText(Game.this, toast, Toast.LENGTH_SHORT).show();
                    currentScore = 1;

                } else if (c < score && c != 21 && c != 20) {
                    List<Player> activePlayers = myDb.getActivePlayers(false);
                    int currentPlayerIndex = 0;
                    for (int i = 0; i < activePlayers.size(); i++) {
                        if (activePlayers.get(i).getId() == currentPlayerId) {
                            currentPlayerIndex = i;
                            break;
                        }
                    }

                    if (currentPlayerIndex == 0) {
                        // Current player is the first player in the list, set current player to be the last player
                        currentPlayerId = activePlayers.get(activePlayers.size() - 1).getId();
                    } else {
                        currentPlayerId = activePlayers.get(currentPlayerIndex - 1).getId();
                    }

                    subtractPointFromPlayer(currentPlayerId);
                    int updatedScore = myDb.getPlayerScore(currentPlayerId);
                    String toast = myDb.getPlayerName(currentPlayerId).getName() + " lost a point";
                    Toast.makeText(Game.this, toast, Toast.LENGTH_SHORT).show();

                    messageDialog.showMessage(myDb.getPlayerName(currentPlayerId).getName() + " try again!", 3000);

                    playerName.setText(myDb.getPlayerName(currentPlayerId).getName() + " : " + updatedScore + " point(s) left");

                    Intent intent = new Intent(Game.this, Game.class);
                    currentScore = 1;
                    intent.putExtra("Score", currentScore);
                } else if (c == 21) {
                    String toast = player.getName() + " you had a joker, replay";
                    Toast.makeText(Game.this, toast, Toast.LENGTH_SHORT).show();
                    currentPlayerId = (currentPlayerId - 1);
                    int updatedScore = myDb.getPlayerScore(currentPlayerId);

                    playerName.setText(player.getName() + " : " + updatedScore + " point(s) left");
                    System.out.println("coucou 4 ");


                    Intent intent = new Intent(Game.this, Game.class);

                } else if (c == 20) {
                    mMexicanSong.start();
                    for (int i = 0; i < 2; i++) {
                        subtractPointFromPlayer(currentPlayerId);
                    }
                    String current_player_name = myDb.getCurrentPlayerName(currentPlayerId);
                    String toast = current_player_name + " lost two point";
                    int updatedScore = myDb.getPlayerScore(currentPlayerId);
                    playerName.setText(current_player_name + " : " + updatedScore + " point(s) left");
                    System.out.println("coucou 5 ");

                    Toast.makeText(Game.this, toast, Toast.LENGTH_SHORT).show();
                    currentScore = 1;

                }
                mReveal.setEnabled(false);
            }
        });


    }

    public void animateDice() {
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                rollDice();

            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation started
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Animation ended, get the score
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int[] actual = getDiceScore(dice1.getDrawable(), dice2.getDrawable());
                        previousDice1 = dice1.getDrawable();
                        // save the current image of dice2
                        previousDice2 = dice2.getDrawable();
                        if (actual[0] > actual[1]) {
                            dice1.setImageDrawable(previousDice1);
                            // set the image of dice2 to the previous image
                            dice2.setImageDrawable(previousDice2);
                        } else {
                            dice1.setImageDrawable(previousDice2);
                            // set the image of dice2 to the previous image
                            dice2.setImageDrawable(previousDice1);
                        }

                        dice1.setVisibility(View.VISIBLE);
                        dice2.setVisibility(View.VISIBLE);

                        // Do something with the score here
                    }
                }, 500); // Delay the retrieval of the score by 500ms to allow for the animation to finish
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeated
            }
        });

        animation.setDuration(500);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        dice1.startAnimation(animation);
        dice2.startAnimation(animation);


    }

    protected void onDestroy() {
        super.onDestroy();
        myDb.resetStatusInDatabase();
    }

    public void subtractPointFromPlayer(int playerId) {
        // Get the current score of the player
        int currentScore = myDb.getPlayerScore(playerId);

        // Subtract 1 from the score
        int newScore = currentScore - 1;
        // Update the player's score in the database
        myDb.updatePlayerScore(playerId, newScore);

        if (newScore == 0) {
            myDb.updatePlayerStatus(currentPlayerId, false);

            boolean hasSecondChance = getIntent().getBooleanExtra("hasSecondChance", false);
            if (hasSecondChance) {
                //     myDb.updatePlayerStatus(currentPlayerId, false);
                String eliminatedPlayer = myDb.getCurrentPlayerName(currentPlayerId);
                mLoserSound.start();
                String toast = eliminatedPlayer + " has been eliminated!";
                Toast.makeText(Game.this, toast, Toast.LENGTH_SHORT).show();
                List<Player> activePlayers = myDb.getActivePlayers(false);

                if (activePlayers.size() == 1) {
                    Player winner = activePlayers.get(0);
                    winnerDialog.showMessage("Congratulation " + winner.getName() + " you have won", 3000);
                }
                else {
                    // Get the list of active players (excluding the eliminated player)
                    for (Player player : activePlayers) {
                        nextPlayer();
                        player = myDb.getPlayerName(currentPlayerId);
                        playerName.setText(player.getName() + " : " + player.getScore() + " point(s) left");
                        System.out.println("coucou 6 ");

                    }
                }

                Intent intent = new Intent(Game.this, Game.class);
                startActivity(intent);
                nextPlayer();


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
                builder.setTitle("You lost")
                        .setMessage("Would you like a last chance?")
                        .setPositiveButton("Yes, pleas", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Game.this, TruthOrDare.class);
                                intent.putExtra("PlayerId", playerId);
                                intent.putExtra("hasSecondChance", true);
                                startActivityForResult(intent, 5);
                            }
                        })
                        .setNegativeButton("No, thank you", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Update the status of the eliminated player to inactive
                                String eliminatedPlayer = myDb.getCurrentPlayerName(currentPlayerId);
                                String toast = eliminatedPlayer + " has been eliminated!";
                                Toast.makeText(Game.this, toast, Toast.LENGTH_SHORT).show();

                                // Get the list of active players (excluding the eliminated player)
                                List<Player> activePlayers = myDb.getActivePlayers(false);
                                System.out.println(activePlayers.size() + "chianttttttttt");
                                if (activePlayers.size() == 1) {
                                    System.out.println("helo o won");
                                    Player winner = activePlayers.get(0);
                                    winnerDialog.showMessage("Congratulation " + winner.getName() + " you have won", 3000);
                                }
                                else {
                                    for (Player player : activePlayers) {
                                        nextPlayer();
                                        player = myDb.getPlayerName(currentPlayerId);

                                        playerName.setText(player.getName() + " : " + player.getScore() + " point(s) left");
                                        System.out.println("coucou 7 ");

                                    }
                                    Intent intent = new Intent(Game.this, Game.class);
                                    startActivity(intent);
                                }



                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false); // Set the dialog to be non-cancelable
                dialog.show();
            }
        }


    }

    private int[] getDiceScore(Drawable dice1, Drawable dice2) {
        Drawable[] diceImages = {null, getResources().getDrawable(R.drawable.d_1), getResources().getDrawable(R.drawable.d_2),
                getResources().getDrawable(R.drawable.d_3), getResources().getDrawable(R.drawable.d_4),
                getResources().getDrawable(R.drawable.d_5), getResources().getDrawable(R.drawable.d_6)};


        int score1 = 0;
        if (dice1 != null) {
            for (int i = 1; i < diceImages.length; i++) {
                if (dice1.getConstantState().equals(diceImages[i].getConstantState())) {
                    score1 = i;
                    break;
                }
            }
        }

        int score2 = 0;
        if (dice2 != null) {
            for (int i = 1; i < diceImages.length; i++) {
                if (dice2.getConstantState().equals(diceImages[i].getConstantState())) {
                    score2 = i;
                    break;
                }
            }
        }

        return new int[]{score1, score2};
    }

    private void rollDice() {
        mDiceSound.start();

        Random random = new Random();
        int diceValue1 = random.nextInt(6) + 1;
        int diceValue2 = random.nextInt(6) + 1;

        switch (diceValue1) {
            case 1:
                dice1.setImageResource(R.drawable.d_1);
                break;
            case 2:
                dice1.setImageResource(R.drawable.d_2);
                break;
            case 3:
                dice1.setImageResource(R.drawable.d_3);
                break;
            case 4:
                dice1.setImageResource(R.drawable.d_4);
                break;
            case 5:
                dice1.setImageResource(R.drawable.d_5);
                break;
            case 6:
                dice1.setImageResource(R.drawable.d_6);
                break;
        }

        switch (diceValue2) {
            case 1:
                dice2.setImageResource(R.drawable.d_1);
                break;
            case 2:
                dice2.setImageResource(R.drawable.d_2);
                break;
            case 3:
                dice2.setImageResource(R.drawable.d_3);
                break;
            case 4:
                dice2.setImageResource(R.drawable.d_4);
                break;
            case 5:
                dice2.setImageResource(R.drawable.d_5);
                break;
            case 6:
                dice2.setImageResource(R.drawable.d_6);
                break;
        }
    }

    public static int calculateScore(String input) {
        switch (input) {
            case "32":
                return 1;
            case "41":
                return 2;
            case "42":
                return 3;
            case "43":
                return 4;
            case "51":
                return 5;
            case "52":
                return 6;
            case "53":
                return 7;
            case "54":
                return 8;
            case "61":
                return 9;
            case "62":
                return 10;
            case "63":
                return 11;
            case "64":
                return 12;
            case "65":
                return 13;
            case "11":
                return 14;
            case "22":
                return 15;
            case "33":
                return 16;
            case "44":
                return 17;
            case "55":
                return 18;
            case "66":
                return 19;
            case "21":
                return 20;
            case "31":
                return 21;

        }
        return 0;
    }

    public static int getScore(int input) {
        switch (input) {
            case 1:
                return 32;
            case 2:
                return 41;
            case 3:
                return 42;
            case 4:
                return 43;
            case 5:
                return 51;
            case 6:
                return 52;
            case 7:
                return 53;
            case 8:
                return 54;
            case 9:
                return 61;
            case 10:
                return 62;
            case 11:
                return 63;
            case 12:
                return 64;
            case 13:
                return 65;
            case 14:
                return 11;
            case 15:
                return 22;
            case 16:
                return 33;
            case 17:
                return 44;
            case 18:
                return 55;
            case 19:
                return 66;
            case 20:
                return 21;
            case 21:
                return 31;

        }
        return input;
    }

    public void viewAll() {
        btnviewAll.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        mViewDataSound.start();
                        int count = 0;
                        List<Player> activePlayers = myDb.getActivePlayers(false);
                        for (Player player : activePlayers) {
                            count++;
                        }
                        Cursor res = myDb.getAllData(count);
                        if (res.getCount() == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
                            builder.setTitle("Error");
                            builder.setMessage("Nothing found");
                            builder.setPositiveButton("OK", null);
                            builder.show();
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            String playerName = res.getString(1);
                            String playerScore = res.getString(2);
                            buffer.append("Name : " + playerName + "\n");
                            buffer.append("Score : " + playerScore + "\n\n");
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
                        builder.setTitle("Players data");
                        builder.setMessage(buffer.toString());
                        builder.setPositiveButton("OK", null);
                        builder.show();
                    }
                }
        );
    }

    public void nextPlayer() {
        Player player = myDb.getPlayerName(currentPlayerId);
        if (player == null) {
            // The current player has already been eliminated, skip over them
            currentPlayerId = myDb.getNextActivePlayer(currentPlayerId).getId();
//            player = myDb.getPlayerName(currentPlayerId);
        }
        Player nextPlayer = myDb.getNextActivePlayer(currentPlayerId);
        Intent intent = getIntent();
        // int count = intent.getIntExtra("numberPlayers",0);
        int count = 0;
        List<Player> activePlayers = myDb.getActivePlayers(false);
        for (Player player1 : activePlayers) {
            count++;
        }
        if (currentPlayerId == count) {
            currentPlayerId = 1;
            int updatedScore = myDb.getPlayerScore(currentPlayerId);
            messageDialog.showMessage("It's " + myDb.getPlayerName(currentPlayerId).getName() + " turn !", 3000);

            playerName.setText(myDb.getPlayerName(currentPlayerId).getName() + " : " + updatedScore + " point(s) left");
            System.out.println("coucou 8 ");

        } else if (nextPlayer != null) {

            currentPlayerId = nextPlayer.getId();
            messageDialog.showMessage("It's " + nextPlayer.getName() + " turn !", 3000);

            playerName.setText(nextPlayer.getName() + " : " + nextPlayer.getScore() + " point(s) left");
            System.out.println("coucou 9 ");


        }

    }

    class MessageDialog {
        private Dialog dialog;
        private Context context;

        public MessageDialog(Context context) {
            this.context = context;
        }

        public void showMessage(String message, int duration) {
            // Créer le dialog personnalisé
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_msg);

            TextView messageTextView = dialog.findViewById(R.id.message_text_view);

            messageTextView.setText(message);

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.gravity = android.view.Gravity.CENTER;
            dialog.getWindow().setAttributes(layoutParams);

            dialog.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, 2000);
        }
    }


    class WinnerDialog {
        private Dialog dialog;
        private Context context;
        private Game game;

        public WinnerDialog(Context context) {
            this.context = context;
        }

        public void showMessage(String message, int duration) {
            // Créer le dialog personnalisé
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_message);


            // Rendre le dialog transparent
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);

                // Rendre le dialog centré à l'écran
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.gravity = Gravity.CENTER;
                window.setAttributes(layoutParams);
            }
            LottieAnimationView confettiAnimationView = dialog.findViewById(R.id.lottie_animation_view);
            confettiAnimationView.setAnimation("confetti.json");
            confettiAnimationView.setBackgroundColor(Color.TRANSPARENT);
            confettiAnimationView.playAnimation();

            FrameLayout animationContainer = dialog.findViewById(R.id.animation_container);
            // Obtenir la référence de la TextView dans le layout du dialog
            TextView messageTextView = dialog.findViewById(R.id.message_text_view);

            // Afficher le message
            messageTextView.setText(message);
            // Remove confettiAnimationView from its previous parent before adding it to animationContainer
            ViewGroup parent = (ViewGroup) confettiAnimationView.getParent();
            if (parent != null) {
                parent.removeView(confettiAnimationView);
            }

            animationContainer.addView(confettiAnimationView);

            // Afficher le dialog
            dialog.show();

            // Fermer le dialog après la durée spécifiée
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    game.finish();
                }
            }, 30000);
        }
    }
}