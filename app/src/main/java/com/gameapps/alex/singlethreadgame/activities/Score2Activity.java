package com.gameapps.alex.singlethreadgame.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textViewCheck=(TextView) findViewById(R.id.textViewCheck);
        DBLevelHandler dbLevelHandler = DBLevelHandler.getInstance(this);
        List<LevelForTable> allLevels = dbLevelHandler.getAllLevels();
        setContentView(R.layout.activity_score2);
        //textViewCheck=new textViewCheck(this , lft.getLevelName());


    }
    public class TableActivity extends Activity {

//        private boolean mShrink;
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_score2);
//
//            final TableLayout table = (TableLayout) findViewById(R.id.tablelayout);
//            Button button = (Button) findViewById(R.id.toggle);
//            button.setOnClickListener(new Button.OnClickListener() {
//                public void onClick(View v) {
//                    mShrink = !mShrink;
//                    table.setColumnShrinkable(0, mShrink);
//                }
//            });
//            mShrink = table.isColumnShrinkable(0);
//        }
//    }
}}
