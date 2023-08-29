package ViewModels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private Boolean isPlaying = false;
    private Context context;

    private MutableLiveData<Boolean> playing = new MutableLiveData<>();
    private MutableLiveData<Context> storeContext = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsPlaying() {
        playing.postValue(isPlaying);
        return playing;
    }

    public void changePlaying(){
        isPlaying = !isPlaying;
        playing.postValue(isPlaying);
    }

    public void setContext(Context context){
        if (this.context == null) {
            this.context = context;
        }
    }

    public MutableLiveData<Context> getStoreContext(){
        storeContext.postValue(context);
        return storeContext;
    }

    public void updateContext(){
        storeContext.postValue(context);
    }


}
