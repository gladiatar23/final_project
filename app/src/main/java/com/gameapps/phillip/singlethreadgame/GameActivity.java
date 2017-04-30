package com.gameapps.phillip.singlethreadgame;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gameapps.phillip.singlethreadgame.data_handle.DBLevelHandler;
import com.gameapps.phillip.singlethreadgame.data_handle.LevelForTable;
import com.gameapps.phillip.singlethreadgame.ready_sprites.SpriteCollisions;

//The main Class that Runs all the other classes.
public class GameActivity extends AppCompatActivity {

    Button restart;
    TextView scoreText;

    private SpriteEssentialData activityData;

    private GameThread singleThreadRunner;
//On startup of application define the activities in which application will start. Produce graphical and logical classes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        restart=(Button)findViewById(R.id.restart);
        scoreText = (TextView)findViewById(R.id.scores);

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

//        singleThreadRunner = new GameThread(activityData.graphics , activityData.logics);
//        singleThreadRunner.start();
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

        public GameActivity ctx;
        public Point canvasSize;
        public Rect canvasRect;
        public GameGraphics graphics;
        public GameLogics logics;
        public SpriteCollisions spriteCollisions;
        public SpriteCreator spriteCreator;
        public GameSession gameSession;
//Function gets the Settings of the screen and sets the canvas
        public SpriteEssentialData(GameActivity ctx) {

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
            gameSession = new GameSession(ctx);
        }
//Restores the canvas location
        public void reSetCanvasSize(int width , int height) {
            this.canvasSize.x = width;
            this.canvasSize.y = height;

            this.canvasRect = new Rect(0 , 0 , this.canvasSize.x , this.canvasSize.y);




        }
    }

    public void setScore(int currentScore) {
        scoreText.setText("" + currentScore);
    }

    public void goBackToLevelSelect() {
//        startActivity(new Intent(this , LevelMenuActivity.class));
        HeroMenuActivity.isBackFromStage = true;
        finish();
        Log.i("Finish" , "Finish1 "+HeroMenuActivity.isBackFromStage );
    }

    public void winLevel() {
//        SaveLoad.createAndSaveFile(this ,  getResources().getString(R.string.player_sav_name) , "");
        DBLevelHandler dblh = DBLevelHandler.getInstance(this);

        String levelName = "testing";//GameSession.currentLevel.getName();
        int isWon = 1;
        int score = Integer.valueOf(scoreText.getText().toString());
        LevelForTable lt = new LevelForTable((int)dblh.getLevelTableSize() ,  //id
                levelName, //levelname
                isWon, //won
                score  //score
        );

        //TODO - check if already won once, and if actually new high score

        //add won level to DB
        dblh.addLevel(lt);


        goBackToLevelSelect();
        Log.i("Saved" , "Saved!!");
    }

    public void restartGame(View v) {
//        singleThreadRunner.terminateRun();
//        activityData.logics.removeAllItems();
//        activityData.graphics.removeAllItems();
//
//        setScore(0);
//
//        activityData = new SpriteEssentialData(this);
//        singleThreadRunner = new GameThread(activityData.graphics , activityData.logics);
//        singleThreadRunner.start();

        recreate();

    }

    public void killThread() {
        singleThreadRunner.terminateRun();
    }
}
