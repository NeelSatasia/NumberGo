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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Levels extends AppCompatActivity {

    ScrollView scrlView;
    RelativeLayout relLay;

    TextView levelsLabel;

    Button[][] levels;
    GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scrlView = new ScrollView(this);

        relLay = new RelativeLayout(this);
        scrlView.addView(relLay);

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

        grid = new GridLayout(this);
        grid.setRowCount(15);
        grid.setColumnCount(3);
        grid.setId(View.generateViewId());

        levels = new Button[grid.getRowCount()][grid.getColumnCount()];
        int level = 1;

        RelativeLayout.LayoutParams levelParams = new RelativeLayout.LayoutParams(150, 140);

        for(int i = 0; i < levels.length; i++) {
            for(int j = 0; j < levels[0].length; j++) {
                levels[i][j] = new Button(this);
                levels[i][j].setText(level + "");
                levels[i][j].setTextSize(25);
                levels[i][j].setTextColor(Color.WHITE);
                levels[i][j].setBackgroundResource(R.drawable.custom_level_bg);

                if (i > 0) {
                    levelParams.topMargin = 50;
                }

                if(j > 0) {
                    levelParams.leftMargin = 50;
                } else {
                    levelParams.leftMargin = 0;
                }

                int i2 = i;
                int j2 = j;

                levels[i][j].setOnClickListener(view -> {
                    Intent intent = new Intent(Levels.this, Play.class);
                    intent.putExtra(getString(R.string.level), Integer.parseInt(levels[i2][j2].getText().toString()));
                    startActivity(intent);
                });

                grid.addView(levels[i][j], levelParams);

                level++;
            }
        }

        RelativeLayout.LayoutParams gridRelLayParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        gridRelLayParams.addRule(RelativeLayout.BELOW, levelsLabel.getId());
        gridRelLayParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        relLay.addView(grid, gridRelLayParams);

        setContentView(scrlView);
    }
}