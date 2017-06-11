package com.gameapps.alex.singlethreadgame.data_handle;

/**
 * Created by USER on 21/03/2017.
 */

public class LevelForTable {
    public static final int ID_INDEX = 0, LEVEL_NAME_INDEX = 1 , WON_INDEX = 2 , SCORE_INDEX = 3;

    private int id; //level's number
    private String levelName;
    private int isWon;
    private int bestScore;


    public LevelForTable(int id, String levelName, int isWon , int bestScore){
        this.id = id;
        this.levelName = levelName;
        this.isWon = isWon;
        this.bestScore = bestScore;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int isWon() {
        return isWon;
    }

    public void setWon(int won) {
        isWon = won;
    }


    @Override
    public String toString() {
        return "LevelForTable{" +
                "id=" + id +
                ", levelName='" + levelName + '\'' +
                ", isWon=" + isWon +
                ", bestScore=" + bestScore +
                '}';
    }
}
