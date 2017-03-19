package com.gameapps.phillip.singlethreadgame.ready_sprites;
import com.gameapps.phillip.singlethreadgame.GameActivity;
import com.gameapps.phillip.singlethreadgame.MyMath;
import com.gameapps.phillip.singlethreadgame.sprite_definition.LogicalElement;
import com.gameapps.phillip.singlethreadgame.sprite_definition.Sprite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by user on 22/01/2017.
 */

public class SpriteCollisions implements LogicalElement {

    Human human;
    Set<BulletSprite> allBullets;
    Set<Enemy> allEnemies;
    public static int myScore=0;


    protected GameActivity.SpriteEssentialData spriteEssentialData;

    public SpriteCollisions(GameActivity.SpriteEssentialData spriteEssentialData) {
        allBullets = new HashSet<>();
        allEnemies = new HashSet<>();

        this.spriteEssentialData = spriteEssentialData;
    }

    public void addBullet(BulletSprite bulletSprite) {allBullets.add(bulletSprite);}
    public void addEnemy(Enemy enemySprite) {allEnemies.add(enemySprite);}
    public void addHuman(Human h) {this.human = h;}





    @Override
    //Function checks if the bullet hit the enemy
    //If yes the function marks the specific elements for deletion
    public void change() {
        //check for enemy kills
        for(BulletSprite b : allBullets) {
            for(Enemy e : allEnemies) {
                //check if colliding
                if(MyMath.areRectanglesIntersecting(b.getAreaRect() , e.getAreaRect())) {
                    b.flagForRemoval();
                    e.flagForRemoval();
                }
            }
        }
       //  checks if the enemy hit (enters rectangle) main hero
        //If yes mark all other elements for deletion, except an enemy that hit the main hero
        for(Enemy e : allEnemies) {
            if(MyMath.areRectanglesIntersecting(human.getAreaRect() , e.getAreaRect())) {
//                allBullets = new HashSet<>();
//                allEnemies = new HashSet<>();

                spriteEssentialData.logics.removeAllItems();
                spriteEssentialData.graphics.removeAllItems();

                spriteEssentialData.logics.addToManagedList(e);
                spriteEssentialData.graphics.addToManagedList(e);
                e.frameForKillingPoorHuman();

                break;

            }
        }


        //garbage collector
        removeFlaggedSprites(allBullets);
        removeFlaggedSprites(allEnemies);
    }
//Function erases marked sprites
    private <E extends Sprite> void removeFlaggedSprites(Set<E> spriteSet) {
        List<E> setCopy = new ArrayList<>(spriteSet);

        for(E element : setCopy) {
            if(element.isFlaggedForRemoval()) {
                spriteSet.remove(element);
            }
        }
    }

    @Override
    public void setFlagIfOutsideScreen() {

    }

    @Override
    public void flagForRemoval() {

    }

    @Override
    public boolean isFlaggedForRemoval() {
        return false;
    }


}
