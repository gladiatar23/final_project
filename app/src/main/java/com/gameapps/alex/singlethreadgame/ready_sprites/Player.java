package com.gameapps.alex.singlethreadgame.ready_sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.gameapps.alex.singlethreadgame.GameSession;
import com.gameapps.alex.singlethreadgame.R;
import com.gameapps.alex.singlethreadgame.activities.GameActivity;
import com.gameapps.alex.singlethreadgame.sprite_definition.Location;
import com.gameapps.alex.singlethreadgame.sprite_definition.Sprite;

import java.io.IOException;
import java.util.Set;

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
    public static Human currentHero;
    public static Bitmap currentHeroBitmap;
    public static Set<Human> availableHeroes;
    public static enum Human {
        DEFAULT(R.drawable.stickman, R.drawable.stickman, GameSession.Bullet.SHURIKEN, 3, 5),
        BEAR(R.drawable.russianbear_l, R.drawable.russianbear_r, GameSession.Bullet.SICKLE, 8, 5),
        MARIO(R.drawable.mario, R.drawable.marior, GameSession.Bullet.SPECIAL_STAR, 8, 5),
        TERMINATOR(R.drawable.terminatorr_l, R.drawable.terminatorr_r, GameSession.Bullet.GRENADE, 5, 5),
        ROBORABI(R.drawable.robo_rabi_l, R.drawable.robo_rabi_r, GameSession.Bullet.SEVIVON, 6, 5),
        MINYON(R.drawable.wolverine_l, R.drawable.wolverine_r, GameSession.Bullet.BANANA, 8, 2),
        MOTARO(R.drawable.motaro_l , R.drawable.motaro_r , GameSession.Bullet.DIPERS, 8,5),
        FRADY(R.drawable.frady_l , R.drawable.frady_r , GameSession.Bullet.PIZZA , 8,5),
        GOBLIN(R.drawable.goblin_l , R.drawable.goblin_r , GameSession.Bullet.MISSILE , 8,5);

        public int pathToPicHero;
        public int pathToPicBoss;
        //        public int pathToPicBullet;
        public GameSession.Bullet bullet;
        public int fireRate;

        public long initialBossHP;

        Human(int pathToHeroPic, int pathToBossPic, GameSession.Bullet bullet, int fireRate, long initialBossHP) {
            this.pathToPicHero = pathToHeroPic;
            this.pathToPicBoss = pathToBossPic;
            this.bullet = bullet;
            this.fireRate = fireRate;
            this.initialBossHP = initialBossHP;
        }

        public int getPathToPicBullet() {
            return bullet.pathToPicBullet;
        }

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
