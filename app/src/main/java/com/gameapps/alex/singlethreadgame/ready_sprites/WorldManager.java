package com.gameapps.alex.singlethreadgame.ready_sprites;

import com.gameapps.alex.singlethreadgame.GameActivity;
import com.gameapps.alex.singlethreadgame.GameSession;
import com.gameapps.alex.singlethreadgame.sprite_definition.LogicalElement;

/**
 * Created by user on 11/01/2017.
 */

public class WorldManager implements LogicalElement {
    //how many interactions to wait create an enemy
    public static int interatoinsForEnemyCreation = 30;
    public static final int BOSS_CEASEFIRE_TIME = 0;

    public static boolean isTapped;
    public static int tapX , tapY;

    public static boolean isMinionThrown;

    GameActivity.SpriteEssentialData spriteEssentialData;

    long currentIteration;

    public WorldManager(GameActivity.SpriteEssentialData spriteEssentialData) {
        this.spriteEssentialData = spriteEssentialData;

        currentIteration = 1;
        isTapped = false;
        isMinionThrown = false;
    }

    @Override
    public void change() {
        currentIteration++;

        if(spriteEssentialData.gameSession.stagePhase == GameSession.StagePhase.MAIN_PHASE) {
            spawnEnemy();
            doShooting();
        }
        else if(spriteEssentialData.gameSession.stagePhase == GameSession.StagePhase.FINAL_BOSS_FIGHT) {
            if(spriteEssentialData.gameSession.getTimeElapsed() > BOSS_CEASEFIRE_TIME) {
                doShooting();
            }

            if(isMinionThrown) {
                spriteEssentialData.spriteCreator.createThrownEnemy();
            }
        }

    }

    /**
     * Calculating the chances of creating a new enemy
     */
    private void spawnEnemy() {
        if (currentIteration % interatoinsForEnemyCreation == 0) {
            createEnemy(GameSession.currentLevel.enemyType);
        }
    }
    /**
     * create a bullet for player
     */
    private void doShooting() {
        if (isTapped) {
            spriteEssentialData.spriteCreator.getPlayer().shootBullet(tapX, tapY);
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

    public void emptyAllBesidesPlayer() {

        spriteEssentialData.spriteCollisions.dumpEnemiesAndBullets();
        spriteEssentialData.logics.removeAllItems();
        spriteEssentialData.graphics.removeAllItems();


        spriteEssentialData.logics.addToManagedList(spriteEssentialData.spriteCreator.getPlayer());
        spriteEssentialData.graphics.addToManagedList(spriteEssentialData.spriteCreator.getPlayer());
    }

}
