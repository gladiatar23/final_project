package com.gameapps.alex.singlethreadgame.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gameapps.alex.singlethreadgame.R;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener{

    Button campaignButton;
    Button vsButton;
    Button exitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.gc();

//        fullScreen();
        setContentView(R.layout.activity_main_menu);

        campaignButton = (Button) findViewById(R.id.campaign_mode);
        vsButton = (Button) findViewById(R.id.solo_game);
        exitButton = (Button) findViewById(R.id.exit);


        campaignButton.setOnClickListener(this);
        vsButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);


    }


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
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
