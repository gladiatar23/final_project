package com.gameapps.alex.singlethreadgame.gameplay_events;

import android.content.Context;
import android.graphics.Point;

import com.gameapps.alex.singlethreadgame.GameGraphics;
import com.gameapps.alex.singlethreadgame.GameLogics;

/**
 * Created by Phillip on 1/7/2017.
 */

public class DroppingStarCreationCause implements SpriteCreationCause {


    @Override
    public void addSprites(Context ctx, Point screenSize, GameGraphics gg, GameLogics gl) {

    }


//    private void createRandomStar() {
//        createStar(
//                MyMath.getRandomUpTo(screenSize.x),
//                MyMath.getRandomUpTo(screenSize.y),
//                (int)(screenSize.x/20),
//                (int)(screenSize.y/20)
//        );
//    }
//    private void createStar(int starCenterX , int starCenterY , int starWidth , int starHeight) {
//        DroppingStar droppingStar = new DroppingStar(ctx ,
//                starCenterX ,
//                starCenterY ,
//                starWidth ,
//                starHeight);
//
//        gg.addToManagedList(droppingStar);
//        gl.addToManagedList(droppingStar);
//
//        Log.i("added sprite" , "" + droppingStar);
//    }


}
