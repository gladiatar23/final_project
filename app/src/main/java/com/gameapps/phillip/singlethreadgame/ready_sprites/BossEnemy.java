package com.gameapps.phillip.singlethreadgame.ready_sprites;

import com.gameapps.phillip.singlethreadgame.GameActivity;
import com.gameapps.phillip.singlethreadgame.GameSession;
import com.gameapps.phillip.singlethreadgame.sprite_definition.Location;

/**
 * Created by USER on 09/05/2017.
 */

public class BossEnemy extends Enemy {

    private static final double RATIO_TO_SCREEN_HEIGHT = (double)2/3;
    private static final int ENTERING_SPEED = 4;
    boolean isStillEntering;

    GameSession.Human humanEnum;

    public BossEnemy(GameSession.Human humanEnum, GameActivity.SpriteEssentialData spriteEssentialData) {
        super(humanEnum, spriteEssentialData, 0 , 0 , 0 , 0);
        this.humanEnum = humanEnum;

        setImageAndSizes(humanEnum.pathToPicBoss , RATIO_TO_SCREEN_HEIGHT);

        this.location = new Location(spriteEssentialData.canvasRect.right + size.getWidth() / 2 , spriteEssentialData.canvasRect.height() / 2);

        isStillEntering = true;

    }

    private void phaseEnter() {
        location.setX(location.getX() - ENTERING_SPEED);

        if(location.getX()  + size.getWidth() / 2 < spriteEssentialData.canvasRect.right)
            isStillEntering = false;
    }

    private void phaseFight() {
        //TODO
    }

    @Override
    public void change() {
        if(isStillEntering) {
            phaseEnter();
        }
        else {
            phaseFight();
        }
    }

}
