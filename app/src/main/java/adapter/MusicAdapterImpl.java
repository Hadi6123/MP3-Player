package adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediaplayerapp.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import models.Songs;

public class MusicAdapterImpl implements MusicAdapter {

    private ActualMusicPlayer actualMusicPlayer = null;
    private ScheduledExecutorService scheduledExecutorService;
    private Context context;

    public MusicAdapterImpl(Context context){

        this.context = context;
        createThread();

    }

    private void createThread(){
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Play the song!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void playMusic(Songs song, SeekBar seekBar, TextView time) {

        scheduledExecutorService.shutdown();

        if (actualMusicPlayer == null){
            actualMusicPlayer = ActualMusicPlayer.getInstance(context, song, seekBar, time);
            /*actualMusicPlayer.setMediaPlayer(context, song.getResourceID());
            actualMusicPlayer.setContext(context);
            actualMusicPlayer.setSeekbar(seekBar);
            actualMusicPlayer.setTime(time);
            actualMusicPlayer.play(); */
        } else {
            pauseMusic(context, song, seekBar, time);
        }

    }

    @Override
    public void pauseMusic(Context context, Songs song, SeekBar seekBar, TextView time) {
        actualMusicPlayer = ActualMusicPlayer.getInstance(context, song, seekBar, time);
        actualMusicPlayer.pauseSong();

        createThread();
    }

    @Override
    public void playForward(Context context) {
        actualMusicPlayer.setContext(context);
        actualMusicPlayer.forward();

    }

    @Override
    public void playBackward(Context context) {
        actualMusicPlayer.setContext(context);
        actualMusicPlayer.backward();

    }

    @Override
    public void playReset() {
        actualMusicPlayer.resetMusic();
    }
}
