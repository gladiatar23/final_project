package com.gameapps.alex.singlethreadgame;

import com.gameapps.alex.singlethreadgame.pool_manage.ListOrganizer;
import com.gameapps.alex.singlethreadgame.pool_manage.ListOrganizerInterface;
import com.gameapps.alex.singlethreadgame.sprite_definition.LogicalElement;

/**
 * Created by Phillip on 1/6/2017.
 */
public class GameLogics implements ListOrganizerInterface<LogicalElement> {
    private static final int GARBAGE_COLLECTION_FREQUENCY = 20; //collecting once every this number of iterations

    private GameActivity.SpriteEssentialData spriteEssentialData;
    private ListOrganizer<LogicalElement> organizer;

    private int iterationCounter;
    //Creates a new management list and zero counts interactions
    public GameLogics(GameActivity.SpriteEssentialData spriteEssentialData) {
        this.spriteEssentialData = spriteEssentialData;
        this.organizer = new ListOrganizer<LogicalElement>();

        this.iterationCounter = 0;
    }

//Recalculates all the sprites in maneged list
    public void recalculateEverything() {
        synchronized (organizer.getManagedListCopy()) {
            for (LogicalElement le : organizer.getManagedListCopy()) {
                le.change();
            }
        }
        removeDeadItems();
       // If interacting divided by modulo garbage cleaning rate, is zero so call private call checkDriftersOutsideScreen
        if(++(iterationCounter) % GARBAGE_COLLECTION_FREQUENCY == 0)
            checkDriftersOutsideScreen();
    }
//Junk Cleaner goes through the list calls for review Did the sprites beyond the scope of screen
    private void checkDriftersOutsideScreen() {
        synchronized (organizer.getManagedListCopy()) {
            for (LogicalElement le : organizer.getManagedListCopy()) {
                le.setFlagIfOutsideScreen();
            }
        }
    }

//Calls Adds list management
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

    @Override
    public void removeAllItems() {organizer.removeAllItems();}
}
