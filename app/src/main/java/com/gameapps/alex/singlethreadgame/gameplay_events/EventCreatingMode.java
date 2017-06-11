package com.gameapps.alex.singlethreadgame.gameplay_events;

/**
 * Created by Phillip on 1/7/2017.
 */

public class EventCreatingMode {

    public Mode currentMode;
    private EventCreatingMode prevModeHolder;

    //first ever mode
    public EventCreatingMode() {
        this.currentMode = Mode.TAP_SCREEN;
        this.prevModeHolder = null;
    }

    public EventCreatingMode(EventCreatingMode modeLeftBehind , Mode mode) {
        this.currentMode = mode;
        this.prevModeHolder = modeLeftBehind;
    }

    public EventCreatingMode getPreviousModeHolder() {return prevModeHolder;}


    public enum Mode {
        TAP_SCREEN

    }

}
