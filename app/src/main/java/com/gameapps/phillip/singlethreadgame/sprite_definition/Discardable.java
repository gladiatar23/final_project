package com.gameapps.phillip.singlethreadgame.sprite_definition;

/**
 * Created by Phillip on 1/6/2017.
 */
//The class raises a flag on the sprites thet get out of screen for remov them from the list
public interface Discardable {

    public void setFlagIfOutsideScreen();

    public void flagForRemoval();
    public boolean isFlaggedForRemoval();


}
