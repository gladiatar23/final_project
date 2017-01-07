package com.gameapps.phillip.singlethreadgame.sprite_definition;

/**
 * Created by Phillip on 1/6/2017.
 */

public interface ListOrganizerInterface <T> {

    void addToManagedList(T type);

    void addToManagedList(int i , T type);

    void addToManagedListAfter(T addMe , T afterMe);

    void removeFromManagedList(T type);

    void removeDeadItems();
}
