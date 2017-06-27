package com.gameapps.alex.singlethreadgame.ready_sprites;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.gameapps.alex.singlethreadgame.R;
import com.gameapps.alex.singlethreadgame.activities.GameActivity;
import com.gameapps.alex.singlethreadgame.sprite_definition.Location;
import com.gameapps.alex.singlethreadgame.sprite_definition.Sprite;

import java.io.IOException;

/**
 * Created by user on 11/01/2017.
 */

public class Player extends Sprite {

    private static final int MOVE_SPEED = 10;
    private static final double RATIO_TO_SCREEN_HEIGHT = (double)1/3;

    private static final double SHIELD_RATIO = 1.3;

    private boolean isUpPressed;
    private boolean isDownPressed;

    pl.droidsonroids.gif.GifDrawable gifFromResource;

//This function provides definitions (height and width, position on the screen, an image), the main character.
    public Player(GameActivity.SpriteEssentialData spriteEssentialData) {
        super(spriteEssentialData, 0, 0, 0, 0);

//        double hToWRatio = 2;
//        int height = spriteEssentialData.canvasSize.y/3;
//        this.size = new Size((int)(height/hToWRatio) , height);

        if(spriteEssentialData.gameSession.currentHeroBitmap != null) {
            setImageAndSizes(spriteEssentialData.gameSession.currentHeroBitmap , RATIO_TO_SCREEN_HEIGHT);
        }
        else {
            setImageAndSizes(spriteEssentialData.gameSession.currentHero.pathToPicHero, RATIO_TO_SCREEN_HEIGHT);
        }
        this.location = new Location(size.getWidth()/2 , spriteEssentialData.canvasSize.y * 4/7);

        try {
            gifFromResource = new pl.droidsonroids.gif.GifDrawable(spriteEssentialData.ctx.getResources(), R.drawable.oie_trans);
            gifFromResource.setVisible(true, true);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void change() {
        if(isUpPressed) {
            this.location.setY(location.getY()-MOVE_SPEED);
            isUpPressed = false;
        }
        if(isDownPressed) {
            this.location.setY(location.getY()+MOVE_SPEED);
            isDownPressed = false;
        }

        //update shield
        gifFromResource.setBounds(getAreaRect().left , getAreaRect().top , getAreaRect().right , getAreaRect().bottom);

    }
//The function receives location coordinates of the click on the screen and calculates arc tangent a pathToPicBullet fired.
    public void shootBullet(int xToShootAt , int yToShootAt) {
        if(isFlaggedForRemoval()) return;

        double distanceX = xToShootAt - location.getX();
        double distanceY = yToShootAt - location.getY();

        double angle = Math.atan(distanceY / distanceX);

        spriteEssentialData.spriteCreator.createBullet(location.getX() ,
                location.getY(),
                angle
                );
    }


    public void getHit(Enemy e) {
        //TODO - if testing for death

        //still alive
        //TODO

        //dead (rest in piss)
        flagForRemoval();
        spriteEssentialData.gameSession.handleOnPlayerSpriteHit(this , e);
    }

    public void setUpToPressed() {this.isUpPressed = true;}
    public void setDownToPressed() {this.isDownPressed = true;}

    @Override
    public void flagForRemoval() {
        super.flagForRemoval();
        //transport to a distant location
        this.location = new Location(100000 , 100000);
    }

    @Override
    public void drawSelf(Canvas canvas) {
        super.drawSelf(canvas);

        Log.i("dsfedagdfg" , "" + gifFromResource.isPlaying());
//        gifFromResource.draw(canvas);
    }
}
