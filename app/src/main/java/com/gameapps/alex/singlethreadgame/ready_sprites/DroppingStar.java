package com.gameapps.alex.singlethreadgame.ready_sprites;

import android.util.Size;

import com.gameapps.alex.singlethreadgame.activities.GameActivity;
import com.gameapps.alex.singlethreadgame.MyMath;
import com.gameapps.alex.singlethreadgame.R;
import com.gameapps.alex.singlethreadgame.sprite_definition.Sprite;

/**
 * Created by Phillip on 1/4/2017.
 */

public class DroppingStar extends Sprite {
    private static final double CHANCE_FOR_SPECIAL_STAR = 0.06;

    private static final double ROTATION_SPEED = 0.15;
    private static final int VERTICAL_ACCELERATION = 1;


    private int horizontalSpeed;
    private int dropSpeed;

    public DroppingStar(GameActivity.SpriteEssentialData spriteEssentialData , int centerX, int centerY, int width, int height) {
        super(spriteEssentialData , centerX , centerY , width , height);
        isRemovedWhenOffScreen = true;

        horizontalSpeed = 0;
        dropSpeed = 0;

        rotation = 0;
        int squareSize = Math.min(width , height);
        size = new Size(squareSize , squareSize);

        if(MyMath.doRandomChance(CHANCE_FOR_SPECIAL_STAR))
            setImage(R.drawable.special_star);
        else
            setImage(R.drawable.shit);
    }

    @Override
    public void change() {
        rotation += Math.PI * ROTATION_SPEED;

        dropSpeed += VERTICAL_ACCELERATION;
        location.setX(location.getX() + horizontalSpeed);
        location.setY(location.getY() + dropSpeed);


        setFlagIfOutsideScreen();
    }


}
