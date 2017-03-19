package com.gameapps.phillip.singlethreadgame.ready_sprites;

import android.util.Size;

import com.gameapps.phillip.singlethreadgame.GameActivity;
import com.gameapps.phillip.singlethreadgame.MyMath;
import com.gameapps.phillip.singlethreadgame.R;
import com.gameapps.phillip.singlethreadgame.sprite_definition.Sprite;

/**
 * Created by user on 18/01/2017.
 */
//The class defines all connected to bullet shooting. height, width, speed, angle, ballistics, direction and image set
public class BulletSprite extends Sprite {

    public static final int BULLET_WIDTH = 100;
    public static final int BULLET_HEIGHT = 100;
    public static final int BULLET_INIT_VELOCITY = 60;//movement speed

    private static final double VERTICAL_ACCELERATION = 1; //gravity
    private double verticalDropSpeed; //gravity-influenced



    private double direction;
    private int velocity;
//Settings drawing and movment bullet
    public BulletSprite(GameActivity.SpriteEssentialData spriteEssentialData , int centerX, int centerY , double directionAngle) {
        super(spriteEssentialData , centerX , centerY , BULLET_WIDTH, BULLET_HEIGHT);
        isRemovedWhenOffScreen = true;


        this.rotation = (float)directionAngle;
        direction = directionAngle;
        velocity = BULLET_INIT_VELOCITY;

        verticalDropSpeed = 17;

        rotation = 0;
        size = new Size(BULLET_WIDTH, BULLET_HEIGHT);
//gets the bullet pic
        setImage(R.drawable.shit);
    }

    @Override
    //Ballistics Calculation
    public void change() {

        verticalDropSpeed += VERTICAL_ACCELERATION;

        location.setX(location.getX() + (int)(velocity * Math.cos(direction)));
        location.setY(location.getY() + (int)(velocity * Math.sin(direction)) + (int)verticalDropSpeed);
//Addressing a function to check if an object out of the screen
        setFlagIfOutsideScreen();
    }


}
