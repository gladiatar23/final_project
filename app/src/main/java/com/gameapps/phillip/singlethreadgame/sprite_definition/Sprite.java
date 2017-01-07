package com.gameapps.phillip.singlethreadgame.sprite_definition;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Size;

/**
 * Created by Phillip on 1/6/2017.
 */

public abstract class Sprite implements LogicalElement , VisualElement , Discardable{
    protected boolean isRemovedWhenOffScreen;
    protected boolean isRemoved;    //sprite death

    protected float rotation;
    protected Location location;  //center location
    protected Size size;
    protected Bitmap bitmap;

    public abstract Rect getAreaRect();

    public boolean isRemovedWhenOffScreen() {return isRemovedWhenOffScreen;}

    @Override
    public void flagForRemoval() {isRemoved = true;}
    @Override
    public boolean isFlaggedForRemoval() {return isRemoved;}

}
