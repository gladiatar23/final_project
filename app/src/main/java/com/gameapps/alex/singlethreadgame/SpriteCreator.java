package com.gameapps.alex.singlethreadgame;

import android.graphics.Point;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;

import com.gameapps.alex.singlethreadgame.activities.GameActivity;
import com.gameapps.alex.singlethreadgame.gameplay_events.SpriteCreationCause;
import com.gameapps.alex.singlethreadgame.ready_sprites.BarsSprite;
import com.gameapps.alex.singlethreadgame.ready_sprites.BossEnemy;
import com.gameapps.alex.singlethreadgame.ready_sprites.BulletSprite;
import com.gameapps.alex.singlethreadgame.ready_sprites.Enemy;
import com.gameapps.alex.singlethreadgame.ready_sprites.Player;
import com.gameapps.alex.singlethreadgame.ready_sprites.ThrownEnemy;
import com.gameapps.alex.singlethreadgame.ready_sprites.WorldManager;
import com.gameapps.alex.singlethreadgame.sprite_definition.Location;
import com.gameapps.alex.singlethreadgame.sprite_definition.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phillip on 1/4/2017.
 */

public class SpriteCreator {

    private Player player;
    private BossEnemy boss;


    private static final double ENEMY_WIDTH_TO_SCREEN_WIDTH = (double)(1)/15;
    private static final double ENEMY_HEIGHT_TO_OWN_WIDTH = 1.6;

    private static final int enemy_width = 100;
    private static final int enemy_height = 135;


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
        Point p = getEnemySizes();

        Enemy enemy = new Enemy(et , spriteEssentialData ,
          spriteEssentialData.canvasRect.right+enemy_width/3 ,
                MyMath.getRandomUpTo(spriteEssentialData.canvasRect.bottom) , p.x , p.y
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

        Point p = getEnemySizes();

        //TODO - get angle and initial speed
        ThrownEnemy thrownEnemySprite = new ThrownEnemy(GameSession.currentLevel.enemyType , spriteEssentialData ,
                boss.getLocation().getX(), boss.getLocation().getY(), p.x, p.y , boss.getAngleOfThrow() , boss.getThrowSpeed());

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

    private Point getEnemySizes() {
        int enemyWidthByRatio = (int) (ENEMY_WIDTH_TO_SCREEN_WIDTH * spriteEssentialData.canvasSize.x);
        int enemyHeightByRatio = (int) (enemyWidthByRatio * ENEMY_HEIGHT_TO_OWN_WIDTH);

        Log.i("sizes_are" , " " + enemyWidthByRatio + ", " + enemyHeightByRatio);
        Log.i("sizes_are2" , " " + spriteEssentialData.canvasSize.x + ", " + spriteEssentialData.canvasSize.y);
        Log.i("sizes_are3" , " " + spriteEssentialData.canvasRect.width() + ", " + spriteEssentialData.canvasRect.width());

        return new Point(enemyWidthByRatio , enemyHeightByRatio);
    }
}
