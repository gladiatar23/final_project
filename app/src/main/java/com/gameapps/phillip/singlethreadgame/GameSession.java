package com.gameapps.phillip.singlethreadgame;

import android.os.AsyncTask;
import android.util.Log;

import com.gameapps.phillip.singlethreadgame.data_handle.DBLevelHandler;
import com.gameapps.phillip.singlethreadgame.data_handle.LevelForTable;
import com.gameapps.phillip.singlethreadgame.ready_sprites.Enemy;
import com.gameapps.phillip.singlethreadgame.sprite_definition.Sprite;

import java.util.List;
import java.util.Set;

/**
 * Created by USER on 07/03/2017.
 */

public class GameSession {

    public static Level currentLevel;
    public static Human currentHero;
    public static Set<Human> availableHeroes;
    int enemiesHit;
    int enemiesToKill;

    GameActivity gameActivity;

    public GameSession(GameActivity gameActivity){
        this.gameActivity = gameActivity;


        enemiesHit=0;
        enemiesToKill = currentLevel.killsToWin;


    }

    public void handleSpriteRemoval(Sprite s) {

        if(s instanceof Enemy) {
            enemiesHit++;

            PointsUpdateThread pointsUpdateThread = new PointsUpdateThread();
            pointsUpdateThread.execute(gameActivity);

            if (enemiesHit>=enemiesToKill)
            {
                doWin();
            }
        }
    }

    public void doWin() {
        gameActivity.killThread();

        Log.i("game over" , "WIN!!");

        DBLevelHandler db = DBLevelHandler.getInstance(gameActivity);
        List<LevelForTable> levels = db.getAllLevels();

        boolean isExisting = false;
        LevelForTable currentForTable = null;
        for(LevelForTable l : levels){
            if(l.getId() == currentLevel.id) {
                isExisting = true;
                currentForTable = l;
                break;
            }
        }

        if(!isExisting) {
            currentForTable = new LevelForTable(currentLevel.id, currentLevel.name() , 1 , enemiesHit);
        }
        else {
            currentForTable.setWon(1);
            if(currentForTable.getBestScore() < enemiesHit) {
                currentForTable.setBestScore(enemiesHit);
            }
        }

        db.addLevel(currentForTable);

        gameActivity.goBackToLevelSelect();


    }
    public void doLose() {

    }

    public enum Level {
        //write these down in the order of ID!!!
        MOSCOW (6 , 6, R.drawable.moscow , Enemy.EnemyType.JIHADIST,Human.BEAR),
        HONGKONG (2 , 6, R.drawable.hongkong , Enemy.EnemyType.TURTLE,Human.MARIO),
        NEW_YORK (0 , 6 , R.drawable.newyork , Enemy.EnemyType.SHVARCNEGER, Human.TERMINATOR),
        JERUSALEM(1 , 6, R.drawable.jerusalem , Enemy.EnemyType.WAZE,Human.ROBORABI),
        PARIS (3 , 30, R.drawable.paris , Enemy.EnemyType.VAMINYON,Human.MINYON),
        LONDON(4 , 6, R.drawable.london , Enemy.EnemyType.MOTARO,Human.BENDEL),
        TOKYO (5 , 6, R.drawable.tokyo , Enemy.EnemyType.JIHADIST,Human.BEAR),
        ROME (7 , 6, R.drawable.rome , Enemy.EnemyType.JIHADIST,Human.BEAR),

        ;


        int id;
        int killsToWin;
        int pathToBG;
        public Enemy.EnemyType enemyType;
        public Human unlocledPlayable;

        Level (int id , int killsToWin, int pathToBG , Enemy.EnemyType enemyType,Human unlocledPlayable){
            this.id = id;
            this.killsToWin=killsToWin;
            this.pathToBG=pathToBG;
            this.enemyType = enemyType;
            this.unlocledPlayable=unlocledPlayable;
        }

    }
    public enum Human
    {
        DEFAULT(R.drawable.stickman,R.drawable.shuriken,3),
        BEAR(R.drawable.russianbear_l,R.drawable.sickle_l,8),
        MARIO(R.drawable.mario,R.drawable.special_star,8),
        TERMINATOR(R.drawable.terminatorr_l,R.drawable.grenade1,5),
        ROBORABI(R.drawable.robo_rabi_l,R.drawable.sevivon,6),
        SOLDIER(R.drawable.soldier,R.drawable.missile_l,7),
        MINYON(R.drawable.wolverine_l,R.drawable.banan,8),
        BENDEL(R.drawable.bendel_l,R.drawable.gayka,8),
        ;

        public int pathToPicHero;
        public int bullet;
        public int fireRate;

        Human(int pathToPic, int bullet, int fireRate)
        {
            this.pathToPicHero = pathToPic;
            this.bullet=bullet;
            this.fireRate=fireRate;

        }



    }

    public class PointsUpdateThread extends AsyncTask<GameActivity , GameActivity, String> {

        public boolean isRunning;

        @Override
        protected String doInBackground(GameActivity... params) {
            publishProgress(params);
            return null;
        }

        protected void onProgressUpdate(GameActivity... progress) {
            if(progress[0] == null) return;

            GameActivity activityCalled = progress[0];
            activityCalled.setScore(enemiesHit);
        }

        protected void onPostExecute(String... strs) {

        }
    }

}
