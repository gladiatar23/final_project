package com.gameapps.phillip.singlethreadgame;

import android.os.AsyncTask;
import android.util.Log;

import com.gameapps.phillip.singlethreadgame.data_handle.DBLevelHandler;
import com.gameapps.phillip.singlethreadgame.data_handle.LevelForTable;
import com.gameapps.phillip.singlethreadgame.ready_sprites.Enemy;
import com.gameapps.phillip.singlethreadgame.ready_sprites.Player;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by USER on 07/03/2017.
 */

public class GameSession {

    public static final long GAME_OVER_TIME_TILL_QUIT = 3000;
    public static Level currentLevel;
    public static Human currentHero;
    public static Set<Human> availableHeroes;
    public StagePhase stagePhase;
    public boolean isDoneWithPhase;
    public long timeStartedPhase;

    int enemiesHit;
    int enemiesToKill;

    GameActivity gameActivity;
    GameActivity.SpriteEssentialData spriteEssentialData;

    public GameSession(GameActivity gameActivity , GameActivity.SpriteEssentialData spriteEssentialData){
        this.gameActivity = gameActivity;
        this.spriteEssentialData = spriteEssentialData;
        this.isDoneWithPhase = false;
        stagePhase = StagePhase.MAIN_PHASE;

        enemiesHit=0;
        enemiesToKill = currentLevel.killsToWin;


    }

    public long getTimeElapsed() {
        return new Date().getTime() - timeStartedPhase;
    }

    public void handleOnEnemySpriteRemoval(Enemy s) {

            enemiesHit++;

            PointsUpdateThread pointsUpdateThread = new PointsUpdateThread();
            pointsUpdateThread.execute(gameActivity);

        if(stagePhase == StagePhase.MAIN_PHASE) {
            if (!isDoneWithPhase && enemiesHit >= enemiesToKill) {
                isDoneWithPhase = true;
            }
        }
    }

    public void handleOnPlayerSpriteHit(Player p , Enemy enemyToBlame) {


        enemyToBlame.frameForKillingPoorHuman();

        doLose();
    }


    public void handleBossSpriteRemoval() {

    }

    public void doWinAgainstMinions() {
        spriteEssentialData.worldManager.emptyAllBesidesPlayer();
        spriteEssentialData.spriteCreator.createBoss(currentLevel.unlocledPlayable);
    }
    public void doWinBoss() {
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
        Log.i("game over" , "LOSE!!");

       new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                gameActivity.killThread();

                gameActivity.goBackToLevelSelect();
            }
        } , GAME_OVER_TIME_TILL_QUIT);



    }

    public void determinePhase() {
        if (isDoneWithPhase) {
            if(stagePhase == StagePhase.MAIN_PHASE) {
                isDoneWithPhase = false;
                timeStartedPhase = new Date().getTime();
                stagePhase = StagePhase.FINAL_BOSS_ENTERING;
                doWinAgainstMinions();  //switch to this in order to finish stage
            }
            else if(stagePhase == StagePhase.FINAL_BOSS_ENTERING) {
                isDoneWithPhase = false;
                timeStartedPhase = new Date().getTime();
                stagePhase = StagePhase.FINAL_BOSS_FIGHT;
            }
        }
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
        DEFAULT(R.drawable.stickman,R.drawable.stickman, Bullet.SHURIKEN,3),
        BEAR(R.drawable.russianbear_l,R.drawable.russianbear_r,Bullet.SICKLE,8),
        MARIO(R.drawable.mario,R.drawable.marior,Bullet.SPECIAL_STAR,8),
        TERMINATOR(R.drawable.terminatorr_l,R.drawable.terminatorh_r,Bullet.GRENADE,5),
        ROBORABI(R.drawable.robo_rabi_l,R.drawable.robo_rabi_r,Bullet.SEVIVON,6),
        SOLDIER(R.drawable.soldier,R.drawable.soldier,Bullet.MISSILE,7),
        MINYON(R.drawable.wolverine_l,R.drawable.wolverine_r,Bullet.BANANA,8),
        BENDEL(R.drawable.bendel_l,R.drawable.bendel_r,Bullet.GAYKA,8),
        ;

        public int pathToPicHero;
        public int pathToPicBoss;
//        public int pathToPicBullet;
        public Bullet bullet;
        public int fireRate;

        Human(int pathToHeroPic, int pathToBossPic , Bullet bullet, int fireRate)
        {
            this.pathToPicHero = pathToHeroPic;
            this.pathToPicBoss = pathToBossPic;
            this.bullet = bullet;
            this.fireRate=fireRate;

        }

        public int getPathToPicBullet() {
            return bullet.pathToPicBullet;
        }

    }
    public enum Bullet
    {
        SHURIKEN(R.drawable.shuriken,30,5),
        SICKLE(R.drawable.sickle_l,45,15),
        SPECIAL_STAR(R.drawable.special_star,50,20),
        GRENADE(R.drawable.grenade1,55,25),
        SEVIVON(R.drawable.sevivon,60,30),
        MISSILE(R.drawable.missile_l,65,40),
        BANANA(R.drawable.banan,70,43),
        GAYKA(R.drawable.gayka,80,45),
        ;

        public int pathToPicBullet;
        public int initSpeed;
        public int VDSpeed;

        Bullet(int pathToPicBullet, int initSpeed,int VDSpeed)
        {
            this.pathToPicBullet = pathToPicBullet;
            this.initSpeed=initSpeed;
            this.VDSpeed=VDSpeed;
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

    public enum StagePhase{
        MAIN_PHASE,
        FINAL_BOSS_ENTERING,
        FINAL_BOSS_FIGHT;

    }

}
