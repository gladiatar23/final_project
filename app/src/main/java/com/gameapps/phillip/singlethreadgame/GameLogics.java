package com.gameapps.phillip.singlethreadgame;

import android.graphics.Point;
import android.graphics.Rect;

import com.gameapps.phillip.singlethreadgame.sprite_definition.ListOrganizer;
import com.gameapps.phillip.singlethreadgame.sprite_definition.ListOrganizerInterface;
import com.gameapps.phillip.singlethreadgame.sprite_definition.LogicalElement;
import com.gameapps.phillip.singlethreadgame.sprite_definition.Sprite;

/**
 * Created by Phillip on 1/6/2017.
 */
public class GameLogics implements ListOrganizerInterface<LogicalElement> {
    private static final int GARBAGE_COLLECTION_FREQUENCY = 20; //collecting once every this number of iterations

    private Point screenSize;
    private Rect screenrect;
    private ListOrganizer<LogicalElement> organizer;

    private int iterationCounter;

    public GameLogics(Point screenSize) {
        this.organizer = new ListOrganizer<LogicalElement>();

        this.screenSize = screenSize;
        this.screenrect = new Rect();
        this.screenrect.set(0 , 0 , screenSize.x , screenSize.y);

        this.iterationCounter = 0;
    }


    public void recalculateEverything() {
        synchronized (organizer.getManagedListCopy()) {
            for (LogicalElement le : organizer.getManagedListCopy()) {
                le.change();
            }
        }
        removeDeadItems();

        if(++(iterationCounter) % GARBAGE_COLLECTION_FREQUENCY == 0)
            collectGarbage();
    }

    private void collectGarbage() {
        synchronized (organizer.getManagedListCopy()) {
            for (LogicalElement le : organizer.getManagedListCopy()) {
                if(le instanceof Sprite) {  //if it's an object of this specific class
                    Sprite sprite = (Sprite) le;
                    if(sprite.isRemovedWhenOffScreen()) {
                        //if no intersection (with screen)
                        if(!MyMath.areRectanglesIntersecting(sprite.getAreaRect() , screenrect)) {
                            sprite.flagForRemoval();
                        }
                    }
                }
            }
        }
    }


    @Override
    public void addToManagedList(LogicalElement type) {organizer.addToManagedList(type);}

    @Override
    public void addToManagedList(int i, LogicalElement type) {organizer.addToManagedList(i , type);}

    @Override
    public void addToManagedListAfter(LogicalElement addMe, LogicalElement afterMe) {organizer.addToManagedListAfter(addMe , afterMe);}

    @Override
    public void removeFromManagedList(LogicalElement type) {organizer.removeFromManagedList(type);}

    @Override
    public void removeDeadItems() {organizer.removeDeadItems();}

}
