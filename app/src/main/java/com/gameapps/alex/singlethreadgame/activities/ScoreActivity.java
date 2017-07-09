package com.gameapps.alex.singlethreadgame.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gameapps.alex.singlethreadgame.GameSession;
import com.gameapps.alex.singlethreadgame.R;
import com.gameapps.alex.singlethreadgame.data_handle.DBLevelHandler;
import com.gameapps.alex.singlethreadgame.data_handle.LevelForTable;

import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    LinearLayout scoresLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

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

    public class ScoreLevelRow extends LinearLayout {

        public ScoreLevelRow(Context context, String levelName, boolean isWon, int highScore) {
            super(context);

            setOrientation(LinearLayout.HORIZONTAL);

            TextView tvLvName = new TextView(context);
            tvLvName.setText(levelName);
            TextView tvIsWon = new TextView(context);
            tvIsWon.setText(String.valueOf(isWon));
            TextView tvHighScore = new TextView(context);
            tvHighScore.setText(String.valueOf(highScore));

            this.addView(tvLvName);
            this.addView(tvIsWon);
            this.addView(tvHighScore);
        }
    }
}
