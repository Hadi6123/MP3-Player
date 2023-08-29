package exitTimer;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class Timer {

    private int trackerTime;
    private int reduceTime;
    private TextView textView;

    private Boolean isStopped;
    private Context context;

    private final Object lock = new Object();

    private Thread clock;
    private Thread pausedState;

    private Boolean quitTimer;
    private int immediateQuit;

    public Timer(TextView textView, Context context){
        //lock = new Object();
        trackerTime = 20 * 1000;
        reduceTime = 5;
        isStopped = false;
        quitTimer = false;

        this.textView = textView;
        this.context = context;

        clock = new Thread() {
            @Override
            public void run() {
                try {
                    countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        //object not locked by thread before notify()

        pausedState = new Thread() {
            @Override
            public void run(){
                try {
                    resumeTimer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        //handler.postDelayed(clock, 1000);

    }

    private void countDown() throws InterruptedException{
        // java.lang.IllegalMonitorStateException: object not locked by thread before wait()

        synchronized (this.lock){
            while (true){
                this.lock.notify();
                if (isStopped){
                    this.lock.wait();
                    continue;
                } else{
                    trackerTime -= reduceTime;
                    Thread.sleep(3);

                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(String.format("%d sec",
                                    TimeUnit.MILLISECONDS.toSeconds((long) trackerTime)
                                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) trackerTime))
                            ));
                        }
                    });
                }

                if (trackerTime == 0 || quitTimer){
                    //textView.setText("Time is Up!!!");
                    this.lock.notify();
                    this.lock.wait();
                    break;
                }

            }

            if (!quitTimer){
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("");
                    }
                });
            }

            if (trackerTime == 0){
                ((Activity)context).finish();
            }
        }


        //con;
    }

    public void pauseTimer(){
        isStopped = !isStopped;
    }

    public void startClock(){
        clock.start();
        pausedState.start();
    }

    public void resumeTimer() throws InterruptedException{
        synchronized (this.lock){

            while (true){

                if (!isStopped){
                    //isStopped = false;
                    //new Toast(context, "Hello", Toast.LENGTH_SHORT).show*();
                    this.lock.notify();
                    this.lock.wait();
                }

                if (quitTimer && trackerTime > 0){
                    if (immediateQuit == 1) {
                        this.lock.notify();
                        this.lock.wait();
                    }
                    this.lock.notify();
                    break;
                }

                if (trackerTime == 0){
                    this.lock.notify();
                    break;
                }
            }

        }
    }

    public void quitClock(int immediateQuit){
        quitTimer = !quitTimer;
        this.immediateQuit = immediateQuit;
    }


}
