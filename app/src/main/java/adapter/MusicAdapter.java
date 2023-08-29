package adapter;

import android.content.Context;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import models.Songs;

public interface MusicAdapter {
    public void playMusic(Songs song, SeekBar seekBar, TextView time);
    public void pauseMusic(Context context, Songs song, SeekBar seekBar, TextView time);
    public void playForward(Context context);
    public void playBackward(Context context);
    public void playReset();
}
