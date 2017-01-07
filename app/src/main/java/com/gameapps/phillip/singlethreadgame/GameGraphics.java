package com.gameapps.phillip.singlethreadgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gameapps.phillip.singlethreadgame.sprite_definition.ListOrganizer;
import com.gameapps.phillip.singlethreadgame.sprite_definition.ListOrganizerInterface;
import com.gameapps.phillip.singlethreadgame.sprite_definition.VisualElement;

/**
 * Created by Phillip on 1/4/2017.
 */

public class GameGraphics extends SurfaceView
        implements SurfaceHolder.Callback , ListOrganizerInterface<VisualElement> {

    private boolean isReadyToDraw;
    private SurfaceHolder sh;
    private ListOrganizer<VisualElement> organizer;

    public GameGraphics(Context context) {
        super(context);
        ctorStuff();
    }

    public GameGraphics(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctorStuff();
    }

    public GameGraphics(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ctorStuff();
    }

//    public GameGraphics(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        ctorStuff();
//    }



    private void ctorStuff() {
        sh = getHolder();
        sh.addCallback(this);

        isReadyToDraw = false;

        this.organizer = new ListOrganizer<VisualElement>();
    }


    //surface-holder callback

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isReadyToDraw = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        isReadyToDraw = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }




    public void redrawEverything() {
        if(!isReadyToDraw) return;

        Canvas canvas = sh.lockCanvas();

        if(canvas != null) {
            synchronized (sh) {
                canvas.drawColor(Color.BLACK);

                synchronized (organizer.getManagedListCopy()) {
                    for (VisualElement ve : organizer.getManagedListCopy()) {
                        ve.drawSelf(canvas);
                    }
                }

                removeDeadItems();

            }
            sh.unlockCanvasAndPost(canvas);
        }
    }


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

}
