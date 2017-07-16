package com.gameapps.alex.singlethreadgame.ready_sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
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

    private static final double SHIELD_RATIO_MAX = 2.2;
    private static final double SHIELD_RATIO_MIN = 1.9;
    private static final double DEGREES_PER_ITERATION = 0.33;
    private double shieldSineValue;

    private boolean isUpPressed;
    private boolean isDownPressed;

    private boolean isThereShieldLeft;
    private boolean isShieldActivated;

    pl.droidsonroids.gif.GifDrawable gifFromResource;

//This function provides definitions (height and width, position on the screen, an image), the main character.
    public Player(GameActivity.SpriteEssentialData spriteEssentialData) {
        super(spriteEssentialData, 0, 0, 0, 0);

        isThereShieldLeft = true;
        isShieldActivated = false;

        shieldSineValue = DEGREES_PER_ITERATION;



        if(spriteEssentialData.gameSession.currentHeroBitmap != null) {
            setImageAndSizes(spriteEssentialData.gameSession.currentHeroBitmap , RATIO_TO_SCREEN_HEIGHT);
        }
        else {
            setImageAndSizes(spriteEssentialData.gameSession.currentHero.pathToPicHero, RATIO_TO_SCREEN_HEIGHT);
        }
        this.location = new Location(size.getWidth()/2+10 , spriteEssentialData.canvasSize.y * 4/7);

        try {
            gifFromResource = new pl.droidsonroids.gif.GifDrawable(spriteEssentialData.ctx.getResources(), R.drawable.oie_trans);
            gifFromResource.setBounds(getShieldRect());
            gifFromResource.setVisible(isShieldActivated, true);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void raiseShield() {
        isShieldActivated = true;
        isThereShieldLeft = false;
    }
    public void lowerShield() {
        isShieldActivated = false;
    }
    public Rect getShieldRect() {
        double currentShieldRatio = SHIELD_RATIO_MIN + (SHIELD_RATIO_MAX-SHIELD_RATIO_MIN) * Math.sin(shieldSineValue);
        Rect r = new Rect();
        r.set(
                location.getX() - (int)(currentShieldRatio*(size.getWidth()/2)) ,
                location.getY() - (int)(currentShieldRatio*(size.getHeight()/2)) ,
                location.getX() + (int)(currentShieldRatio*(size.getWidth()/2)) ,
                location.getY() + (int)(currentShieldRatio*(size.getHeight()/2))
        );

        return r;
    }

    public boolean isThereShieldLeft() {return isThereShieldLeft;}
//    public void setThereShieldLeft(boolean thereShieldLeft) {isThereShieldLeft = thereShieldLeft;}
    public boolean isShieldActivated() {return isShieldActivated;}
//    public void setShieldActivated(boolean shieldActivated) {isShieldActivated = shieldActivated;}

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
        if(isShieldActivated) {
            gifFromResource.setBounds(getShieldRect());
            shieldSineValue += DEGREES_PER_ITERATION;
        }

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
        GameActivity.shootSound.start();
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

    public boolean getHit(Enemy e) {

        if(isShieldActivated) {//still alive
            isShieldActivated = false;
            return false; //not even a scratch
        }

        //dead (rest in piss)
        flagForRemoval();
//        spriteEssentialData.gameSession.handleOnPlayerSpriteHit(this , e);
        return true;
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

        if(isShieldActivated) {
            gifFromResource.draw(canvas);
        }
    }
}
