package com.puzzlegame.numbergo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
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

    Button[][] levelsBtns;

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
        levelsLabelParams.bottomMargin = 10;

        relLay.addView(levelsLabel, levelsLabelParams);

        scrlView = new ScrollView(this);
        scrlView.setFillViewport(true);

        grid = new GridLayout(this);
        grid.setId(View.generateViewId());
        grid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        grid.setColumnCount(4);
        grid.setRowCount(4);

        levelsBtns = new Button[4][4];
        int level = 1;

        for(int i = 0; i < levelsBtns.length; i++) {
            for(int j = 0; j < levelsBtns[0].length; j++) {
                levelsBtns[i][j] = new Button(this);
                levelsBtns[i][j].setText(level + "");
                level++;

                grid.addView(levelsBtns[i][j]);
            }
        }

        scrlView.addView(grid);

        RelativeLayout.LayoutParams scrlViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        scrlViewParams.addRule(RelativeLayout.BELOW, levelsLabel.getId());

        relLay.addView(scrlView, scrlViewParams);

        setContentView(relLay);
    }
}