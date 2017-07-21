package com.gameapps.alex.singlethreadgame.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gameapps.alex.singlethreadgame.MyApp;
import com.gameapps.alex.singlethreadgame.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener{

    Button campaignButton;
    Button vsButton;
    Button exitButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//garbage collector
        System.gc();



        setContentView(R.layout.activity_main_menu);
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.gameapps.alex.singlethreadgame",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }


        //Sets the display to LANDSCAPE according to how the user holds the device using the accelerometer sensor
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);

//Definition of buttons and their listener
        campaignButton = (Button) findViewById(R.id.campaign_mode);
        vsButton = (Button) findViewById(R.id.solo_game);
        exitButton = (Button) findViewById(R.id.exit);


        campaignButton.setOnClickListener(this);
        vsButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);


    }

//Define what the program will do when a button is pressed
    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.campaign_mode:
                startActivity(new Intent(this, LevelMenuActivity.class));
                break;
            case R.id.solo_game:
                startActivity(new Intent(this, FacebookActivity.class));
                break;
            case R.id.scoreButton:
                startActivity(new Intent(this, ScoreActivity.class));
                break;
            case R.id.exit:
                startActivity(new Intent(this, Score2Activity.class));
                break;
            default:
                break;
        }

    }
//Cancel back button action
    @Override
    public void onBackPressed() {
//        super.onBackPressed();

    }
}
