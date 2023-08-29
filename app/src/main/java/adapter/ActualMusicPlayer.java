package adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediaplayerapp.MainActivity;

import java.util.concurrent.TimeUnit;

import models.Songs;

public class ActualMusicPlayer {

    private static ActualMusicPlayer instance;  /// line 1
    private MediaPlayer mediaPlayer;

    private double startTime;
    private double finalTime;
    private int forwardTime;
    private int backwardTime ;
    private static int oneTimeOnly;

    private int pausedTime;
    private Boolean isPaused;

    private TextView time;

    /*public TextView getTime() {
        return time;
    } */

    public void setTime(TextView time) {
        this.time = time;
    }

    private Context context;

    private SeekBar seekbar;

    /*public SeekBar getSeekbar() {
        return seekbar;
    } */

    public void setSeekbar(SeekBar seekbar) {
        this.seekbar = seekbar;
    }

    // Handlers
    private Handler handler;
    private Object lock;
    private Boolean isBegan;

    //private Thread running;
    //private Thread stop;

    private ActualMusicPlayer(Context context, Songs song, SeekBar seekBar, TextView time){
        startTime = 0;
        finalTime = 0;
        forwardTime = 10000;
        backwardTime = 10000;
        oneTimeOnly = 0;

        this.context = context;
        setMediaPlayer(this.context, song.getResourceID());
        this.seekbar = seekBar;
        this.time = time;

        handler = new Handler();
        isBegan = false;
        lock = new Object();
        pausedTime = 0;
        isPaused = false;

        startTime = 0;
        finalTime = 60*1000;

        play();
    }

    public static ActualMusicPlayer getInstance(Context context, Songs song, SeekBar seekBar, TextView time){
        synchronized (ActualMusicPlayer.class){
            if (instance == null){
                instance = new ActualMusicPlayer(context, song, seekBar, time);
            }

            return instance;
        }
    }

    public void setMediaPlayer(Context context, int resource){
        mediaPlayer = MediaPlayer.create(context, resource);
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void forward(){
        int temp = (int) startTime;
        if ((temp + forwardTime) <= finalTime){
            startTime = startTime + forwardTime;
            mediaPlayer.seekTo((int) startTime);

            if (isPaused) pausedTime = (int) startTime;

        }else{
            Toast.makeText(context,
                    "Can't Jump Forward!", Toast.LENGTH_SHORT).show();
        }
    }

    public void backward(){
        int temp = (int) startTime;

        if ((temp - backwardTime) > 0){
            startTime = startTime - backwardTime;
            mediaPlayer.seekTo((int) startTime);

            if (isPaused) pausedTime = (int) startTime;
        }else{
            Toast.makeText(context,
                    "Can't Go Back!", Toast.LENGTH_SHORT).show();
        }
    }


    public void play() {

        mediaPlayer.start();

        if (isPaused){
            mediaPlayer.seekTo(pausedTime);
            isPaused = false;
        }

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0){
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        time.setText(String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes
                                ((long) finalTime))
        ));

        seekbar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime, 100);

        }


    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            time.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
            ));


            seekbar.setProgress((int)startTime);
            handler.postDelayed(this, 100);
        }
    };

    public void pauseSong(){
        isPaused = !isPaused;

        if (isPaused){
            pausedTime = (int)startTime * 1;
            Toast.makeText(context, String.format("%d", pausedTime), Toast.LENGTH_SHORT).show();
            mediaPlayer.pause();
        } else{
            play();
        }

    }

    public void resetMusic(){
        mediaPlayer.pause();
        time.setText("00:00");
        startTime = 0;
        seekbar.setProgress((int) startTime);
        mediaPlayer.seekTo((int) startTime);
    }


}
