package com.example.mediaplayerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import ViewModels.MainViewModel;
import adapter.MusicAdapter;
import adapter.MusicAdapterImpl;

import factory.SongFactory;
import models.Songs;

public class MainActivity extends AppCompatActivity {

    // Widgets
    Button forward_btn, back_btn, play_btn, stop_button;
    TextView time_txt, title_txt;
    SeekBar seekbar;
    private MusicAdapter musicAdapter;
    private Songs mainSong;

    private SongFactory factory;
    private Boolean isPlaying;

    private Context context;

    MainViewModel mainActivityViewModel;

    public MainActivity(){
        this.factory = new SongFactory();
        //this.isPlaying = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        this.musicAdapter = new MusicAdapterImpl(this);

         mainActivityViewModel = new ViewModelProvider(this).get(MainViewModel.class);
         mainActivityViewModel.setContext(this);

         LiveData<Context> getContext = mainActivityViewModel.getStoreContext();
         getContext.observe(this, new Observer<Context>() {
             @Override
             public void onChanged(Context context) {
                 MainActivity.this.context = context;
             }
         });

        LiveData<Boolean> checkPlaying = mainActivityViewModel.getIsPlaying();
        checkPlaying.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isPlaying = aBoolean;
            }
        });

        play_btn = findViewById(R.id.play_btn);
        stop_button = findViewById(R.id.pause_btn);
        forward_btn = findViewById(R.id.forward_btn);
        back_btn = findViewById(R.id.back_btn);

        title_txt = findViewById(R.id.song_title);
        time_txt = findViewById(R.id.time_left_text);

        seekbar = findViewById(R.id.seekBar);

        mainSong = factory.generateSong(getIntent().getIntExtra("resourceName", 0));

        title_txt.setText(mainSong.getTitle());

        seekbar.setClickable(false);


        // Adding Functionalities for the buttons
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PlayMusic();

                if (isPlaying){
                    Toast.makeText(MainActivity.this, "Already Playing!!", Toast.LENGTH_SHORT).show();
                } else {
                    musicAdapter.playMusic(mainSong, seekbar, time_txt);
                    mainActivityViewModel.changePlaying();
                }
            }
        });

        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying){
                    musicAdapter.pauseMusic(context, mainSong, seekbar, time_txt);
                    mainActivityViewModel.changePlaying();
                } else {
                    Toast.makeText(MainActivity.this, "Already Paused!!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicAdapter.playForward(MainActivity.this);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicAdapter.playBackward(MainActivity.this);
            }
        });
    }
}