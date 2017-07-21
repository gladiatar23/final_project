package com.gameapps.alex.singlethreadgame.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gameapps.alex.singlethreadgame.R;
import com.gameapps.alex.singlethreadgame.data_handle.DBLevelHandler;
import com.gameapps.alex.singlethreadgame.data_handle.LevelForTable;

import java.util.List;

public class Score2Activity extends AppCompatActivity {
    TextView textViewCheck;
    LevelForTable lft;

    TableLayout theTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score2);

        theTable = (TableLayout) findViewById(R.id.tablelayout);

        DBLevelHandler dbLevelHandler = DBLevelHandler.getInstance(this);
        List<LevelForTable> allLevels = dbLevelHandler.getAllLevels();

        for (LevelForTable lft : allLevels) {
            insertRow(new RowOrder(lft.getLevelName() , lft.getIsWon() , lft.getBestScore()));

            Log.i("levelllll", " " + lft.getLevelName());
        }

    }

    protected void insertRow(RowOrder ro) {
        TableRow tr = new TableRow(this);
        ro.insertFieldsToTableRow(tr);
        theTable.addView(tr);
    }

    protected class RowOrder {

        private static final int IMAGE_SIZE = 20;
        public View levelNameField, winImageField, scoreField;

        public RowOrder(String lvName , boolean isWon , int score) {
            Log.i("table_roww" , "name2: " + lvName + " , isWon: " + isWon + " , score: " + score);
            levelNameField = new TextView(getBaseContext());
            winImageField = new ImageView(getBaseContext());
            scoreField = new TextView(getBaseContext());

            ((TextView) levelNameField).setText("");
            ((TextView) scoreField).setText("");


            try{
                ((TextView) levelNameField).setText(lvName);
                ((TextView) scoreField).setTextColor(Color.WHITE);
            } catch(Exception e) {;}
            try{
                winImageField.setBackgroundResource(isWon? R.drawable.banan_v : R.drawable.shit_lose);
            }catch(Exception e) {;}
            try {
                ((TextView) scoreField).setText(String.valueOf(score));
                ((TextView) scoreField).setTextColor(Color.WHITE);
            }catch(Exception e) {;}
        }

        public void insertFieldsToTableRow(TableRow tr) {
            //change the order here
            tr.addView(new TextView(getBaseContext()));
            tr.addView(levelNameField);
            tr.addView(winImageField);
            tr.addView(scoreField);
        }

    }

}
