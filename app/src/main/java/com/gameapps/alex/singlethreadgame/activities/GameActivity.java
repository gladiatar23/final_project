package com.gameapps.alex.singlethreadgame.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gameapps.alex.singlethreadgame.GameGraphics;
import com.gameapps.alex.singlethreadgame.GameLogics;
import com.gameapps.alex.singlethreadgame.GameSession;
import com.gameapps.alex.singlethreadgame.GameThread;
import com.gameapps.alex.singlethreadgame.R;
import com.gameapps.alex.singlethreadgame.SpriteCreator;
import com.gameapps.alex.singlethreadgame.data_handle.DBLevelHandler;
import com.gameapps.alex.singlethreadgame.data_handle.LevelForTable;
import com.gameapps.alex.singlethreadgame.ready_sprites.SpriteCollisions;
import com.gameapps.alex.singlethreadgame.ready_sprites.WorldManager;

//The main Class that Runs all the other classes.
public class GameActivity extends AppCompatActivity implements View.OnClickListener {


    TextView scoreText;
    MediaPlayer shootSound;

    private boolean isPaused;


    ImageButton upButton , downButton;

    private SpriteEssentialData activityData;

    private GameThread singleThreadRunner;
//On startup of application define the activities in which application will start. Produce graphical and logical classes
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_main);
        shootSound = MediaPlayer.create(this,R.raw.xara);


        scoreText = (TextView)findViewById(R.id.scores);
        upButton = (ImageButton)findViewById(R.id.upButton);
        downButton = (ImageButton)findViewById(R.id.downButton);

        upButton.setOnClickListener(this);
        downButton.setOnClickListener(this);

        activityData = new SpriteEssentialData(this);
        singleThreadRunner = new GameThread(activityData.graphics , activityData.logics , activityData.worldManager, activityData.spriteCollisions , activityData.gameSession);
        singleThreadRunner.start();

        isPaused = false;
        ( (Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseResume();
            }
        });
    }
    public void pauseResume() {

        if(isPaused) {
            resumeThread();
        }
        else {
            pauseThread();
        }
    }

//Defines what happens onPause in the game. Currently the game annihilated
    @Override
    protected void onPause() {
        super.onPause();

        singleThreadRunner.pauseRunning();

    }

    //Defines what happens onPause in the game. Currently the game annihilated
    @Override
    protected void onStop() {
        super.onStop();

//        singleThreadRunner.terminateRunning();

        goBackToLevelSelect();

    }
//Defines what happens onResume of the game. Currently the game restart
    @Override
    protected void onResume() {
        super.onResume();
        singleThreadRunner.resumeRunning();

//        singleThreadRunner = new GameThread(activityData.graphics , activityData.logics);
//        singleThreadRunner.start();
    }
    //Defines what happens onDestroy in the game. Currently the game annihilated
    @Override
    protected void onDestroy() {
        super.onDestroy();

        singleThreadRunner.terminateRunning();
    }

//   This method is automatically called when the user touches the screen
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        super.onTouchEvent(event);

        activityData.spriteCreator.handleMotionEvent(event);
        shootSound.start(); //TODO - move to doShooting in world manager

        return false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.upButton:
                activityData.spriteCreator.getPlayer().setUpToPressed();
                break;
            case R.id.downButton:
                activityData.spriteCreator.getPlayer().setDownToPressed();
                break;
            default:
                ;
        }
    }

    public void activateShield(View view) {
        activityData.spriteCreator.getPlayer().raiseShield();

        view.setEnabled(false);
        view.setAlpha((float)0.5);
    }


    //This Class takes care of the initial settings to start the game
    public class SpriteEssentialData {

        public GameActivity ctx;
        public Point canvasSize;
        public Rect canvasRect;
        public GameGraphics graphics;
        public GameLogics logics;
        public SpriteCreator spriteCreator;
        public GameSession gameSession;
        public WorldManager worldManager;
        public SpriteCollisions spriteCollisions;
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
            gameSession = new GameSession(ctx , this);

            worldManager = new WorldManager(this);
//            spriteEssentialData.logics.addToManagedList(worldManager);
//            spriteEssentialData.logics.addToManagedList(spriteEssentialData.spriteCollisions);
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
        killThread();
        HeroMenuActivity.isBackFromStage = true;
//        finish();
        Log.i("Finish" , "Finish1 "+HeroMenuActivity.isBackFromStage );
        Intent intent = new Intent(this , MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
//        singleThreadRunner.terminateRunning();
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
    public void playSound(View view){
        shootSound.start();
    }

    public void killThread() {
        singleThreadRunner.terminateRunning();
        singleThreadRunner = null;
        System.gc();
    }
    public void pauseThread() {
        singleThreadRunner.pauseRunning();
        isPaused = true;
    }
    public void resumeThread() {
        singleThreadRunner.resumeRunning();
        isPaused = false;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        pauseThread();
        doAlert();
    }

    public void doAlert() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Quit Game")
                .setMessage("Are you sure you want to quit this game?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        goBackToLevelSelect(); // exit game
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        resumeThread();// do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//
//        singleThreadRunner.resumeRunning();
//
//    }

}
