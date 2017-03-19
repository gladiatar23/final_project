package com.gameapps.phillip.singlethreadgame.ready_sprites;

import android.graphics.Rect;

import com.gameapps.phillip.singlethreadgame.GameActivity;
import com.gameapps.phillip.singlethreadgame.MyMath;
import com.gameapps.phillip.singlethreadgame.R;
import com.gameapps.phillip.singlethreadgame.sprite_definition.LosingNPCSprite;
import com.gameapps.phillip.singlethreadgame.sprite_definition.Sprite;

/**
 * Created by user on 11/01/2017.
 */

public class Enemy extends Sprite implements LosingNPCSprite {
    private static final int ITERATIONS_PERSISTING_VETICAL = 1000;//ITERATIONS PERSISTING VERTICAL (fps)

    private boolean isFramedForKillingPoorHuman;
    private boolean isStillMoving;

    private long iterationsExisted;

    private static final int MAX_VERTICAL_SPEED = 3;
    private static final int HORIZONTAL_SPEED = 26;
    private int verticalSpeed;
//Function creates enemies, some of the features of their logical (as vertical speed), and adds a picture
    public Enemy(GameActivity.SpriteEssentialData spriteEssentialData, int centerX, int centerY, int width, int height) {
        super(spriteEssentialData, centerX, centerY, width, height);

        iterationsExisted = 0;
        verticalSpeed = 0;
        isRemovedWhenOffScreen = false;
        isStillMoving = true;

        setImage(R.drawable.rabi);

        isFramedForKillingPoorHuman = false;
    }
//Function calls to check if main character was killed, then calls the enemy reach the center of the screen and activates the bars
    @Override
    public void change() {
        if(!isStillMoving)
            return;


        if(isFramedForKillingPoorHuman) {
            if(moveToMiddle()) {
                spriteEssentialData.spriteCreator.createBars(this);
                isStillMoving = false;
            }
        }
        else {
            location.setX(location.getX() - HORIZONTAL_SPEED);
            if ((++iterationsExisted) % ITERATIONS_PERSISTING_VETICAL == 0) {
                verticalSpeed = MyMath.getRandomUpToIncludingNegative(MAX_VERTICAL_SPEED);
            }
            location.setY(location.getY() + verticalSpeed);
        }

    }

    public void frameForKillingPoorHuman() {isFramedForKillingPoorHuman = true;}
//The function returns parameters (middle of the screen) to go to the enemy who killed the hero
    @Override
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


//Calculated Correction to Arrival way to center of the screen to the enemy that hit the main hero
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
