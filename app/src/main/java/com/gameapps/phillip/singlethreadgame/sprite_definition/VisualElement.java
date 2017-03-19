package com.gameapps.phillip.singlethreadgame.sprite_definition;

import android.graphics.Canvas;

/**
 * Created by Phillip on 1/4/2017.
 */
//The Class gives an order to draw the sprite
public interface VisualElement extends Discardable {

    public void drawSelf(Canvas canvas);

}
