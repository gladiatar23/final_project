package com.gameapps.phillip.singlethreadgame;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import com.gameapps.phillip.singlethreadgame.GameGraphics;
import com.gameapps.phillip.singlethreadgame.GameLogics;
import com.gameapps.phillip.singlethreadgame.MyMath;
import com.gameapps.phillip.singlethreadgame.ready_sprites.DroppingStar;

/**
 * Created by Phillip on 1/4/2017.
 */

public class SpriteCreator {

    private Context ctx;
    private Point screenSize;

    private GameGraphics gg;
    private GameLogics gl;


    public SpriteCreator(Context ctx , Point screenSize , GameGraphics gg , GameLogics gl) {
        this.ctx = ctx;
        this.screenSize = screenSize;
        this.gg = gg;
        this.gl = gl;

        initializeSprites();
    }

    public void initializeSprites() {
        createRandomStar();
    }

    public void handleMotionEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            createStar(
                    (int) event.getRawX(),
                    (int) event.getRawY(),
                    (int) (screenSize.x / 20),
                    (int) (screenSize.y / 20)
            );
        }
    }



    private void createRandomStar() {
        createStar(
                MyMath.getRandomUpTo(screenSize.x),
                MyMath.getRandomUpTo(screenSize.y),
                (int)(screenSize.x/20),
                (int)(screenSize.y/20)
        );
    }
    private void createStar(int starCenterX , int starCenterY , int starWidth , int starHeight) {
        DroppingStar droppingStar = new DroppingStar(ctx ,
                starCenterX ,
                starCenterY ,
                starWidth ,
                starHeight);

        gg.addToManagedList(droppingStar);
        gl.addToManagedList(droppingStar);

        Log.i("added sprite" , "" + droppingStar);
    }

}
