package factory;

import models.Songs;

public interface Factory {

    public Songs generateSong(int resourceID);

}
