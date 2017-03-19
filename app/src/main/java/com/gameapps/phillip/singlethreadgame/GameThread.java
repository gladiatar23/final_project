package com.gameapps.phillip.singlethreadgame;


import android.util.Log;

import java.util.Date;

/**
 * Created by Phillip on 1/6/2017.
 */
//The Class Runs Threads .Defines sleep time and runtime for them (in order not to overload the CPU).
// At the end of each run finishing them and start all over again
public class GameThread extends Thread {
    public static final long SLEEP_TIME = 20;
    public static final long ITERATION_TIME = 25;

    private boolean isRunning;
    private GameGraphics graphics;
    private GameLogics logics;

    public GameThread(GameGraphics graphics , GameLogics gameLocgics) {
        this.graphics = graphics;
        this.logics = gameLocgics;
    }

    public void terminateRun() {
        isRunning = false;
    }

    /**
     * do logics
     * then do graphics
     * then wait.
     * repeat.
     */
    @Override
    public void run() {
        Date startTime , endTime;

        isRunning = true;
        startTime = new Date();
        while (isRunning) {

            logics.recalculateEverything();
            graphics.redrawEverything();
//Time Iteration, subtraction running start time, end time running subtraction
            endTime = new Date();
            long remainingSleep = ITERATION_TIME - (endTime.getTime() - startTime.getTime());
            //If enough sleep time, sleep time remaining if not print Exception
            if(remainingSleep > 0) {
//                Log.i("time sleeping" , "" + remainingSleep);

                try {
                    sleep(remainingSleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            startTime = new Date();
        }


    }
}