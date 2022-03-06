package com.puzzlegame.numbergo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Play extends AppCompatActivity {

    RelativeLayout relLay;

    TextView level;

    GridLayout gridLayout;
    Button[][] boxes;

    Button startBtn;

    View line;

    Button[] options;
    int optionsLength = 4;

    GridLayout numoptionsgrid;

    boolean isNumSelected = false;
    int currentNumOptionRowIndex = -1;
    int currentNumOptionColIndex = -1;
    int currentNumSelected;
    String currentDirectionSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        relLay = new RelativeLayout(this);
        relLay.setBackgroundColor(Color.WHITE);

        level = new TextView(this);
        level.setText("Level 1");
        level.setTextSize(40);
        level.setTextColor(Color.BLACK);
        level.setId(View.generateViewId());

        RelativeLayout.LayoutParams levelNumParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        levelNumParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        levelNumParams.topMargin = 20;

        relLay.addView(level, levelNumParams);

        gridLayout = new GridLayout(this);
        gridLayout.setRowCount(4);
        gridLayout.setColumnCount(4);
        gridLayout.setId(View.generateViewId());

        boxes = new Button[gridLayout.getRowCount()][gridLayout.getColumnCount()];

        int randRowPosition = 3;
        int randColPosition = 2;

        RelativeLayout.LayoutParams boxParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < boxes.length; i++) {
            for(int j = 0; j < boxes[0].length; j++) {
                boxes[i][j] = new Button(this);
                boxes[i][j].setTextSize(30);
                boxes[i][j].setTextColor(Color.BLACK);

                if(i == randRowPosition && j == randColPosition) {
                    boxes[i][j].setBackgroundResource(R.drawable.bluecoloredbox);
                } else {
                    boxes[i][j].setBackgroundResource(R.drawable.whitecoloredbox);
                }

                boxParams.topMargin = 5;
                boxParams.leftMargin = 5;

                if(i == boxes.length - 1) {
                    boxParams.bottomMargin = 5;
                } else {
                    boxParams.bottomMargin = 0;
                }

                if(j == boxes[0].length - 1) {
                    boxParams.rightMargin = 5;
                } else {
                    boxParams.rightMargin = 0;
                }

                gridLayout.addView(boxes[i][j], boxParams);

                int i2 = i;
                int j2 = j;

                boxes[i][j].setOnClickListener(view -> {
                    if(isNumSelected && boxes[i2][j2].getText().toString().isEmpty()) {
                        boxes[i2][j2].setText(currentNumSelected + "");

                        switch (currentDirectionSelected) {
                            case "left":
                                boxes[i2][j2].setBackgroundResource(R.drawable.leftwhitecoloredbox);
                                break;
                            case "right":
                                boxes[i2][j2].setBackgroundResource(R.drawable.rightwhitecoloredbox);
                                break;
                            case "top":
                                boxes[i2][j2].setBackgroundResource(R.drawable.upwhitecoloredbox);
                                break;
                            case "down":
                                boxes[i2][j2].setBackgroundResource(R.drawable.downwhitecoloredbox);
                                break;
                        }

                        isNumSelected = false;

                        options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.whitecoloredbox);
                        currentNumOptionRowIndex = -1;
                        currentNumOptionColIndex = -1;
                    } else if(!boxes[i2][j2].getText().toString().isEmpty()) {
                        isNumSelected = true;
                        currentNumSelected = Integer.parseInt(boxes[i2][j2].getText().toString());

                        //fix the direction issue

                        currentNumOptionRowIndex = i2;
                        currentNumOptionColIndex = j2;
                    }
                });
            }
        }

        RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        gridParams.addRule(RelativeLayout.BELOW, level.getId());
        gridParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        gridParams.topMargin = 20;
        gridParams.bottomMargin = 30;

        relLay.addView(gridLayout, gridParams);

        startBtn = new Button(this);
        startBtn.setText(getString(R.string.start));
        startBtn.setTextSize(25);
        startBtn.setId(View.generateViewId());

        RelativeLayout.LayoutParams startBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        startBtnParams.addRule(RelativeLayout.BELOW, gridLayout.getId());
        startBtnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        startBtnParams.bottomMargin = 20;

        relLay.addView(startBtn, startBtnParams);

        line = new View(this);
        line.setBackgroundColor(Color.BLUE);
        line.setId(View.generateViewId());

        RelativeLayout.LayoutParams lineParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 10);
        lineParams.addRule(RelativeLayout.BELOW, startBtn.getId());
        lineParams.bottomMargin = 30;

        relLay.addView(line, lineParams);

        options = new Button[optionsLength];

        numoptionsgrid = new GridLayout(this);
        numoptionsgrid.setRowCount(2);
        numoptionsgrid.setColumnCount(4);

        RelativeLayout.LayoutParams numoptionsParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < options.length; i++) {
            options[i] = new Button(this);
            options[i].setTextSize(30);
            options[i].setBackgroundResource(R.drawable.whitecoloredbox);

            if(i > 0) {
                numoptionsParams.leftMargin = 5;
            }

            numoptionsgrid.addView(options[i], numoptionsParams);

            int i2 = i;
            options[i].setOnClickListener(view -> {
                if(isNumSelected && options[i2].getText().toString().isEmpty()) {
                    options[i2].setText(currentNumSelected);

                    boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setText("");
                    boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.whitecoloredbox);



                    currentNumOptionRowIndex = -1;
                    currentNumOptionColIndex = -1;

                    isNumSelected = false;
                }
            });
        }

        options[0].setText("2");
        options[0].setBackgroundResource(R.drawable.downwhitecoloredbox);

        options[1].setText("1");
        options[1].setBackgroundResource(R.drawable.rightwhitecoloredbox);

        options[2].setText("1");
        options[2].setBackgroundResource(R.drawable.downwhitecoloredbox);

        options[3].setText("1");
        options[3].setBackgroundResource(R.drawable.rightwhitecoloredbox);

        RelativeLayout.LayoutParams numoptionsgridParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numoptionsgridParams.addRule(RelativeLayout.BELOW, line.getId());
        numoptionsgridParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        relLay.addView(numoptionsgrid, numoptionsgridParams);

        setContentView(relLay);
    }
}