package com.gameapps.phillip.singlethreadgame;

import android.graphics.Rect;

/**
 * Created by Phillip on 1/6/2017.
 */

public class MyMath {
//The function generates a random position (height) where the enemy appears
    public final static int getRandomUpTo(int upTo) {return (int)(upTo * Math.random());}
    public final static int getRandomUpToIncludingNegative(int upTo) {
        int retVal = upTo * getRandomUpTo(upTo);
        retVal = Math.random() < 0.5 ? -retVal : retVal;

        return retVal;
    }
//The function generates a random chance of the appearance of a special enemy
    public final static boolean doRandomChance(double oddsOfSuccess) {return Math.random() < oddsOfSuccess;}
//The function checks Did the rectangle (the enemy with a Hero or a a bullet with the enemy) touch
    public final static boolean areRectanglesIntersecting(Rect r1 , Rect r2) {
        if(r1.right < r2.left){return false;}    //r1 is off to the left of r2
        else if(r1.left > r2.right){return false;}    //r1 is off to the right of r2
        else if(r1.top > r2.bottom){return false;}    //r1 is off below r2
        else if(r1.bottom < r2.top){return false;}    //r1 is off above r2

        return true;
    }

}
