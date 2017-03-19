package com.gameapps.phillip.singlethreadgame;

import com.gameapps.phillip.singlethreadgame.R;

/**
 * Created by USER on 07/03/2017.
 */

public class GameSession {

    public static Level currentLevel;
    int enemiesHit;
    int enemiesToKill;

    public GameSession(){
        enemiesHit=0;
        enemiesToKill = currentLevel.killsToWin;


    }

    public void registerKill(){
        enemiesHit++;
        if (enemiesHit>=enemiesToKill)
        {
            //TODO - win
        }
    }

    public enum Level {
        NEW_YORK (20 , R.drawable.newyork),
        SIBIRIA (30, R.drawable.jerusalem1),
        HELL (40, R.drawable.newyork1),
        NEW_YOR1 (20, R.drawable.newyork),
        SIBIRIA1(30, R.drawable.newyork),
        HELL1 (40, R.drawable.newyork),
        NEW_YORK2 (20, R.drawable.newyork),
        SIBIRIA2 (30, R.drawable.newyork),
        HELL2 (40, R.drawable.newyork)
        ;

        int killsToWin;
        int pathToBG;

        Level (int killsToWin, int pathToBG){
            this.killsToWin=killsToWin;
            this.pathToBG=pathToBG;

        }

    }

}
