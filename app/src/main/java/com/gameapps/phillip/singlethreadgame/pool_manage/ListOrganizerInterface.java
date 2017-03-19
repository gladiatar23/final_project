package com.gameapps.phillip.singlethreadgame.pool_manage;

/**
 * Created by Phillip on 1/6/2017.
 */
//This class manages a list of elements (bullets, enemies and bosses) that are created during the game (dead or alive)
// adds and organizes them in a list T.
public interface ListOrganizerInterface <T> {

    void addToManagedList(T type);

    void addToManagedList(int i , T type);

    void addToManagedListAfter(T addMe , T afterMe);

    void removeFromManagedList(T type);

    void removeDeadItems();

    void removeAllItems();
}
