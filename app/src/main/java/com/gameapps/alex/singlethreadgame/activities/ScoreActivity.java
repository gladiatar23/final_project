package com.gameapps.alex.singlethreadgame.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gameapps.alex.singlethreadgame.GameSession;
import com.gameapps.alex.singlethreadgame.R;
import com.gameapps.alex.singlethreadgame.data_handle.DBLevelHandler;
import com.gameapps.alex.singlethreadgame.data_handle.LevelForTable;

import java.util.List;

public class ScoreActivity extends AppCompatActivity implements View.OnClickListener{
    Button backFromScore;

    LinearLayout scoresLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_score);
        backFromScore = (Button)findViewById(R.id.backFromScore);
        backFromScore.setOnClickListener(this);

        scoresLayout = (LinearLayout)findViewById(R.id.scoresVertical);

        addAllScoreRows();

    }

    public void addAllScoreRows() {
        DBLevelHandler dbLevelHandler = DBLevelHandler.getInstance(this);
        List<LevelForTable> allLevels = dbLevelHandler.getAllLevels();

        for (LevelForTable lft : allLevels) {
            ScoreLevelRow slr = new ScoreLevelRow(this , lft.getLevelName() , lft.getIsWon() , lft.getBestScore());
            scoresLayout.addView(slr);

            Log.i("levelllll", " " + lft.getLevelName());
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromScore){
            finish();
        }
    }

    public class ScoreLevelRow extends LinearLayout {

        public ScoreLevelRow(Context context, String levelName, boolean isWon, int highScore) {
            super(context);

            setOrientation(LinearLayout.HORIZONTAL);

            TextView tvLvName = new TextView(context);
            tvLvName.setText(levelName);
            tvLvName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            TextView tvIsWon = new TextView(context);
            tvIsWon.setText(String.valueOf(isWon));
            tvIsWon.setTextColor(0xffbdbdbd);
            TextView tvHighScore = new TextView(context);
            tvHighScore.setTextColor(0xffbdbdbd);
            tvHighScore.setText(String.valueOf(highScore));

            this.addView(tvLvName);
            this.addView(tvIsWon);
            this.addView(tvHighScore);
        }
    }
}
