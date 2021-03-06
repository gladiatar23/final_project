package com.gameapps.alex.singlethreadgame.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.gameapps.alex.singlethreadgame.GameSession;
import com.gameapps.alex.singlethreadgame.R;
import com.gameapps.alex.singlethreadgame.data_handle.DBLevelHandler;
import com.gameapps.alex.singlethreadgame.data_handle.LevelForTable;
import com.gameapps.alex.singlethreadgame.sprite_definition.Enums;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LevelMenuActivity extends AppCompatActivity implements View.OnClickListener{

    private static final double buttonRelativeToWidth = 0.3;
    private static final double buttonRelativeToHeight = 0.6;

    private int buttonWidth, buttonHeight;
    LinearLayout levelsLayout;
    List<ImageButton> levelButtons;
    //Button backMain;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
//Request a screen size and set the size of the level selection buttons
        Display display = getWindowManager().getDefaultDisplay();
        Point canvasSize = new Point();
        display.getSize(canvasSize);
        buttonHeight = (int)(canvasSize.x * buttonRelativeToWidth);
        buttonWidth = (int)(canvasSize.y * buttonRelativeToHeight);

        levelsLayout=(LinearLayout)findViewById(R.id.levelSelection);
//Accepting the amount of levels available and saving them into an array
        int numOfLevels= GameSession.Level.values().length;
        levelButtons = new ArrayList<>();
        levelsLayout.removeAllViews();

        System.gc();

//Runs in the loop, creates the level buttons, puts a relevant picture for each level by id,
// puts the clicks listener on buttons  and add them to view
        for (int i=0 ; i<numOfLevels ; i++){
            ImageButton bLevel =new ImageButton(this);
            bLevel.setOnClickListener(this);
            levelsLayout.setGravity(Gravity.CENTER_VERTICAL);

            Drawable d = getResources().getDrawable(GameSession.Level.values()[i].pathToBG);
            Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
            bitmap = Bitmap.createScaledBitmap(bitmap , buttonWidth , buttonHeight , false);
            bLevel.setImageBitmap(bitmap);

            levelsLayout.addView(bLevel);
            levelButtons.add(bLevel);

            d = null;
            bitmap = null;
        }

        placeAllLevelsInDB();

        lockLevels();


    }

//    @Override
//    protected void onResume() {
//        super.onPostResume();
//
//        lockLevels();
//    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

        lockLevels();
    }




    ///---------------------------------Clicks---------------------------------///
    //////////////////////////////////////////////////////////////////////////////
//With a click of the button get the id of the level,
// positioning the id to GameSession.currentLevel and switching to HeroMenuActivity
    @Override
    public void onClick(View v) {
        int indexOfPressedButton = levelsLayout.indexOfChild(v);
        GameSession.currentLevel= GameSession.Level.values()[indexOfPressedButton];

        HeroMenuActivity.isBackFromStage = false;
        startActivity(new Intent(this, HeroMenuActivity.class));

        //Log.i("level button" , "index is " + indexOfPressedButton);
    }

//Ending current activity
    public void goMain(View v){
        finish();
    }

//    public void goToFB(View view) {
//        startActivity(new Intent(this , FacebookActivity.class));
//    }

//    public void printDBToLog(View v) {
//        DBLevelHandler db = DBLevelHandler.getInstance(this);
//
//        List<LevelForTable> levelForTables = db.getAllLevels();
//        for(LevelForTable lev : levelForTables) {
//            Log.i("level: " , "" + lev.toString());
//        }
//    }
//public void eraseDB(View v) {
//    DBLevelHandler db = DBLevelHandler.getInstance(this);
//
//    db.deleteTable();
//}



    ///------------------------activity layout handling-------------------------///
    //////////////////////////////////////////////////////////////////////////////


    private void placeAllLevelsInDB() {
        DBLevelHandler db = DBLevelHandler.getInstance(this);

        for(GameSession.Level lv : GameSession.Level.values()) {
            LevelForTable lft = new LevelForTable(lv.id , lv.levelName , 0 , 0);
            db.addLevel(lft , false);
        }
    }
//The function checks which stages were been win and opens the following level for playing
// as well the heroes with which you can play.
// For the first time the opening of the first hero and the first stage.
    private void lockLevels() {
        DBLevelHandler db = DBLevelHandler.getInstance(this);

        GameSession.availableHeroes = new HashSet<>();
        GameSession.availableHeroes.add(GameSession.Human.DEFAULT);
//The difference between the stages and  heroes that are available to play is treated graphically and unable to click them
        for(int i = 0 ; i < levelButtons.size()-1 ; i++) {
            if(db.isWon(i)) {
                levelButtons.get(i+1).setEnabled(true);
                levelButtons.get(i+1).setImageAlpha(0xFF);
                GameSession.availableHeroes.add(GameSession.Level.values()[i].unlocledPlayable);
            }
            else {
                levelButtons.get(i+1).setEnabled(false);
                levelButtons.get(i+1).setImageAlpha(50);
            }
        }
    }
    public void onBackPressed() {
        finish();
    }



}
