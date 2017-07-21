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

import java.util.ArrayList;
import java.util.List;

public class HeroMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private static final double buttonRelativeToWidth = 0.3;
    private static final double buttonRelativeToHeight = 0.6;

    public static boolean isBackFromStage;

    private int buttonWidth, buttonHeight;
    LinearLayout heroLayout;
    List<GameSession.Human> heroesAvailableList;
    List<ImageButton> heroButtons;
    Button backLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
//Request a screen size and set the size of the hero selection buttons
        Display display = getWindowManager().getDefaultDisplay();
        Point canvasSize = new Point();
        display.getSize(canvasSize);
        buttonHeight = (int)(canvasSize.x * buttonRelativeToWidth);
        buttonWidth = (int)(canvasSize.y * buttonRelativeToHeight);

        heroLayout=(LinearLayout)findViewById(R.id.heroSelection);

        //int numOfHeros= GameSession.Human.values().length;
        heroButtons = new ArrayList<>();
        heroLayout.removeAllViews();

//Checking of Heroes is available and saved in the list
////Runs in the loop, creates the hero buttons, puts a relevant picture for each hero by id,
// puts the clicks listener on buttons  and add them to view
        heroesAvailableList = new ArrayList<>(GameSession.availableHeroes);
        for(GameSession.Human hero : heroesAvailableList) {
            ImageButton bLevel =new ImageButton(this);
            bLevel.setOnClickListener(this);
            heroLayout.setGravity(Gravity.CENTER_VERTICAL);
            Drawable d = getResources().getDrawable(hero.pathToPicHero);
            Bitmap heroPic = ((BitmapDrawable)d).getBitmap();
            heroPic = Bitmap.createScaledBitmap(heroPic , buttonWidth , buttonHeight , false);
            bLevel.setImageBitmap(heroPic);
            heroLayout.addView(bLevel);
            heroButtons.add(bLevel);

        }

        backLevel = (Button)findViewById(R.id.backToLevels);
        backLevel.setOnClickListener(this);

    }
//Checking of pressing a button according to ID. If the ID is not equal to backToLevels then check which stage is selected.
// If it is, then the running of the activity is completed
    @Override
    public void onClick(View v) {
        if(v.getId()!=R.id.backToLevels){
            int indexOfPressedButton = heroLayout.indexOfChild(v);
            GameSession.initializeStatics(false);
            GameSession.currentHero = heroesAvailableList.get(indexOfPressedButton);
            startActivity(new Intent(this, GameActivity.class));
            Log.i("Hero button", "index is " + indexOfPressedButton);

        }
        else {
            finish();
        }
    }
    public void leaveBackToStageSelect() {
      //  Log.i("Finish" , "Finish2 " );
        finish();

    }




    @Override
    protected void onPostResume() {
        super.onPostResume();

        if(isBackFromStage) {
           // Log.i("Finish" , "Finish3 " );
            leaveBackToStageSelect();

        }


    }
    public void onBackPressed() {
     finish();
    }

}
