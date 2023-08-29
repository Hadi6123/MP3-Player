package factory;

import com.example.mediaplayerapp.R;

import models.Confident;
import models.Moana;
import models.PanthersGame6;
import models.Songs;

public class SongFactory implements Factory{
    @Override
    public Songs generateSong(int resourceID) {
        if (resourceID == R.id.panthersBgame6 || resourceID == R.raw.panthersgame6){
            return new PanthersGame6();
        } else if (resourceID == R.id.confidentSong || resourceID == R.raw.confident) {
            return new Confident();
        } else if (resourceID == R.id.moanaSong || resourceID == R.raw.moana){
            return new Moana();
        }

        return new Moana();
    }
}
