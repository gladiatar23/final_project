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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        campaignButton = (Button) findViewById(R.id.campaign_mode);
        vsButton = (Button) findViewById(R.id.vs_friends);


        campaignButton.setOnClickListener(this);
        vsButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.campaign_mode:
                startActivity(new Intent(this, LevelMenuActivity.class));
                break;
            case R.id.vs_friends:
                startActivity(new Intent(this, FacebookActivity.class));
                break;
            default:
                break;
        }

    }
}
