package com.example.mediaplayerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import exitTimer.Timer;
import factory.SongFactory;
import models.Songs;

public class StartActivity extends AppCompatActivity {

    private SongFactory songFactory;
    private Songs mainSong;

    private Button startButton;
    private Button floridaOption;
    private Button confidentOption;
    private Button moanaOption;
    private Button exitButton;

    private TextView songTitleSelected;
    private TextView exitClock;

    private Boolean readyPlay;

    private Timer timer;

    private Boolean intentionToQuit;


    public StartActivity(){
        this.songFactory = new SongFactory();
        this.mainSong = this.songFactory.generateSong(0);
        this.readyPlay = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        startButton = findViewById(R.id.button);
        floridaOption = findViewById(R.id.button2);
        confidentOption = findViewById(R.id.button3);
        moanaOption = findViewById(R.id.button4);

        intentionToQuit = false;

        songTitleSelected = findViewById(R.id.songTitleSelected);

        exitClock = findViewById(R.id.exitTime);

        exitButton = findViewById(R.id.exit);

        if (exitButton.getText().toString().equals("Exit"))
        {
            Toast.makeText(this, exitButton.getText().toString(), Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "hhh", Toast.LENGTH_SHORT).show();

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (exitButton.getText().toString().equals("Exit")){
                    timer = new Timer(exitClock, StartActivity.this);
                    timer.startClock();

                    intentionToQuit = true;

                    exitButton.setText("Cancel Quitting");

                    AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);

                    // Set the message show for the Alert time
                    builder.setMessage("Do you want to exit ?");

                    // Set Alert Title
                    builder.setTitle("Alert !");

                    // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                    builder.setCancelable(false);

                    // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
                    builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                        // When the user click yes button then app will close
                        timer.quitClock(0);
                        finish();
                    });

                    // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
                    builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                        // If user click no then dialog box is canceled.
                        dialog.cancel();
                    });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();
                    // Show the Alert Dialog box
                    alertDialog.show();
                } else{
                    timer.pauseTimer();


                    AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);

                    // Set the message show for the Alert time
                    builder.setMessage("Do you want to stay ?");

                    // Set Alert Title
                    builder.setTitle("Alert !");

                    // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                    builder.setCancelable(false);

                    // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
                    builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                        // When the user click yes button then app will close
                        timer.quitClock(1);
                        exitButton.setText("Exit");
                        intentionToQuit = false;
                        exitClock.setText(" ");

                    });

                    // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
                    builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                        // If user click no then dialog box is canceled.
                        dialog.cancel();
                        timer.pauseTimer();
                    });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();
                    // Show the Alert Dialog box
                    alertDialog.show();
                }

                //timer = Timer.getInstance(exitClock, this);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readyPlay && !intentionToQuit){
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    intent.putExtra("resourceName", mainSong.getResourceID());
                    startActivity(intent);
                    finish();
                } else{
                    Toast.makeText(StartActivity.this, "Cannot be done!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /*
        floridaOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainSong = songFactory.generateSong(R.id.panthersBgame6);
                songTitleSelected.setText(mainSong.getTitle());

                readyPlay = true;
            }
        });

        confidentOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainSong = songFactory.generateSong(R.id.confidentSong);
                songTitleSelected.setText(mainSong.getTitle());

                readyPlay = true;
            }
        });

        moanaOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainSong = songFactory.generateSong(R.id.moanaSong);
                songTitleSelected.setText(mainSong.getTitle());

                readyPlay = true;
            }
        }); */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();

        mainSong = songFactory.generateSong(itemID);
        songTitleSelected.setText(mainSong.getTitle());

        readyPlay = true;

        Toast.makeText(this, "hahahha", Toast.LENGTH_SHORT).show();
        //musicAdapter.playReset();

        return super.onContextItemSelected(item);
    }
}