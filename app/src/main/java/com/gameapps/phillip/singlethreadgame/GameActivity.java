package com.gameapps.phillip.singlethreadgame;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;

public class GameActivity extends AppCompatActivity {

    private Point screenSize;
    private GameGraphics graphics;
    private GameLogics gameLocgics;
    private SpriteCreator spriteCreator;

    GameThread singleThreadRunner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);

        graphics = (GameGraphics)findViewById(R.id.gameGraphics);
        gameLocgics = new GameLogics(screenSize);
        spriteCreator = new SpriteCreator(this , screenSize , graphics , gameLocgics);

        singleThreadRunner = new GameThread(graphics , gameLocgics);
        singleThreadRunner.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        singleThreadRunner.terminateRun();
    }

    @Override
    protected void onResume() {
        super.onResume();

        singleThreadRunner = new GameThread(graphics , gameLocgics);
        singleThreadRunner.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        singleThreadRunner.terminateRun();
    }


//    This method is automatically called when the user touches the screen
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        super.onTouchEvent(event);

        spriteCreator.handleMotionEvent(event);

        return false;
    }
}
