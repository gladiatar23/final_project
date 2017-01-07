package com.gameapps.phillip.singlethreadgame.ready_sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Size;

import com.gameapps.phillip.singlethreadgame.MyMath;
import com.gameapps.phillip.singlethreadgame.R;
import com.gameapps.phillip.singlethreadgame.sprite_definition.Location;
import com.gameapps.phillip.singlethreadgame.sprite_definition.Sprite;

/**
 * Created by Phillip on 1/4/2017.
 */

public class DroppingStar extends Sprite {
    private static final double CHANCE_FOR_SPECIAL_STAR = 0.06;
    private static final double ROTATION_SPEED = 0.15;
    private static final int ACCELERATION = 1;

    private int dropSpeed;

    public DroppingStar(Context ctx, int centerX, int centerY, int width, int height) {
        isRemovedWhenOffScreen = true;
        isRemoved = false;

        dropSpeed = 0;

        int squareSize = Math.min(width , height);

        rotation = 0;
        location = new Location(centerX, centerY);
        size = new Size(squareSize , squareSize);

        if(MyMath.doRandomChance(CHANCE_FOR_SPECIAL_STAR))
            bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.special_star);
        else
            bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.regular_star);

        bitmap = Bitmap.createScaledBitmap(bitmap , size.getWidth() , size.getHeight() , false);
    }

    @Override
    public void change() {
        dropSpeed += ACCELERATION;

        rotation += Math.PI * ROTATION_SPEED;

        location.setY(location.getY() + dropSpeed);

    }

    @Override
    public void drawSelf(Canvas canvas) {
        canvas.save();
        canvas.rotate(rotation , location.getX(), location.getY()); //rotate around own center
//        canvas.drawBitmap(bitmap, location.getX() - size.getWidth()/2, location.getY() - size.getHeight()/2, null);

        canvas.drawBitmap(bitmap, location.getX() - size.getWidth()/2, location.getY() - size.getHeight()/2, null);

        canvas.restore();   //undo rotation
    }


    @Override
    public Rect getAreaRect() {
        Rect r = new Rect();
        r.set(
                location.getX() - size.getWidth()/2 ,
                location.getY() - size.getHeight()/2 ,
                location.getX() + size.getWidth()/2 ,
                location.getY() + size.getHeight()/2
        );

        return r;
    }


    @Override
    public String toString() {
        String str = "";
        str += "Location: " + location.getX() + " , " + location.getY();
        str += "\nSize: " + size.getWidth() + " , " + size.getHeight();

        return str;
    }
}
