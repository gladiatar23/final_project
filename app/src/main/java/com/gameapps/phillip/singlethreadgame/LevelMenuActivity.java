package com.gameapps.phillip.singlethreadgame;

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

public class LevelMenuActivity extends AppCompatActivity implements View.OnClickListener{

    private static final double buttonRelativeToWidth = 0.3;
    private static final double buttonRelativeToHeight = 0.6;

    private int buttonWidth, buttonHeight;
    LinearLayout levelsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_menu);

        Display display = getWindowManager().getDefaultDisplay();
        Point canvasSize = new Point();
        display.getSize(canvasSize);
        buttonHeight = (int)(canvasSize.x * buttonRelativeToWidth);
        buttonWidth = (int)(canvasSize.y * buttonRelativeToHeight);

        levelsLayout=(LinearLayout)findViewById(R.id.levelSelection);

        int numOfLevels= GameSession.Level.values().length;
        for (int i=0 ; i<numOfLevels ; i++){
            ImageButton bLevel =new ImageButton(this);
            bLevel.setOnClickListener(this);
            levelsLayout.setGravity(Gravity.CENTER_VERTICAL);

            Drawable d = getResources().getDrawable(GameSession.Level.values()[i].pathToBG);
            Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
            bitmap = Bitmap.createScaledBitmap(bitmap , buttonWidth , buttonHeight , false);
            bLevel.setImageBitmap(bitmap);
            levelsLayout.addView(bLevel);
        }


    }

    @Override
    public void onClick(View v) {
        int indexOfPressedButton = levelsLayout.indexOfChild(v);
        GameSession.currentLevel= GameSession.Level.values()[indexOfPressedButton];
        startActivity(new Intent(this, GameActivity.class));

        Log.i("level button" , "index is " + indexOfPressedButton);
    }
}
