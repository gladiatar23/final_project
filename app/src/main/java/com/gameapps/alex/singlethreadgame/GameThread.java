package com.gameapps.alex.singlethreadgame;


import android.util.Log;

import com.gameapps.alex.singlethreadgame.ready_sprites.SpriteCollisions;
import com.gameapps.alex.singlethreadgame.ready_sprites.WorldManager;

import java.util.Date;

/**
 * Created by Alex on 1/6/2017.
 */
//The Class Runs Threads .Defines sleep time and runtime for them (in order not to overload the CPU).
// At the end of each run finishing them and start all over again
public class GameThread extends Thread {
    public static final long SLEEP_TIME = 20;
    public static final long ITERATION_TIME = 25;

    private boolean isRunning;
    private GameGraphics graphics;
    private GameLogics logics;

    private WorldManager worldManager;
    private SpriteCollisions spriteCollisions;
    private GameSession gameSession;


    private Object mPauseLock;
    private boolean mPaused;

    public GameThread(GameGraphics graphics, GameLogics logics, WorldManager worldManager, SpriteCollisions spriteCollisions , GameSession gameSession) {
        this.graphics = graphics;
        this.logics = logics;
        this.worldManager = worldManager;
        this.spriteCollisions = spriteCollisions;
        this.gameSession = gameSession;

        mPauseLock = new Object();
        mPaused = false;
    }

    public void terminateRunning() {
        Log.i("terminating thread" , "terminated " + this.toString());
        isRunning = false;  //indicating that thread needs to stop running
    }


    /**
     * Call this on pause.
     */
    public void pauseRunning() {
        synchronized (mPauseLock) {
            mPaused = true;
        }
    }

    /**
     * Call this on resume.
     */
    public void resumeRunning() {
        synchronized (mPauseLock) {
            mPaused = false;
            mPauseLock.notifyAll();
        }
    }


    /**
     * do logics
     * then do graphics
     * then wait.
     * repeat.
     */
    @Override
    public void run() {

        doRunning();

    }

    private void doRunning() {
        Date startTime , endTime;

        isRunning = true;
        startTime = new Date();
        while (isRunning) {

            synchronized (mPauseLock) {
                while (mPaused) {
                    try {
                        mPauseLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }

            //special logical frames
            worldManager.change();
            spriteCollisions.change();

            //logical-visual
            logics.recalculateEverything();
            graphics.redrawEverything();

            gameSession.determinePhase();

//Time Iteration, subtraction running start time, end time running subtraction
            endTime = new Date();
            long remainingSleep = ITERATION_TIME - (endTime.getTime() - startTime.getTime());
            //If enough sleep time, sleep time remaining if not print Exception
            if(remainingSleep > 0) {


                try {
                    sleep(remainingSleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            startTime = new Date();
        }

        Log.i("terminating thread" , "fully stopped");
    }
}