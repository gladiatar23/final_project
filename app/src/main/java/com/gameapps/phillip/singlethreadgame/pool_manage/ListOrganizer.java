package com.gameapps.phillip.singlethreadgame.pool_manage;

import android.util.Log;

import com.gameapps.phillip.singlethreadgame.sprite_definition.Discardable;
import com.gameapps.phillip.singlethreadgame.sprite_definition.Sprite;
import com.gameapps.phillip.singlethreadgame.sprite_definition.VisualElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phillip on 1/6/2017.
 */

public class ListOrganizer<T extends Discardable> implements ListOrganizerInterface <T> {

    //T is the list's type
    private List<T> managedList;

    private List<T> managedListCopy;
    private boolean isCopyOutdated;

    public <T> ListOrganizer() {
        managedList = new ArrayList<>();  //may want to turn it into linked-list

        isCopyOutdated = true;
    }

    public List<T> getManagedListCopy() {
        if(isCopyOutdated) {
            isCopyOutdated = false;
            return managedListCopy = new ArrayList<>(managedList);
        }
        else
            return managedListCopy;
    }

    @Override
    public void addToManagedList(T type) {
        isCopyOutdated = true;
        synchronized (managedList) {
            managedList.add(type);
        }
        subscribeSelf(type);
    }

    @Override
    public void addToManagedList(int i , T type) {
        isCopyOutdated = true;
        synchronized (managedList) {
            managedList.add(i , type);
        }
        subscribeSelf(type);
    }
//Adds a new sprite to a managed list
    @Override
    public void addToManagedListAfter(T addMe , T afterMe) {
        isCopyOutdated = true;
        int after = managedList.indexOf(afterMe) + 1;
        addToManagedList(after , addMe);
        subscribeSelf(addMe);
    }
    //removs a dead sprite to a managed list
    @Override
    public void removeFromManagedList(T type) {
        isCopyOutdated = true;
        synchronized (managedList) {
            managedList.remove(type);
            Log.i("list removed object" , "" + this);
        }
        unsubscribeSelf(type);
    }
////function run on the list of sprites and remove that not in the rectangle of the screen
    @Override
    public void removeDeadItems() {
//        synchronized (managedList) {
////            Iterator<T> iterator = managedList.iterator();
////            while (iterator.hasNext()) {
////                T t = iterator.next();
////                if (t.isFlaggedForRemoval()) {
////                    removeFromManagedList(t);
////                }
////            }
// //The function checks the list of elements each have a flag is annihilation. If so delete them from the list
//            for (int i = 0 ; i < managedList.size() ; i++) {
//                if(managedList.get(i).isFlaggedForRemoval()){
//                    removeFromManagedList(managedList.get(i));
//                    i--;
//                }
//            }
//
//        }
    }
//function run on the list of sprites and remove all of them
    @Override
    public void removeAllItems() {
        while(managedList.size() != 0) {
            managedList.remove(0);
        }

    }

    private void subscribeSelf(T type) {
        if(type instanceof Sprite) {
            ((Sprite)type).addToListManagersSubscribed(this);
        }
    }
    private void unsubscribeSelf(T type) {
        if(type instanceof Sprite) {
            ((Sprite)type).ridFromListManagersSubscribed(this);
        }
    }


    //syncing



}
