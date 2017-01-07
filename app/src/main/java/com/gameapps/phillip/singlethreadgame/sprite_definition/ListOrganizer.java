package com.gameapps.phillip.singlethreadgame.sprite_definition;

import android.util.Log;

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
    }

    @Override
    public void addToManagedList(int i , T type) {
        isCopyOutdated = true;
        synchronized (managedList) {
            managedList.add(i , type);
        }
    }

    @Override
    public void addToManagedListAfter(T addMe , T afterMe) {
        isCopyOutdated = true;
        int after = managedList.indexOf(afterMe) + 1;
        addToManagedList(after , addMe);
    }

    @Override
    public void removeFromManagedList(T type) {
        isCopyOutdated = true;
        synchronized (managedList) {
            managedList.remove(type);
            Log.i("list removed object" , "" + this);
        }
    }

    @Override
    public void removeDeadItems() {
        synchronized (managedList) {
//            Iterator<T> iterator = managedList.iterator();
//            while (iterator.hasNext()) {
//                T t = iterator.next();
//                if (t.isFlaggedForRemoval()) {
//                    removeFromManagedList(t);
//                }
//            }
            for (int i = 0 ; i < managedList.size() ; i++) {
                if(managedList.get(i).isFlaggedForRemoval()){
                    removeFromManagedList(managedList.get(i));
                    i--;
                }
            }

        }
    }


    //syncing



}
