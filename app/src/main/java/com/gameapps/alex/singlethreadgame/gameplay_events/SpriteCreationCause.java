package com.gameapps.alex.singlethreadgame.gameplay_events;

import android.content.Context;
import android.graphics.Point;

import com.gameapps.alex.singlethreadgame.GameGraphics;
import com.gameapps.alex.singlethreadgame.GameLogics;

/**
 * Created by Phillip on 1/7/2017.
 */

public interface SpriteCreationCause {

    public void addSprites(Context ctx , Point screenSize , GameGraphics gg , GameLogics gl);

}
