package com.puzzlegame.numbergo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

public class Play extends AppCompatActivity {

    RelativeLayout relLay;
    LinearLayout linLay;

    TextView level;

    GridLayout gridLayout;
    Button[][] boxes;
    int[][] nums;
    String[][] boxActions;

    Button[] options;
    int[] optionNums;
    String[] optionsAction;
    int optionsLength = 4;
    int optionsUsed = 0;

    GridLayout numOptionsGrid;

    boolean isNumSelected = false;
    int currentNumOptionRowIndex = -1;
    int currentNumOptionColIndex = -1;
    int previousCurrentNumOptionRowIndex = -1;
    int previousCurrentNumOptionColIndex = -1;
    int currentNumSelected;
    String currentActionSelected;

    int startingX;
    int startingY;
    int endingX;
    int endingY;

    CountDownTimer timer;

    int currentAction;
    String currentDirection;

    boolean puzzleSolved = false;
    boolean gameOver = false;

    int currentLevel;

    final int BUTTON_WIDTH = 175;
    final int BUTTON_HEIGHT = 175;

    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    View popupView;

    TextView levelState;
    Button previousLevelBtn;
    Button restartLevelBtn;
    Button nextLevelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        currentLevel = intent.getIntExtra(getString(R.string.level), -1);

        relLay = new RelativeLayout(this);
        relLay.setBackgroundColor(Color.WHITE);

        linLay = new LinearLayout(this);
        linLay.setOrientation(LinearLayout.VERTICAL);
        linLay.setGravity(Gravity.CENTER_HORIZONTAL);

        RelativeLayout.LayoutParams linLayParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linLayParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        relLay.addView(linLay, linLayParams);

        level = new TextView(this);
        level.setText(getString(R.string.level) + " " + currentLevel);
        level.setTextSize(40);
        level.setTextColor(Color.BLACK);

        LinearLayout.LayoutParams levelNumParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        levelNumParams.bottomMargin = 40;

        linLay.addView(level, levelNumParams);

        gridLayout = new GridLayout(this);
        gridLayout.setRowCount(4);
        gridLayout.setColumnCount(4);

        nums = new int[gridLayout.getRowCount()][gridLayout.getColumnCount()];
        boxes = new Button[gridLayout.getRowCount()][gridLayout.getColumnCount()];
        boxActions = new String[gridLayout.getRowCount()][gridLayout.getColumnCount()];

        switch(currentLevel) {
            case 1:
                endingX = 2;
                endingY = 3;
                break;
        }

        LinearLayout.LayoutParams boxParams = new LinearLayout.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT);

        for(int i = 0; i < boxes.length; i++) {
            for(int j = 0; j < boxes[0].length; j++) {
                nums[i][j] = -1;
                boxes[i][j] = new Button(this);
                boxes[i][j].setTextSize(30);
                boxes[i][j].setTextColor(Color.BLACK);
                boxActions[i][j] = "";

                boxes[i][j].setBackgroundResource(R.drawable.custom_whitecoloredbox);

                if(i > 0) {
                    boxParams.topMargin = 30;
                }

                if(j > 0) {
                    boxParams.leftMargin = 30;
                } else {
                    boxParams.leftMargin = 0;
                }

                gridLayout.addView(boxes[i][j], boxParams);

                int i2 = i;
                int j2 = j;

                boxes[i][j].setOnClickListener(view -> {
                    if(isNumSelected) {
                        if(!boxActions[i2][j2].isEmpty()) {
                            if(currentNumOptionColIndex == -1) {
                                options[currentNumOptionRowIndex].setText(boxes[i2][j2].getText().toString());
                                options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                                optionsAction[currentNumOptionRowIndex] = boxActions[i2][j2];
                                optionNums[currentNumOptionRowIndex] = nums[i2][j2];
                            } else {
                                nums[currentNumOptionRowIndex][currentNumOptionColIndex] = nums[i2][j2];
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setText(boxes[i2][j2].getText().toString());
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                                boxActions[currentNumOptionRowIndex][currentNumOptionColIndex] = boxActions[i2][j2];
                            }
                        } else {
                            if(currentNumOptionColIndex == -1) {
                                options[currentNumOptionRowIndex].setText("");
                                options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                                optionsAction[currentNumOptionRowIndex] = "";
                                optionsUsed++;
                            } else {
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setText("");
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                                boxActions[currentNumOptionRowIndex][currentNumOptionColIndex] = "";
                                nums[currentNumOptionRowIndex][currentNumOptionColIndex] = -1;
                            }
                        }

                        nums[i2][j2] = currentNumSelected;
                        boxActions[i2][j2] = currentActionSelected;

                        switch (currentActionSelected) {
                            case "left":
                                boxes[i2][j2].setText(currentNumSelected + "L");
                                break;
                            case "right":
                                boxes[i2][j2].setText(currentNumSelected + "R");
                                break;
                            case "top":
                                boxes[i2][j2].setText(currentNumSelected + "U");
                                break;
                            case "down":
                                boxes[i2][j2].setText(currentNumSelected + "D");
                                break;
                        }

                        isNumSelected = false;

                        currentNumOptionRowIndex = -1;
                        currentNumOptionColIndex = -1;
                    } else if(!boxActions[i2][j2].isEmpty()) {
                        isNumSelected = true;
                        currentNumSelected = nums[i2][j2];
                        currentActionSelected = boxActions[i2][j2];

                        boxes[i2][j2].setBackgroundResource(R.drawable.custom_greencoloredbox);

                        currentNumOptionRowIndex = i2;
                        currentNumOptionColIndex = j2;
                    }
                });

                boxes[i][j].setOnLongClickListener(view -> {
                    if(optionsUsed == optionsLength) {
                        for (int i1 = 0; i1 < boxes.length; i1++) {
                            for (int j1 = 0; j1 < boxes[0].length; j1++) {
                                boxes[i1][j1].setEnabled(false);
                            }
                        }

                        startingX = j2;
                        startingY = i2;

                        currentNumOptionRowIndex = startingY;
                        currentNumOptionColIndex = startingX;

                        currentAction = nums[currentNumOptionRowIndex][currentNumOptionColIndex];
                        currentDirection = boxActions[currentNumOptionRowIndex][currentNumOptionColIndex];
                        startTimer();

                        return true;
                    } else {
                        return false;
                    }
                });
            }
        }

        boxes[endingY][endingX].setBackgroundResource(R.drawable.custom_bluecoloredbox);
        boxActions[endingY][endingX] = getString(R.string.target);

        LinearLayout.LayoutParams gridParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        gridParams.topMargin = 20;
        gridParams.bottomMargin = 100;

        linLay.addView(gridLayout, gridParams);

        if(currentLevel >= 1 && currentLevel <= 5) {
            optionsLength = 4;
        }

        options = new Button[optionsLength];
        optionNums = new int[optionsLength];
        optionsAction = new String[optionsLength];

        numOptionsGrid = new GridLayout(this);
        numOptionsGrid.setRowCount(optionsLength/4);
        numOptionsGrid.setColumnCount(4);
        numOptionsGrid.setBackgroundResource(R.drawable.custom_whitecoloredbox);

        LinearLayout.LayoutParams numoptionsParams = new LinearLayout.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT);
        numoptionsParams.topMargin = 20;
        numoptionsParams.bottomMargin = 20;
        numoptionsParams.leftMargin = 20;

        for(int i = 0; i < options.length; i++) {
            options[i] = new Button(this);
            options[i].setTextSize(30);
            options[i].setBackgroundResource(R.drawable.custom_whitecoloredbox);

            if(i == options.length - 1) {
                numoptionsParams.rightMargin = 20;
            }

            numOptionsGrid.addView(options[i], numoptionsParams);

            int i2 = i;
            options[i].setOnClickListener(view -> {
                if(isNumSelected) {
                    if(!optionsAction[i2].isEmpty()) {
                        if(currentNumOptionColIndex == -1) {
                            options[currentNumOptionRowIndex].setText(options[i2].getText().toString());
                            optionsAction[currentNumOptionRowIndex] = optionsAction[i2];
                            optionNums[currentNumOptionRowIndex] = optionNums[i2];
                            options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                        } else {
                            nums[currentNumOptionRowIndex][currentNumOptionColIndex] = optionNums[i2];
                            boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setText(options[i2].getText().toString());
                            boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                            boxActions[currentNumOptionRowIndex][currentNumOptionColIndex] = optionsAction[i2];
                        }
                    } else {
                        if(currentNumOptionColIndex == -1) {
                            options[currentNumOptionRowIndex].setText("");
                            optionsAction[currentNumOptionRowIndex] = "";
                            options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                        } else {
                            nums[currentNumOptionRowIndex][currentNumOptionColIndex] = -1;
                            boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setText("");
                            boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                            boxActions[currentNumOptionRowIndex][currentNumOptionColIndex] = "";
                            optionsUsed--;
                        }
                    }

                    switch (currentActionSelected) {
                        case "left":
                            options[i2].setText(currentNumSelected + "L");
                            break;
                        case "right":
                            options[i2].setText(currentNumSelected + "R");
                            break;
                        case "top":
                            options[i2].setText(currentNumSelected + "T");
                            break;
                        case "down":
                            options[i2].setText(currentNumSelected + "D");
                            break;
                    }

                    optionsAction[i2] = currentActionSelected;
                    optionNums[i2] = currentNumSelected;

                    currentNumOptionRowIndex = -1;
                    currentNumOptionColIndex = -1;

                    isNumSelected = false;
                } else if(!optionsAction[i2].isEmpty()) {
                    isNumSelected = true;

                    currentNumSelected = optionNums[i2];
                    currentActionSelected = optionsAction[i2];

                    options[i2].setBackgroundResource(R.drawable.custom_greencoloredbox);

                    currentNumOptionRowIndex = i2;
                    currentNumOptionColIndex = -1;
                }
            });
        }

        switch (currentLevel) {
            case 1:
                level_1();
                break;
        }

        LinearLayout.LayoutParams numOptionsGridParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        linLay.addView(numOptionsGrid, numOptionsGridParams);

        alertDialogBuilder = new AlertDialog.Builder(this);
        popupView = getLayoutInflater().inflate(R.layout.custom_gameover_popup, null);

        alertDialogBuilder.setView(popupView);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        levelState = popupView.findViewById(R.id.levelcomplete);
        previousLevelBtn = popupView.findViewById(R.id.backBtn);
        restartLevelBtn = popupView.findViewById(R.id.restartBtn);
        nextLevelBtn =  popupView.findViewById(R.id.nextBtn);

        relLay.setOnClickListener(view -> {
            if(isNumSelected) {
                isNumSelected = false;

                currentNumSelected = -1;
                currentActionSelected = "";

                if(currentNumOptionColIndex == -1) {
                    options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                } else {
                    boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                }

                currentNumOptionRowIndex = -1;
                currentNumOptionColIndex = -1;
            }
        });

        setContentView(relLay);
    }

    public void startTimer() {
        timer = new CountDownTimer(1500 * 7, 1000) {
            @Override
            public void onTick(long l) {
                if(previousCurrentNumOptionRowIndex != -1 && previousCurrentNumOptionColIndex != -1) {
                    boxes[previousCurrentNumOptionRowIndex][previousCurrentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                }

                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_redcoloredbox);

                previousCurrentNumOptionRowIndex = currentNumOptionRowIndex;
                previousCurrentNumOptionColIndex = currentNumOptionColIndex;

                if(puzzleSolved) {
                    timer.onFinish();
                    alertDialog.show();

                    levelState.setText(getString(R.string.level_complete));
                } else {
                    if (currentAction - 1 >= 0) {
                        switch (currentDirection) {
                            case "left":
                                if (currentNumOptionColIndex - 1 >= 0) {
                                    if(!boxActions[currentNumOptionRowIndex][currentNumOptionColIndex - 1].equals(getString(R.string.block))) {
                                        currentNumOptionColIndex--;
                                    }
                                }
                                break;
                            case "right":
                                if (currentNumOptionColIndex + 1 < boxes[0].length) {
                                    if(!boxActions[currentNumOptionRowIndex][currentNumOptionColIndex + 1].equals(getString(R.string.block))) {
                                        currentNumOptionColIndex++;
                                    }
                                }
                                break;
                            case "up":
                                if (currentNumOptionRowIndex - 1 >= 0) {
                                    if(!boxActions[currentNumOptionRowIndex - 1][currentNumOptionColIndex].equals(getString(R.string.block))) {
                                        currentNumOptionRowIndex--;
                                    }
                                }
                                break;
                            case "down":
                                if (currentNumOptionRowIndex + 1 < boxes.length) {
                                    if(!boxActions[currentNumOptionRowIndex + 1][currentNumOptionColIndex].equals(getString(R.string.block))) {
                                        currentNumOptionRowIndex++;
                                    }
                                }
                                break;
                        }

                        currentAction--;
                    }

                    if (currentAction == 0) {
                        if (boxActions[currentNumOptionRowIndex][currentNumOptionColIndex].equals(getString(R.string.target))) {
                            if(optionsUsed == optionsLength) {
                                puzzleSolved = true;
                            } else {
                                gameOver = true;
                                timer.onFinish();
                                alertDialog.show();

                                levelState.setText(getString(R.string.try_again));
                            }
                        } else if (!boxActions[currentNumOptionRowIndex][currentNumOptionColIndex].isEmpty()) {
                            currentAction = nums[currentNumOptionRowIndex][currentNumOptionColIndex];
                            currentDirection = boxActions[currentNumOptionRowIndex][currentNumOptionColIndex];
                        } else {
                            timer.onFinish();
                        }
                    } else if(!boxActions[currentNumOptionRowIndex][currentNumOptionColIndex].isEmpty()) {
                        gameOver = true;
                    }
                }
            }

            @Override
            public void onFinish() {
                timer.cancel();
            }
        };

        timer.start();

    }

    public void level_1() {
        optionNums[0] = 2;
        options[0].setText(optionNums[0] + "D");
        options[0].setBackgroundResource(R.drawable.custom_whitecoloredbox);
        optionsAction[0] = getString(R.string.down);

        optionNums[1] = 1;
        options[1].setText(optionNums[1] + "R");
        options[1].setBackgroundResource(R.drawable.custom_whitecoloredbox);
        optionsAction[1] = getString(R.string.right);

        optionNums[2] = 1;
        options[2].setText(optionNums[2] + "D");
        options[2].setBackgroundResource(R.drawable.custom_whitecoloredbox);
        optionsAction[2] = getString(R.string.down);

        optionNums[3] = 1;
        options[3].setText(optionNums[3] + "R");
        options[3].setBackgroundResource(R.drawable.custom_whitecoloredbox);
        optionsAction[3] = getString(R.string.right);
    }

}