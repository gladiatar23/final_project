package com.gameapps.phillip.singlethreadgame;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.TextView;
import com.gameapps.phillip.singlethreadgame.ready_sprites.SpriteCollisions;

import static com.gameapps.phillip.singlethreadgame.ready_sprites.SpriteCollisions.myScore;
//The main Class that Runs all the other classes.
public class GameActivity extends AppCompatActivity {

    private GameSession gameSession;
    private SpriteEssentialData activityData;

    private GameThread singleThreadRunner;
//On startup of application define the activities in which application will start. Produce graphical and logical classes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        gameSession = new GameSession();
        activityData = new SpriteEssentialData(this);

        singleThreadRunner = new GameThread(activityData.graphics , activityData.logics);
        singleThreadRunner.start();
    }
//Defines what happens onPause in the game. Currently the game annihilated
    @Override
    protected void onPause() {
        super.onPause();

        singleThreadRunner.terminateRun();
    }
//Defines what happens onResume of the game. Currently the game restart
    @Override
    protected void onResume() {
        super.onResume();

        singleThreadRunner = new GameThread(activityData.graphics , activityData.logics);
        singleThreadRunner.start();
    }
    //Defines what happens onDestroy in the game. Currently the game annihilated
    @Override
    protected void onDestroy() {
        super.onDestroy();

        singleThreadRunner.terminateRun();
    }

//   This method is automatically called when the user touches the screen
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        super.onTouchEvent(event);

        activityData.spriteCreator.handleMotionEvent(event);

        return false;
    }

//This Class takes care of the initial settings to start the game
    public class SpriteEssentialData {

        public Context ctx;
        public Point canvasSize;
        public Rect canvasRect;
        public GameGraphics graphics;
        public GameLogics logics;
        public SpriteCollisions spriteCollisions;
        public SpriteCreator spriteCreator;
//Function gets the Settings of the screen and sets the canvas
        public SpriteEssentialData(Context ctx) {
            GameSession.currentLevel= GameSession.Level.values()[0];


            this.ctx = ctx;
            Display display = getWindowManager().getDefaultDisplay();
            canvasSize = new Point();
            display.getSize(canvasSize);
            reSetCanvasSize(canvasSize.x , canvasSize.y);

            graphics = (GameGraphics)findViewById(R.id.gameGraphics);
//            canvasSize = new Point(graphics.getMeasuredWidth() , graphics.getMeasuredHeight());
            logics = new GameLogics(this);
            //Creates a new Test for sprites Collisions
            spriteCollisions = new SpriteCollisions(this);
            //Creates a new surface for creating sprites
            spriteCreator = new SpriteCreator(this);
        }
//Restores the canvas location
        public void reSetCanvasSize(int width , int height) {
            this.canvasSize.x = width;
            this.canvasSize.y = height;

            this.canvasRect = new Rect(0 , 0 , this.canvasSize.x , this.canvasSize.y);




        }
    }
}
