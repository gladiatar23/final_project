package com.gameapps.alex.singlethreadgame.ready_sprites;

import com.gameapps.alex.singlethreadgame.activities.GameActivity;

/**
 * Created by USER on 28/05/2017.
 */

public class ThrownEnemy extends Enemy {

    public final long NUM_OF_ARC_ITERATIONS = 20;
    public final int GRAVITY = 0;

    private long currentIteration;

    //movement
    private static final double VERTICAL_ACCELERATION = 1; //gravity

    private double verticalDropSpeed; //gravity-influenced
    private double direction;

    private int velocity;

    public ThrownEnemy(EnemyType type, GameActivity.SpriteEssentialData spriteEssentialData, int centerX, int centerY, int width, int height , double directionAngle , int initialSpeed) {
        super(type, spriteEssentialData, centerX, centerY, width, height);

        currentIteration = 0;
        isRemovedWhenOffScreen = true;


        this.rotation = (float)directionAngle;
        direction = directionAngle;
        velocity = initialSpeed;

        verticalDropSpeed = GRAVITY;

        rotation = 0;
    }

    @Override
    public void change() {
        currentIteration++;

        //move at arc
        if(currentIteration <= NUM_OF_ARC_ITERATIONS) {
            moveArc();
        }
        else {  //move like normal enemy
            super.change();
        }
    }

    public void moveArc() {

        verticalDropSpeed += VERTICAL_ACCELERATION;

        location.setX(location.getX() + (int)(velocity * Math.cos(direction)));
        location.setY(location.getY() + (int)(velocity * Math.sin(direction)) + (int)verticalDropSpeed);
//Addressing a function to check if an object out of the screen
        setFlagIfOutsideScreen();   //TODO - remove?
    }
}
