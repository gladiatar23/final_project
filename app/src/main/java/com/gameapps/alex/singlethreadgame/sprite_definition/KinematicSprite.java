package com.gameapps.alex.singlethreadgame.sprite_definition;

import com.gameapps.alex.singlethreadgame.GameActivity;

/**
 * Created by Phillip on 1/7/2017.
 */

public abstract class KinematicSprite extends Sprite {

    //TODO - include velocity and acceleration

    public KinematicSprite(GameActivity.SpriteEssentialData spriteEssentialData, int centerX, int centerY, int width, int height) {
        super(spriteEssentialData, centerX, centerY, width, height);
    }


}
