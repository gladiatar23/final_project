package com.gameapps.alex.singlethreadgame.sprite_definition;

/**
 * Created by Alex on 1/4/2017.
 */

public class Location {

    private int x , y;

    public Location(int initX , int initY) {
        this.x = initX;
        this.y = initY;
    }

    public int getX() {return x;}
    public int getY() {return y;}

    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}

}