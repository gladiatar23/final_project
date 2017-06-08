package com.gameapps.phillip.singlethreadgame;

import android.util.Log;
import android.view.MotionEvent;

import com.gameapps.phillip.singlethreadgame.gameplay_events.SpriteCreationCause;
import com.gameapps.phillip.singlethreadgame.ready_sprites.BarsSprite;
import com.gameapps.phillip.singlethreadgame.ready_sprites.BossEnemy;
import com.gameapps.phillip.singlethreadgame.ready_sprites.BulletSprite;
import com.gameapps.phillip.singlethreadgame.ready_sprites.Enemy;
import com.gameapps.phillip.singlethreadgame.ready_sprites.Player;
import com.gameapps.phillip.singlethreadgame.ready_sprites.ThrownEnemy;
import com.gameapps.phillip.singlethreadgame.ready_sprites.WorldManager;
import com.gameapps.phillip.singlethreadgame.sprite_definition.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phillip on 1/4/2017.
 */

public class SpriteCreator {

    private Player player;
    private BossEnemy boss;

    private static final int enemy_width = 130;
    private static final int enemy_height = 170;


    GameActivity.SpriteEssentialData spriteEssentialData;

    private List<SpriteCreationCause> spriteCreators;

    //Function generates a list of sprite creators
    public SpriteCreator(GameActivity.SpriteEssentialData spriteEssentialData) {
        this.spriteEssentialData = spriteEssentialData;

        this.spriteCreators = new ArrayList<>();

        initializeSprites();
    }
//At the beginning of the running of the program function creates a new world list and adds it to the lists Logic,
//Further created main character
    public void initializeSprites() {
        //createRandomStar();
        createHuman();
    }
//Function receives parameters of a touch screen to direct shots.
    public void handleMotionEvent(MotionEvent event) {
        int touchX =  (int) (event.getX() - spriteEssentialData.graphics.getLeft());
        int touchY = (int) (event.getY() - spriteEssentialData.graphics.getTop());

//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            createStar(
//                    touchX,
//                    touchY,
//                    spriteEssentialData.canvasSize.x / 20,
//                    spriteEssentialData.canvasSize.y / 20
//            );
//        }
//Working on lowering finger on the screen
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            player.shootBullet(touchX , touchY);
            WorldManager.tapX = touchX;
            WorldManager.tapY = touchY;
            WorldManager.isTapped = true;


        }
    }



//Function creates the hero from parameters that she received from spriteEssentialData.Adds to the list of logic and graphics
    private void createHuman() {
        Player h = new Player(spriteEssentialData);

        spriteEssentialData.graphics.addToManagedList(h);
        spriteEssentialData.logics.addToManagedList(h);

        spriteEssentialData.spriteCollisions.addHuman(h);

        this.player = h;

        Log.i("added player" , "" + h);
    }


//Function creates a new enemy, data sets width and height enemy,
// receives a random number (for creating height location of the enemy). Adds to the list logic, graphics and annihilation
    public void createEnemy(Enemy.EnemyType et) {

        Enemy enemy = new Enemy(et , spriteEssentialData ,
          spriteEssentialData.canvasRect.right+enemy_width/3 ,
                MyMath.getRandomUpTo(spriteEssentialData.canvasRect.bottom) , enemy_width , enemy_height
        );


        spriteEssentialData.graphics.addToManagedList(enemy);
        spriteEssentialData.logics.addToManagedList(enemy);

        spriteEssentialData.spriteCollisions.addEnemy(enemy);

//Print log
        Log.i("added sprite" , "" + enemy);
    }

    public void createBoss(GameSession.Human humanEnum) {
        BossEnemy bossEnemy = new BossEnemy(humanEnum , spriteEssentialData);

        spriteEssentialData.graphics.addToManagedList(bossEnemy);
        spriteEssentialData.logics.addToManagedList(bossEnemy);

        spriteEssentialData.spriteCollisions.addEnemy(bossEnemy);

        Log.i("added boss" , "" + bossEnemy);
    }


//Function accepts parameters of the pathToPicBullet, and adds it to the list of logic, graphics and annihilation
    public void createBullet(int bulletCenterX , int bulletCenterY , double angle) {
        BulletSprite bulletSprite = new BulletSprite(spriteEssentialData ,
                bulletCenterX , bulletCenterY , angle);


        spriteEssentialData.graphics.addToManagedList(bulletSprite);
        spriteEssentialData.logics.addToManagedList(bulletSprite);
        spriteEssentialData.spriteCollisions.addBullet(bulletSprite);

//Print log
        Log.i("added sprite" , "" + bulletSprite);
    }

    public void createThrownEnemy() {

        //TODO - get angle and initial speed
        ThrownEnemy thrownEnemySprite = new ThrownEnemy(GameSession.currentLevel.enemyType , spriteEssentialData ,
                boss.getLocation().getX(), boss.getLocation().getY(), enemy_width, enemy_height , boss.getAngleOfThrow() , boss.getThrowSpeed());

        spriteEssentialData.graphics.addToManagedList(thrownEnemySprite);
        spriteEssentialData.logics.addToManagedList(thrownEnemySprite);
        spriteEssentialData.spriteCollisions.addEnemy(thrownEnemySprite);
    }

//Function creates a new bars, checks the midpoint of the screen and adds to the list of logic and graphics
    public void createBars(Sprite onto) {
        BarsSprite barsSprite = new BarsSprite(spriteEssentialData ,
                onto.getAreaRect().centerX() ,
                0 ,
                (int)(onto.getAreaRect().width()*2) ,
                (int)(onto.getAreaRect().height()*2)
        );

        spriteEssentialData.graphics.addToManagedList(barsSprite);
        spriteEssentialData.logics.addToManagedList(barsSprite);
//Print log
        Log.i("added sprite" , "" + barsSprite);
    }

    public Player getPlayer() {return player;}

    public void setBoss(BossEnemy boss) {
        this.boss = boss;
    }
}
