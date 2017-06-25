package com.gameapps.alex.singlethreadgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gameapps.alex.singlethreadgame.activities.GameActivity;
import com.gameapps.alex.singlethreadgame.pool_manage.ListOrganizer;
import com.gameapps.alex.singlethreadgame.pool_manage.ListOrganizerInterface;
import com.gameapps.alex.singlethreadgame.sprite_definition.VisualElement;

/**
 * Created by Phillip on 1/4/2017.
 */

public class GameGraphics extends SurfaceView
        implements SurfaceHolder.Callback , ListOrganizerInterface<VisualElement> {

    private GameActivity.SpriteEssentialData spriteEssentialData;

    private boolean isReadyToDraw;
    private SurfaceHolder sh;
    private ListOrganizer<VisualElement> organizer;
    private Bitmap bitmap;

//constructor
    public GameGraphics(GameActivity.SpriteEssentialData spriteEssentialData) {
        super(spriteEssentialData.ctx);

        this.spriteEssentialData = spriteEssentialData;
        ctorStuff();
    }

    public GameGraphics(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctorStuff();
    }
//Dunno what it's there! No use project
    public GameGraphics(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ctorStuff();
    }

//    public GameGraphics(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        ctorStuff();
//    }


//Function gets the work surface and receive information about changes to the surface
    private void ctorStuff() {
        sh = getHolder();
        sh.addCallback(this);

        isReadyToDraw = false;

        this.organizer = new ListOrganizer<VisualElement>();
        Resources res = getResources();
        //get's pic of the background
        bitmap = BitmapFactory.decodeResource(res, GameSession.currentLevel.pathToBG);
    }


    //surface-holder callback
//Creates the worktop, get the screen size
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isReadyToDraw = true;

        if(spriteEssentialData != null) {
            spriteEssentialData.reSetCanvasSize(this.getWidth() , this.getHeight());
        }

        Log.i("surface created w sizes" , "" + getWidth() + " , " + getHeight());
    }
//Start the program ready to draw the game
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        isReadyToDraw = true;
        //a one-time thing
        bitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), false);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }



//Draw the screen. Re-paint the screen animation purposes
    public void redrawEverything() {
        if(!isReadyToDraw) return;

        Canvas canvas = sh.lockCanvas();


        if(canvas != null) {
            synchronized (sh) {
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(bitmap , 0 , 0 , null);

//Running a synchronization between graphic elements and logic on the surface
                synchronized (organizer.getManagedListCopy()) {
                    for (VisualElement ve : organizer.getManagedListCopy()) {
                        ve.drawSelf(canvas);
                    }
                }

                removeDeadItems();

            }
            sh.unlockCanvasAndPost(canvas);//Retrieve the current size of the surface
        }
    }

    //Calls Adds list management
    @Override
    public void addToManagedList(VisualElement type) {organizer.addToManagedList(type);}

    @Override
    public void addToManagedList(int i, VisualElement type) {organizer.addToManagedList(i , type);}

    @Override
    public void addToManagedListAfter(VisualElement addMe, VisualElement afterMe) {organizer.addToManagedListAfter(addMe , afterMe);}

    @Override
    public void removeFromManagedList(VisualElement type) {organizer.removeFromManagedList(type);}

    @Override
    public void removeDeadItems() {organizer.removeDeadItems();}

    @Override
    public void removeAllItems() {organizer.removeAllItems();}

}
