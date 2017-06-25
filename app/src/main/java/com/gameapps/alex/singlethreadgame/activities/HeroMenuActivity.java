package com.gameapps.alex.singlethreadgame.activities;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.gameapps.alex.singlethreadgame.GameSession;
import com.gameapps.alex.singlethreadgame.R;
import com.gameapps.alex.singlethreadgame.activities.GameActivity;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_menu);

        Display display = getWindowManager().getDefaultDisplay();
        Point canvasSize = new Point();
        display.getSize(canvasSize);
        buttonHeight = (int)(canvasSize.x * buttonRelativeToWidth);
        buttonWidth = (int)(canvasSize.y * buttonRelativeToHeight);

        heroLayout=(LinearLayout)findViewById(R.id.heroSelection);

        int numOfHeros= GameSession.Human.values().length;
        heroButtons = new ArrayList<>();
        heroLayout.removeAllViews();

        heroesAvailableList = new ArrayList<>(GameSession.availableHeroes);
        for(GameSession.Human hero : heroesAvailableList) {
            ImageButton bLevel =new ImageButton(this);
            bLevel.setOnClickListener(this);
            heroLayout.setGravity(Gravity.CENTER_VERTICAL);

            Drawable d = getResources().getDrawable(hero.pathToPicHero);
            Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
            bitmap = Bitmap.createScaledBitmap(bitmap , buttonWidth , buttonHeight , false);
            bLevel.setImageBitmap(bitmap);
            heroLayout.addView(bLevel);
            heroButtons.add(bLevel);

        }





    }

    @Override
    public void onClick(View v) {
        int indexOfPressedButton = heroLayout.indexOfChild(v);
        GameSession.currentHero= heroesAvailableList.get(indexOfPressedButton);
        startActivity(new Intent(this, GameActivity.class));

        Log.i("Hero button" , "index is " + indexOfPressedButton);

    }
    public void leaveBackToStageSelect() {
        Log.i("Finish" , "Finish2 " );
        finish();

    }




    @Override
    protected void onResume() {
        super.onPostResume();

        if(isBackFromStage) {
            Log.i("Finish" , "Finish3 " );
            leaveBackToStageSelect();

        }


    }

}
