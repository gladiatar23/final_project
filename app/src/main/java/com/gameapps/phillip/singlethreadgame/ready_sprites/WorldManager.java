package com.gameapps.phillip.singlethreadgame.ready_sprites;

import com.gameapps.phillip.singlethreadgame.GameActivity;
import com.gameapps.phillip.singlethreadgame.GameSession;
import com.gameapps.phillip.singlethreadgame.sprite_definition.LogicalElement;

/**
 * Created by user on 11/01/2017.
 */

public class WorldManager implements LogicalElement {
    //how many interactions to wait create an enemy
    public static int interatoinsForEnemyCreation = 30;

    public static boolean isTapped;
    public static int tapX , tapY;

    GameActivity.SpriteEssentialData spriteEssentialData;

    long currentIteration;

    public WorldManager(GameActivity.SpriteEssentialData spriteEssentialData) {
        this.spriteEssentialData = spriteEssentialData;

        currentIteration = 1;
        isTapped = false;
    }

    @Override
    public void change() {
        currentIteration++;
//Calculating the chances of creating a new enemy
        if(currentIteration % interatoinsForEnemyCreation == 0) {
            createEnemy(GameSession.currentLevel.enemyType);
        }
        
        //create a bullet for player
        if(isTapped) {
            spriteEssentialData.spriteCreator.getHuman().shootBullet(tapX , tapY);
            isTapped = false;
        }
    }
//Addressing a function to check if an object out of the screen
    @Override
    public void setFlagIfOutsideScreen() {

    }
    // put an annihilation flag to sprite
    @Override
    public void flagForRemoval() {

    }

    @Override
    public boolean isFlaggedForRemoval() {
        return false;
    }

    private void createEnemy(Enemy.EnemyType et) {
        spriteEssentialData.spriteCreator.createEnemy(et);
    }

    private void createBullet() {

    }

}
