package com.gameapps.alex.singlethreadgame.ready_sprites;

import android.graphics.Rect;
import android.util.Size;

import com.gameapps.alex.singlethreadgame.activities.GameActivity;
import com.gameapps.alex.singlethreadgame.MyMath;
import com.gameapps.alex.singlethreadgame.R;
import com.gameapps.alex.singlethreadgame.sprite_definition.LosingNPCSprite;
import com.gameapps.alex.singlethreadgame.sprite_definition.Sprite;

/**
 * Created by user on 22/01/2017.
 */
//Class takes care of landing of the bars in the middle of of the screen. Class begins run only in a loss in the game.
public class BarsSprite extends Sprite implements LosingNPCSprite{
    private static final double CHANCE_FOR_SPECIAL_STAR = 0.06;

    private static final int HORIZONTAL_SPEED = 300;
//Function receives definitions of bars (height, width, a picture, speed and center points)
    public BarsSprite(GameActivity.SpriteEssentialData spriteEssentialData , int centerX, int centerY, int width, int height) {
        super(spriteEssentialData, centerX, centerY, width, height);
//        isRemovedWhenOffScreen = true;

        size = new Size(width, height);

        setImage(R.drawable.prison_bars);
    }

    @Override
    public void change() {
        moveToMiddle();
    }

    @Override
    //Function calculates the midpoint of of the screen and land the bars in the middle.
    //If bars not moving properly function will recalculate and correct the landing.
    public boolean moveToMiddle() {
        //a single point
        Rect r = new Rect(
                spriteEssentialData.canvasSize.x / 2 ,
                spriteEssentialData.canvasSize.y / 2 ,
                spriteEssentialData.canvasSize.x / 2 ,
                spriteEssentialData.canvasSize.y / 2
        );
        if(MyMath.areRectanglesIntersecting(r , this.getMinimalInsideRect()))
            return true;   //reached middle


////Calculated Correction to Arrival way to center of the screen to the bars
        int directionX = (spriteEssentialData.canvasSize.x / 2) - location.getX();
        int directionY = (spriteEssentialData.canvasSize.y / 2) - location.getY();
        if(directionX < 0) {directionX = Math.min((directionX/MINIMAL_INTERSECTIONS_SIZE)+1 , -MINIMAL_MOVE_SPEED);}
        else {directionX = Math.max((directionX/MINIMAL_INTERSECTIONS_SIZE)+1 , MINIMAL_MOVE_SPEED);}
        if(directionY < 0) {directionY = Math.min((directionY/MINIMAL_INTERSECTIONS_SIZE)+1 , -MINIMAL_MOVE_SPEED);}
        else {directionY = Math.max((directionY/MINIMAL_INTERSECTIONS_SIZE)+1 , MINIMAL_MOVE_SPEED);}


        location.setX(location.getX() + directionX);
        location.setY(location.getY() + directionY);


        return false;

    }
}
