package com.puzzlegame.numbergo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Levels extends AppCompatActivity {

    ScrollView scrlView;
    RelativeLayout relLay;
    TableLayout grid;
    TableRow[] tableRows;

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
        scrlView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scrlView.setPadding(30, 30 , 30, 30);

        RelativeLayout gridRelLay = new RelativeLayout(this);
        gridRelLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        gridRelLay.setGravity(Gravity.CENTER_HORIZONTAL);

        scrlView.addView(gridRelLay);

        grid = new TableLayout(this);
        grid.setId(View.generateViewId());

        tableRows = new TableRow[15];

        levels = new Button[tableRows.length][3];
        int level = 1;

        TableRow.LayoutParams levelParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < levels.length; i++) {
            tableRows[i] = new TableRow(this);
            tableRows[i].setGravity(Gravity.CENTER_HORIZONTAL);

            for(int j = 0; j < levels[0].length; j++) {
                levels[i][j] = new Button(this);
                levels[i][j].setText(level + "");
                levels[i][j].setTextSize(25);
                levels[i][j].setTextColor(Color.WHITE);
                levels[i][j].setBackground(ContextCompat.getDrawable(this, R.drawable.custom_level_bg));

                level++;

                if(i > 0) {
                    levelParams.topMargin = 30;
                }

                if(j > 0) {
                    levelParams.leftMargin = 30;
                } else {
                    levelParams.leftMargin = 0;
                }

                tableRows[i].addView(levels[i][j], levelParams);

                levels[i][j].setOnClickListener(view -> {
                    Intent intent = new Intent(Levels.this, Play.class);
                    startActivity(intent);
                });
            }

            grid.addView(tableRows[i]);
        }

        RelativeLayout.LayoutParams gridRelLayParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        gridRelLayParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        gridRelLay.addView(grid, gridRelLayParams);

        RelativeLayout.LayoutParams scrlViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        scrlViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        scrlViewParams.addRule(RelativeLayout.BELOW, levelsLabel.getId());
        scrlViewParams.bottomMargin = 20;

        relLay.addView(scrlView, scrlViewParams);

        setContentView(relLay);
    }
}