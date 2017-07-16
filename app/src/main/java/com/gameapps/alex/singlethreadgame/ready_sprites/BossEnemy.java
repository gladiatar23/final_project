package com.gameapps.alex.singlethreadgame.ready_sprites;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.util.Size;

import com.gameapps.alex.singlethreadgame.R;
import com.gameapps.alex.singlethreadgame.activities.GameActivity;
import com.gameapps.alex.singlethreadgame.GameSession;
import com.gameapps.alex.singlethreadgame.sprite_definition.Location;

/**
 * Created by USER on 09/05/2017.
 */

public class BossEnemy extends Enemy {

    private final long ITERATIONS_UNTIL_SHOOT = 30;
    private final long ITERATIONS_UNTIL_SHOOT_DEVIATION = 4;

    private final int THROW_SPEED = 20;
    private final int THROW_SPEED_DEVIATION = 5;

    private final double MIN_THROW_ANGLE = Math.PI *3/4;
    private final double MAX_THROW_ANGLE = Math.PI *3/2;

    private final double MAX_WIDTH_TO_SCREEN = 0.25;


    private static final double RATIO_TO_SCREEN_HEIGHT = (double)2/3;
    private static final int ENTERING_SPEED = 4;

    private long iterationsLived;
    private long timeOfLastThrow;
    private long nextShootWait;


    GameSession.Human humanEnum;


    public BossEnemy(GameSession.Human humanEnum, GameActivity.SpriteEssentialData spriteEssentialData) {
        super(humanEnum, spriteEssentialData, 0 , 0 , 0 , 0);
        this.humanEnum = humanEnum;

        setImageAndSizes(humanEnum.pathToPicBoss , RATIO_TO_SCREEN_HEIGHT);

        this.location = new Location(spriteEssentialData.canvasRect.right + size.getWidth() / 2 , spriteEssentialData.canvasRect.height() / 2);

        hitPoints = humanEnum.initialBossHP;

        iterationsLived = 0;
        timeOfLastThrow = 0;
        nextShootWait = 0;

        spriteEssentialData.spriteCreator.setBoss(this);
    }




    private void phaseEnter() {
        location.setX(location.getX() - ENTERING_SPEED);

        if(spriteEssentialData.gameSession.stagePhase == GameSession.StagePhase.FINAL_BOSS_ENTERING) {
            if (location.getX() + size.getWidth() / 2 < spriteEssentialData.canvasRect.right) {
                spriteEssentialData.gameSession.isDoneWithPhase = true;
            }
        }
    }

    private void phaseFight() {
        iterationsLived++;

        //conditional throw
        if(iterationsLived > timeOfLastThrow + nextShootWait) {
            throwMinion();

            //update time of last throw
            timeOfLastThrow = iterationsLived;

            //randomize waiting time till next
            nextShootWait = getRandomWaitTillThrow();
        }
    }
    private void throwMinion() {
        //throw a new minion
        spriteEssentialData.spriteCreator.createThrownEnemy();


    }
    private long getRandomWaitTillThrow() {
        long deviation = (long)(ITERATIONS_UNTIL_SHOOT_DEVIATION * Math.random());
        deviation *= oneOrMinusOne();
        return (long)(ITERATIONS_UNTIL_SHOOT + deviation);
    }

    @Override
    public void change() {
        if(spriteEssentialData.gameSession.stagePhase == GameSession.StagePhase.FINAL_BOSS_ENTERING) {
            GameActivity.phaseEnterSound.start();
            phaseEnter();
        }
        else if(spriteEssentialData.gameSession.stagePhase == GameSession.StagePhase.FINAL_BOSS_FIGHT) {
            phaseFight();
        }
    }

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

    public double getAngleOfThrow() {
//        return 2*Math.PI * Math.random();

        double deviance = (MAX_THROW_ANGLE - MIN_THROW_ANGLE) / 2;
        double meanAngle = MIN_THROW_ANGLE + deviance;

//        return Math.PI/2;
        return meanAngle + Math.random() * deviance * oneOrMinusOne();
    }

    public int getThrowSpeed() {
        int deviation = (int) (THROW_SPEED_DEVIATION * Math.random());
        deviation *= oneOrMinusOne();
        return THROW_SPEED + deviation;
    }


    private int oneOrMinusOne () {
        return (Math.random() < 0.5) ? 1 : -1;
    }

    @Override
    protected void setImageAndSizes(Bitmap drawableBitmap, double screenHeightRation) {
        super.setImageAndSizes(drawableBitmap, screenHeightRation);

        if(size.getWidth() > spriteEssentialData.canvasSize.x * MAX_WIDTH_TO_SCREEN) {
            //constraint into smaller version
            int width = (int) (spriteEssentialData.canvasSize.x * MAX_WIDTH_TO_SCREEN);    //hight of sprite img
            double ratio = bitmap.getWidth() / width;
            this.size = new Size(width , (int)(bitmap.getHeight() / ratio));
            bitmap = Bitmap.createScaledBitmap(bitmap , size.getWidth() , size.getHeight() , false);
        }
    }

}
