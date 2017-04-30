package com.gameapps.phillip.singlethreadgame;

import android.util.Log;
import android.view.MotionEvent;

import com.gameapps.phillip.singlethreadgame.gameplay_events.SpriteCreationCause;
import com.gameapps.phillip.singlethreadgame.ready_sprites.BarsSprite;
import com.gameapps.phillip.singlethreadgame.ready_sprites.BulletSprite;
import com.gameapps.phillip.singlethreadgame.ready_sprites.Enemy;
import com.gameapps.phillip.singlethreadgame.ready_sprites.Human;
import com.gameapps.phillip.singlethreadgame.ready_sprites.WorldManager;
import com.gameapps.phillip.singlethreadgame.sprite_definition.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phillip on 1/4/2017.
 */

public class SpriteCreator {

    private Human human;

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
        WorldManager worldManager = new WorldManager(spriteEssentialData);
        spriteEssentialData.logics.addToManagedList(worldManager);
        spriteEssentialData.logics.addToManagedList(spriteEssentialData.spriteCollisions);

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
//            human.shootBullet(touchX , touchY);
            WorldManager.tapX = touchX;
            WorldManager.tapY = touchY;
            WorldManager.isTapped = true;

        }
    }



//Function creates the hero from parameters that she received from spriteEssentialData.Adds to the list of logic and graphics
    private void createHuman() {
        Human h = new Human(spriteEssentialData);

        spriteEssentialData.graphics.addToManagedList(h);
        spriteEssentialData.logics.addToManagedList(h);

        spriteEssentialData.spriteCollisions.addHuman(h);

        this.human = h;

        Log.i("added human" , "" + h);
    }
//    private void createRandomStar() {
//        createStar(
//                MyMath.getRandomUpTo(spriteEssentialData.canvasSize.x),
//                MyMath.getRandomUpTo(spriteEssentialData.canvasSize.y),
//                (int)(spriteEssentialData.canvasSize.x/20),
//                (int)(spriteEssentialData.canvasSize.y/20)
//        );
//    }
//    private void createStar(int starCenterX , int starCenterY , int starWidth , int starHeight) {
//        DroppingStar droppingStar = new DroppingStar(spriteEssentialData ,
//                starCenterX ,
//                starCenterY ,
//                starWidth ,
//                starHeight);
//
//        spriteEssentialData.graphics.addToManagedList(droppingStar);
//        spriteEssentialData.logics.addToManagedList(droppingStar);
////Print log
//        Log.i("added sprite" , "" + droppingStar);
//    }


//Function creates a new enemy, data sets width and height enemy,
// receives a random number (for creating height location of the enemy). Adds to the list logic, graphics and annihilation
    public void createEnemy(Enemy.EnemyType et) {
        int width = 130;
        int height = 170;

        Enemy enemy = new Enemy(et , spriteEssentialData ,
          spriteEssentialData.canvasRect.right+width/3 ,
                MyMath.getRandomUpTo(spriteEssentialData.canvasRect.bottom) , width , height
        );


        spriteEssentialData.graphics.addToManagedList(enemy);
        spriteEssentialData.logics.addToManagedList(enemy);

        spriteEssentialData.spriteCollisions.addEnemy(enemy);

//Print log
        Log.i("added sprite" , "" + enemy);
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

    public Human getHuman() {return human;}
}
