package com.puzzlegame.numbergo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Play extends AppCompatActivity {

    RelativeLayout relLay;

    TextView level;

    GridLayout gridLayout;
    Button[][] boxes;
    int[][] nums;
    String[][] boxActions;

    Button startBtn;

    View line;

    Button[] options;
    String[] optionsDirection;
    int optionsLength = 4;

    GridLayout numOptionsGrid;

    boolean isNumSelected = false;
    int currentNumOptionRowIndex = -1;
    int currentNumOptionColIndex = -1;
    int previousCurrentNumOptionRowIndex = -1;
    int previousCurrentNumOptionColIndex = -1;
    int currentNumSelected;
    String currentDirectionSelected;

    int startingX;
    int startingY;
    int endingX;
    int endingY;

    CountDownTimer timer;

    int currentAction;
    String currentDirection;

    boolean puzzleSolved = false;

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

        nums = new int[gridLayout.getRowCount()][gridLayout.getColumnCount()];
        boxes = new Button[gridLayout.getRowCount()][gridLayout.getColumnCount()];
        boxActions = new String[gridLayout.getRowCount()][gridLayout.getColumnCount()];

        int randRowPosition = 3;
        int randColPosition = 2;

        startingX = 0;
        startingY = 0;
        endingX = randColPosition;
        endingY = randRowPosition;

        RelativeLayout.LayoutParams boxParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < boxes.length; i++) {
            for(int j = 0; j < boxes[0].length; j++) {
                nums[i][j] = -1;
                boxes[i][j] = new Button(this);
                boxes[i][j].setTextSize(30);
                boxes[i][j].setTextColor(Color.BLACK);

                if(i == randRowPosition && j == randColPosition) {
                    boxes[i][j].setBackgroundResource(R.drawable.bluecoloredbox);
                    boxActions[i][j] = "target";
                } else if(i == startingY && j == startingX){
                    boxes[i][j].setBackgroundResource(R.drawable.greencoloredbox);
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
                        nums[i2][j2] = currentNumSelected;
                        boxes[i2][j2].setText(currentNumSelected + "");
                        options[currentNumOptionRowIndex].setText("");

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

                        boxActions[i2][j2] = currentDirectionSelected;

                        isNumSelected = false;

                        options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.whitecoloredbox);
                        optionsDirection[currentNumOptionRowIndex] = "";
                        currentNumOptionRowIndex = -1;
                        currentNumOptionColIndex = -1;
                    } else if(!boxes[i2][j2].getText().toString().isEmpty()) {
                        isNumSelected = true;
                        currentNumSelected = nums[i2][j2];
                        currentDirectionSelected = boxActions[i2][j2];

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
        optionsDirection = new String[optionsLength];

        numOptionsGrid = new GridLayout(this);
        numOptionsGrid.setRowCount(2);
        numOptionsGrid.setColumnCount(4);

        RelativeLayout.LayoutParams numoptionsParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < options.length; i++) {
            options[i] = new Button(this);
            options[i].setTextSize(30);
            options[i].setBackgroundResource(R.drawable.whitecoloredbox);

            if(i > 0) {
                numoptionsParams.leftMargin = 5;
            }

            numOptionsGrid.addView(options[i], numoptionsParams);

            int i2 = i;
            options[i].setOnClickListener(view -> {
                if(isNumSelected && options[i2].getText().toString().isEmpty()) {
                    options[i2].setText(currentNumSelected + "");

                    nums[currentNumOptionRowIndex][currentNumOptionColIndex] = -1;
                    boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setText("");

                    switch (currentDirectionSelected) {
                        case "left":
                            options[i2].setBackgroundResource(R.drawable.leftwhitecoloredbox);
                            break;
                        case "right":
                            options[i2].setBackgroundResource(R.drawable.rightwhitecoloredbox);
                            break;
                        case "top":
                            options[i2].setBackgroundResource(R.drawable.upwhitecoloredbox);
                            break;
                        case "down":
                            options[i2].setBackgroundResource(R.drawable.downwhitecoloredbox);
                            break;
                    }

                    optionsDirection[i2] = currentDirectionSelected;

                    boxActions[currentNumOptionRowIndex][currentNumOptionColIndex] = "";

                    currentNumOptionRowIndex = -1;
                    currentNumOptionColIndex = -1;

                    isNumSelected = false;
                } else if(!options[i2].getText().toString().isEmpty()) {
                    isNumSelected = true;

                    currentNumSelected = Integer.parseInt(options[i2].getText().toString());
                    currentDirectionSelected = optionsDirection[i2];

                    currentNumOptionRowIndex = i2;
                    currentNumOptionColIndex = -1;
                }
            });
        }

        options[0].setText("2");
        options[0].setBackgroundResource(R.drawable.downwhitecoloredbox);
        optionsDirection[0] = "down";

        options[1].setText("1");
        options[1].setBackgroundResource(R.drawable.rightwhitecoloredbox);
        optionsDirection[1] = "right";

        options[2].setText("1");
        options[2].setBackgroundResource(R.drawable.downwhitecoloredbox);
        optionsDirection[2] = "down";

        options[3].setText("1");
        options[3].setBackgroundResource(R.drawable.rightwhitecoloredbox);
        optionsDirection[3] = "right";

        RelativeLayout.LayoutParams numoptionsgridParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numoptionsgridParams.addRule(RelativeLayout.BELOW, line.getId());
        numoptionsgridParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        relLay.addView(numOptionsGrid, numoptionsgridParams);

        startBtn.setOnClickListener(view -> {
            for(int i = 0; i < boxes.length; i++) {
                for(int j = 0; j < boxes[0].length; j++) {
                    boxes[i][j].setEnabled(false);
                    boxes[i][j].setText("");

                    if(i != endingY || j != endingX) {
                        boxes[i][j].setBackgroundResource(R.drawable.whitecoloredbox);
                    }
                }
            }

            currentNumOptionRowIndex = startingY;
            currentNumOptionColIndex = startingX;

            currentAction = nums[currentNumOptionRowIndex][currentNumOptionColIndex];
            currentDirection = boxActions[currentNumOptionRowIndex][currentNumOptionColIndex];

            startTimer();
        });

        setContentView(relLay);
    }

    public void startTimer() {
        timer = new CountDownTimer(2000 * 6, 1000) {
            @Override
            public void onTick(long l) {
                if(previousCurrentNumOptionRowIndex != -1 && previousCurrentNumOptionColIndex != -1) {
                    boxes[previousCurrentNumOptionRowIndex][previousCurrentNumOptionColIndex].setBackgroundResource(R.drawable.whitecoloredbox);
                }

                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.redcoloredbox);

                previousCurrentNumOptionRowIndex = currentNumOptionRowIndex;
                previousCurrentNumOptionColIndex = currentNumOptionColIndex;

                if(puzzleSolved) {
                    timer.onFinish();
                } else {
                    if (currentAction - 1 >= 0) {
                        switch (currentDirection) {
                            case "left":
                                if (currentNumOptionColIndex - 1 >= 0) {
                                    currentNumOptionColIndex--;
                                }
                                break;
                            case "right":
                                if (currentNumOptionColIndex + 1 < boxes[0].length) {
                                    currentNumOptionColIndex++;
                                }
                                break;
                            case "up":
                                if (currentNumOptionRowIndex - 1 >= 0) {
                                    currentNumOptionRowIndex--;
                                }
                                break;
                            case "down":
                                if (currentNumOptionRowIndex + 1 < boxes.length) {
                                    currentNumOptionRowIndex++;
                                }
                                break;
                        }

                        currentAction--;
                    }

                    if (currentAction == 0) {
                        if (boxActions[currentNumOptionRowIndex][currentNumOptionColIndex].equals("target")) {
                            puzzleSolved = true;
                        } else if (!boxActions[currentNumOptionRowIndex][currentNumOptionColIndex].isEmpty()) {
                            currentAction = nums[currentNumOptionRowIndex][currentNumOptionColIndex];
                            currentDirection = boxActions[currentNumOptionRowIndex][currentNumOptionColIndex];
                        } else {
                            timer.onFinish();
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                timer.cancel();

                startBtn.setEnabled(false);
            }
        };

        timer.start();

    }
}