package de.ur.mi.reactiontest.Helper;

public class Timer {
    private long startTime = -1;
    public void startTimer(){
        startTime = System.currentTimeMillis();
    }
    public long getTime(){
        return System.currentTimeMillis() - startTime;
    }
    public void stopTimer(){
        startTime = -1;
    }

    public boolean isRunning(){
        return startTime >= 0;
    }
}
