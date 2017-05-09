package com.gameapps.phillip.singlethreadgame.sprite_definition;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Size;

import com.gameapps.phillip.singlethreadgame.GameActivity;
import com.gameapps.phillip.singlethreadgame.MyMath;

/**
 * Created by Phillip on 1/6/2017.
 */
//An abstract class is role to implement : the logic of the element, the element painting and kill the element
public abstract class Sprite implements LogicalElement , VisualElement , Discardable{
    protected static final int MINIMAL_INTERSECTIONS_SIZE = 30;
    protected static final int MINIMAL_MOVE_SPEED = 30;

    protected boolean isRemovedWhenOffScreen;
    protected boolean isRemoved;    //sprite death

    protected GameActivity.SpriteEssentialData spriteEssentialData;

    protected float rotation;
    protected Location location;  //center location
    protected Size size;//size pic
    protected Bitmap bitmap;
//The function is responsible to provide data essential sprite of the initial creation.Its height, its location and the it's existence.
    public Sprite(GameActivity.SpriteEssentialData spriteEssentialData , int centerX, int centerY, int width, int height) {
        this.isRemoved = false;
        this.location = new Location(centerX , centerY);
        this.size = new Size(width , height);

        this.spriteEssentialData = spriteEssentialData;
    }
//The function is responsible for drawing the element on canvas area
    protected void setImage(int drawableId) {
        bitmap = BitmapFactory.decodeResource(spriteEssentialData.ctx.getResources(), drawableId);

        bitmap = Bitmap.createScaledBitmap(bitmap , size.getWidth() , size.getHeight() , false);
    }
    protected void setImageAndSizes(int drawableId , double screenHeightRation) {
        bitmap = BitmapFactory.decodeResource(spriteEssentialData.ctx.getResources(), drawableId);

        int height = (int) (spriteEssentialData.canvasSize.y * screenHeightRation);    //hight of sprite img
        double ratio = bitmap.getHeight() / height;
        this.size = new Size((int)(bitmap.getWidth()/ ratio) , height);

        bitmap = Bitmap.createScaledBitmap(bitmap , size.getWidth() , size.getHeight() , false);
    }
//The function returns the size of  occupies the area
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
    //The function returns the size of a rectangular element
    public Rect getMinimalInsideRect() {
        Rect r = new Rect();
        r.set(
                location.getX() - MINIMAL_INTERSECTIONS_SIZE ,
                location.getY() - MINIMAL_INTERSECTIONS_SIZE ,
                location.getX() + MINIMAL_INTERSECTIONS_SIZE ,
                location.getY() + MINIMAL_INTERSECTIONS_SIZE
        );

        return r;
    }

    public boolean isRemovedWhenOffScreen() {return isRemovedWhenOffScreen;}


    ///////////////////////////////Drawable
// / Is the area of the rectangle of the screen does not intersects with the rectangle area of the element
    //If so, the function will raise a flag to  delete sprite from the list of draw
    @Override
    public void setFlagIfOutsideScreen() {
        if(!MyMath.areRectanglesIntersecting(getAreaRect() , spriteEssentialData.canvasRect))
            flagForRemoval();
    }
    @Override
    public void flagForRemoval() {
        isRemoved = true;
    }
    @Override
    public boolean isFlaggedForRemoval() {return isRemoved;}



    ///////////////////////////////Logical-Visual

    @Override
    public void drawSelf(Canvas canvas) {
        canvas.save();
        canvas.rotate(rotation , location.getX(), location.getY()); //rotate around own center
//        canvas.drawBitmap(bitmap, location.getX() - size.getWidth()/2, location.getY() - size.getHeight()/2, null);

        canvas.drawBitmap(bitmap, location.getX() - size.getWidth()/2, location.getY() - size.getHeight()/2, null);

        canvas.restore();   //undo rotation
    }



    @Override
    public String toString() {
        String str = "";
        str += "Location: " + location.getX() + " , " + location.getY();
        str += "\nSize: " + size.getWidth() + " , " + size.getHeight();

        return str;
    }
}
