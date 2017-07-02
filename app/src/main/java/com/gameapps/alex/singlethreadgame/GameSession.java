package com.gameapps.alex.singlethreadgame;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.gameapps.alex.singlethreadgame.activities.GameActivity;
import com.gameapps.alex.singlethreadgame.data_handle.DBLevelHandler;
import com.gameapps.alex.singlethreadgame.data_handle.LevelForTable;
import com.gameapps.alex.singlethreadgame.ready_sprites.BossEnemy;
import com.gameapps.alex.singlethreadgame.ready_sprites.Enemy;
import com.gameapps.alex.singlethreadgame.ready_sprites.Player;

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
    public static Bitmap currentHeroBitmap;
    public static Set<Human> availableHeroes;

    public static boolean isFacebook;

    public StagePhase stagePhase;
    public boolean isDoneWithPhase;
    public long timeStartedPhase;

    int score;
    int enemiesHit;
    int enemiesToKill;

    GameActivity gameActivity;
    GameActivity.SpriteEssentialData spriteEssentialData;

    public static void initializeStatics(boolean isFromFacebook) {
        if (isFromFacebook) {
            currentHero = GameSession.Human.DEFAULT;
        } else {
            GameSession.currentHeroBitmap = null;   //no special bitmap
        }
        isFacebook = isFromFacebook;
    }

    public GameSession(GameActivity gameActivity, GameActivity.SpriteEssentialData spriteEssentialData) {
        this.gameActivity = gameActivity;
        this.spriteEssentialData = spriteEssentialData;

        score = 0;
        ctorStuff();

    }

    private void ctorStuff() {
        this.isDoneWithPhase = false;
        stagePhase = StagePhase.MAIN_PHASE;

        enemiesToKill = currentLevel.killsToWin;
        enemiesHit = 0;

    }

    public long getTimeElapsed() {
        return new Date().getTime() - timeStartedPhase;
    }

    public void handleOnEnemySpriteRemoval(Enemy s) {

        enemiesHit++;
        score++;

        PointsUpdateThread pointsUpdateThread = new PointsUpdateThread();
        pointsUpdateThread.execute(gameActivity);

        if (stagePhase == StagePhase.MAIN_PHASE) {
            if (!isDoneWithPhase && enemiesHit >= enemiesToKill) {
                isDoneWithPhase = true;
            }
        } else if (stagePhase == StagePhase.FINAL_BOSS_FIGHT) {
            if (s instanceof BossEnemy) {
                doWinBoss();
            }
        }
    }

    public void handleOnPlayerSpriteHit(Player p, Enemy enemyToBlame) {


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
        if (isFacebook) {
            //TODO - if at last level, win
            currentLevel = Level.getNextLevelFor(currentLevel);
            ctorStuff();
            spriteEssentialData.graphics.placeBackground();

        } else {


            gameActivity.killThread();

            Log.i("game over", "WIN!!");

            DBLevelHandler db = DBLevelHandler.getInstance(gameActivity);
            List<LevelForTable> levels = db.getAllLevels();

            boolean isExisting = false;
            LevelForTable currentForTable = null;
            for (LevelForTable l : levels) {
                if (l.getId() == currentLevel.id) {
                    isExisting = true;
                    currentForTable = l;
                    break;
                }
            }

            if (!isExisting) {
                currentForTable = new LevelForTable(currentLevel.id, currentLevel.name(), 1, enemiesHit);
            } else {
                currentForTable.setWon(1);
                if (currentForTable.getBestScore() < score) {
                    currentForTable.setBestScore(score);
                }
            }

            db.addLevel(currentForTable);

            gameActivity.goBackToLevelSelect();
        }

    }

    public void doLose() {
        Log.i("game over", "LOSE!!");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                gameActivity.killThread();

                gameActivity.goBackToLevelSelect();
            }
        }, GAME_OVER_TIME_TILL_QUIT);


    }

    public void determinePhase() {
        if (isDoneWithPhase) {
            if (stagePhase == StagePhase.MAIN_PHASE) {
                isDoneWithPhase = false;
                timeStartedPhase = new Date().getTime();
                stagePhase = StagePhase.FINAL_BOSS_ENTERING;
                doWinAgainstMinions();  //switch to this in order to finish stage
            } else if (stagePhase == StagePhase.FINAL_BOSS_ENTERING) {
                isDoneWithPhase = false;
                timeStartedPhase = new Date().getTime();
                stagePhase = StagePhase.FINAL_BOSS_FIGHT;
            }
        }
    }

    public enum Level {
        MOSCOW(0, 6, R.drawable.moscow, Enemy.EnemyType.JIHADIST, Human.BEAR),
        HONGKONG(1, 6, R.drawable.hongkong, Enemy.EnemyType.TURTLE, Human.MARIO),
        NEW_YORK(2, 6, R.drawable.newyork, Enemy.EnemyType.SHVARCNEGER, Human.TERMINATOR),
        JERUSALEM(3, 6, R.drawable.jerusalem, Enemy.EnemyType.WAZE, Human.ROBORABI),
        PARIS(4, 30, R.drawable.paris, Enemy.EnemyType.VAMINYON, Human.MINYON),
        LONDON(5, 6, R.drawable.london, Enemy.EnemyType.BENDEL, Human.GOBLIN),
        TOKYO(6, 6, R.drawable.tokyo, Enemy.EnemyType.HAUNTER, Human.MOTARO),
        ROME(7, 6, R.drawable.rome, Enemy.EnemyType.SOLDIER, Human.FRADY),
        ;


        public int id;
        public int killsToWin;
        public int pathToBG;
        public Enemy.EnemyType enemyType;
        public Human unlocledPlayable;

        Level(int id, int killsToWin, int pathToBG, Enemy.EnemyType enemyType, Human unlocledPlayable) {
            this.id = id;
            this.killsToWin = killsToWin;
            this.pathToBG = pathToBG;
            this.enemyType = enemyType;
            this.unlocledPlayable = unlocledPlayable;
        }

        public static Level getNextLevelFor(Level level) {
            if(level == null) return values()[0];
            for(int i = 0 ; i < values().length-1 ; i++) {
                if(level == values()[i]) {
                    return values()[i+1];
                }
            }


            //next for last stage
            return values()[0]; //TODO - change
        }

    }

    public enum Human {
        DEFAULT(R.drawable.stickman, R.drawable.stickman, Bullet.DIPERS, 3, 5),
        BEAR(R.drawable.russianbear_l, R.drawable.russianbear_r, Bullet.SICKLE, 8, 5),
        MARIO(R.drawable.mario, R.drawable.marior, Bullet.SPECIAL_STAR, 8, 5),
        TERMINATOR(R.drawable.terminatorr_l, R.drawable.terminatorr_r, Bullet.GRENADE, 5, 5),
        ROBORABI(R.drawable.robo_rabi_l, R.drawable.robo_rabi_r, Bullet.SEVIVON, 6, 5),
        MINYON(R.drawable.wolverine_l, R.drawable.wolverine_r, Bullet.BANANA, 8, 2),
        MOTARO(R.drawable.motaro_l , R.drawable.motaro_r , Bullet.SHURIKEN, 8,5),
        FRADY(R.drawable.frady_l , R.drawable.frady_r , Bullet.PIZZA , 8,5),
        GOBLIN(R.drawable.goblin_l , R.drawable.goblin_r , Bullet.MISSILE , 8,5);

        public int pathToPicHero;
        public int pathToPicBoss;
        //        public int pathToPicBullet;
        public Bullet bullet;
        public int fireRate;

        public long initialBossHP;

        Human(int pathToHeroPic, int pathToBossPic, Bullet bullet, int fireRate, long initialBossHP) {
            this.pathToPicHero = pathToHeroPic;
            this.pathToPicBoss = pathToBossPic;
            this.bullet = bullet;
            this.fireRate = fireRate;
            this.initialBossHP = initialBossHP;
        }

        public int getPathToPicBullet() {
            return bullet.pathToPicBullet;
        }

    }

    public enum Bullet {
        SHURIKEN(R.drawable.shuriken, 80, 45),
        SICKLE(R.drawable.sickle_l, 45, 15),
        SPECIAL_STAR(R.drawable.special_star, 50, 20),
        GRENADE(R.drawable.grenade1, 55, 25),
        SEVIVON(R.drawable.sevivon, 60, 30),
        MISSILE(R.drawable.gayka, 65, 40),
        BANANA(R.drawable.banan, 70, 43),
        DIPERS(R.drawable.dipers, 80, 45),
        PIZZA(R.drawable.pizza,80,48);

        public int pathToPicBullet;
        public int initSpeed;
        public int VDSpeed;

        Bullet(int pathToPicBullet, int initSpeed, int VDSpeed) {
            this.pathToPicBullet = pathToPicBullet;
            this.initSpeed = initSpeed;
            this.VDSpeed = VDSpeed;
        }


    }

    public class PointsUpdateThread extends AsyncTask<GameActivity, GameActivity, String> {

        public boolean isRunning;

        @Override
        protected String doInBackground(GameActivity... params) {
            publishProgress(params);
            return null;
        }

        protected void onProgressUpdate(GameActivity... progress) {
            if (progress[0] == null) return;

            GameActivity activityCalled = progress[0];
            activityCalled.setScore(score);
        }

        protected void onPostExecute(String... strs) {

        }
    }

    public enum StagePhase {
        MAIN_PHASE,
        FINAL_BOSS_ENTERING,
        FINAL_BOSS_FIGHT;

    }

}
