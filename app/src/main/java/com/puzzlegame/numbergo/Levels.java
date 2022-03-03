package com.puzzlegame.numbergo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Levels extends AppCompatActivity {

    ScrollView scrlView;
    RelativeLayout relLay;
    GridLayout grid;

    TextView levelsLabel;

    Button[][] levels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        relLay = new RelativeLayout(this);

        levelsLabel = new TextView(this);
        levelsLabel.setText(getString(R.string.levels));
        levelsLabel.setTextSize(40);
        levelsLabel.setTextColor(Color.BLACK);
        levelsLabel.setId(View.generateViewId());

        RelativeLayout.LayoutParams levelsLabelParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        levelsLabelParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        levelsLabelParams.topMargin = 30;
        levelsLabelParams.bottomMargin = 40;

        relLay.addView(levelsLabel, levelsLabelParams);

        scrlView = new ScrollView(this);
        scrlView.setPadding(30, 30 , 30, 30);
        scrlView.setBackgroundColor(Color.BLACK);

        RelativeLayout gridRelLay = new RelativeLayout(this);
        gridRelLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        gridRelLay.setGravity(Gravity.CENTER_HORIZONTAL);

        scrlView.addView(gridRelLay);

        grid = new GridLayout(this);
        grid.setId(View.generateViewId());
        grid.setColumnCount(3);
        grid.setRowCount(15);

        levels = new Button[15][3];
        int level = 1;

        RelativeLayout.LayoutParams levelParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < levels.length; i++) {
            for(int j = 0; j < levels[0].length; j++) {
                levels[i][j] = new Button(this);
                levels[i][j].setText(level + "");
                levels[i][j].setTextSize(25);
                levels[i][j].setTextColor(Color.WHITE);
                levels[i][j].setBackground(ContextCompat.getDrawable(this, R.drawable.custom_level_bg));
                levels[i][j].setPadding(30, 0, 30, 0);
                level++;

                if(i > 0) {
                    levelParams.topMargin = 20;
                }

                if(j > 0) {
                    levelParams.leftMargin = 20;
                } else {
                    levelParams.leftMargin = 0;
                }

                grid.addView(levels[i][j], levelParams);
            }
        }

        gridRelLay.addView(grid);

        RelativeLayout.LayoutParams scrlViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        scrlViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        scrlViewParams.addRule(RelativeLayout.BELOW, levelsLabel.getId());
        scrlViewParams.bottomMargin = 20;

        relLay.addView(scrlView, scrlViewParams);

        setContentView(relLay);
    }
}