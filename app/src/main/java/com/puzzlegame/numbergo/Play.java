package com.puzzlegame.numbergo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Play extends AppCompatActivity {
    ScrollView scrlView;

    RelativeLayout relLay;
    LinearLayout linLay;

    Button restartBtn;

    TextView level;

    GridLayout gridLayout;
    Button[][] boxes;
    int[][] nums;
    String[][] boxActions;

    Button[] options;
    int[] optionNums;
    String[] optionsAction;
    int optionsLength;
    int optionsUsed = 0;

    GridLayout numOptionsGrid;

    boolean isNumSelected = false;
    int currentNumOptionRowIndex = -1;
    int currentNumOptionColIndex = -1;
    int previousCurrentNumOptionRowIndex = -1;
    int previousCurrentNumOptionColIndex = -1;
    int currentNumSelected;
    String currentActionSelected;

    boolean containsStartingPosition;

    int startingX;
    int startingY;
    int endingX;
    int endingY;

    CountDownTimer timer;

    int currentAction;
    int previousAction;
    int totalActionsUsed = 0;
    String currentDirection;

    int totalJumps;

    boolean puzzleSolved = false;
    boolean failedToSolvePuzzle = false;

    int currentLevel;

    final int BUTTON_WIDTH = 175;
    final int BUTTON_HEIGHT = 175;

    LayoutInflater tryAgainInflater;
    LayoutInflater levelCompleteInflater;
    View tryAgainLayout;
    View levelCompleteLayout;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        currentLevel = intent.getIntExtra(getString(R.string.level), -1);

        tryAgainInflater = getLayoutInflater();
        tryAgainLayout = tryAgainInflater.inflate(R.layout.custom_toast_try_again, findViewById(R.id.custom_toast_try_again));

        levelCompleteInflater = getLayoutInflater();
        levelCompleteLayout = levelCompleteInflater.inflate(R.layout.custom_toast_level_complete, findViewById(R.id.custom_toast_level_complete));

        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);

        scrlView = new ScrollView(this);

        relLay = new RelativeLayout(this);
        relLay.setBackgroundColor(Color.WHITE);

        scrlView.addView(relLay);

        linLay = new LinearLayout(this);
        linLay.setOrientation(LinearLayout.VERTICAL);
        linLay.setGravity(Gravity.CENTER_HORIZONTAL);

        RelativeLayout.LayoutParams linLayParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linLayParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        linLayParams.topMargin = 50;
        linLayParams.bottomMargin = 40;

        relLay.addView(linLay, linLayParams);

        restartBtn = new Button(this);
        restartBtn.setText(getString(R.string.restart));
        restartBtn.setTextColor(Color.WHITE);
        restartBtn.setBackgroundResource(R.drawable.custom_restart_background);

        restartBtn.setOnClickListener(view -> restartCurrentLevel());

        LinearLayout.LayoutParams restartBtnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 95);
        restartBtnParams.bottomMargin = 20;

        linLay.addView(restartBtn, restartBtnParams);

        restartBtn.setOnClickListener(view -> restartCurrentLevel());

        level = new TextView(this);
        level.setText(String.format(getString(R.string.level), currentLevel + ""));
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

        if(currentLevel > 20) {
            containsStartingPosition = false;
            startingX = -1;
            startingY = -1;
        } else {
            containsStartingPosition = true;
        }

        switch(currentLevel) {
            case 1:
                startingX = 1;
                startingY = 0;
                endingX = 2;
                endingY = 2;
                break;
            case 2:
                startingX = 1;
                startingY = 1;
                endingX = 2;
                endingY = 0;
                break;
            case 3:
                startingX = 3;
                startingY = 1;
                endingX = 1;
                endingY = 1;
                break;
            case 4:
                startingX = 2;
                startingY = 1;
                endingX = 0;
                endingY = 0;
                break;
            case 5:
                startingX = 0;
                startingY = 0;
                endingX = 2;
                endingY = 0;
                break;
            case 6:
                startingX = 2;
                startingY = 0;
                endingX = 2;
                endingY = 1;
                break;
            case 7:
                startingX = 2;
                startingY = 0;
                endingX = 3;
                endingY = 1;
                break;
            case 8:
                startingX = 0;
                startingY = 2;
                endingX = 2;
                endingY = 3;
                break;
            case 9:
                startingX = 2;
                startingY = 1;
                endingX = 0;
                endingY = 3;
                break;
            case 10:
                startingX = 0;
                startingY = 2;
                endingX = 1;
                endingY = 3;
                break;
            case 11:
                startingX = 0;
                startingY = 3;
                endingX = 2;
                endingY = 1;
                break;
            case 12:
                startingX = 0;
                startingY = 0;
                endingX = 2;
                endingY = 2;
                break;
            case 13:
                startingX = 0;
                startingY = 1;
                endingX = 1;
                endingY = 2;
                break;
            case 14:
                startingX = 0;
                startingY = 0;
                endingX = 1;
                endingY = 0;
                break;
            case 15:
            case 16:
                startingX = 0;
                startingY = 0;
                endingX = 0;
                endingY = 1;
                break;
            case 17:
                startingX = 1;
                startingY = 0;
                endingX = 1;
                endingY = 2;
                break;
            case 18:
                startingX = 0;
                startingY = 3;
                endingX = 2;
                endingY = 3;
                break;
            case 19:
                startingX = 0;
                startingY = 0;
                endingX = 1;
                endingY = 2;
                break;
            case 20:
                startingX = 0;
                startingY = 2;
                endingX = 3;
                endingY = 1;
                break;
            case 21:
                endingX = 2;
                endingY = 2;
                break;
            case 22:
            case 26:
                endingX = 1;
                endingY = 2;
                break;
            case 23:
                endingX = 2;
                endingY = 0;
                break;
            case 24:
                endingX = 3;
                endingY = 3;
                break;
            case 25:
                endingX = 3;
                endingY = 2;
                break;
            case 27:
                endingX = 2;
                endingY = 3;
                break;
            case 28:
                endingX = 2;
                endingY = 1;
                break;
        }

        LinearLayout.LayoutParams boxParams = new LinearLayout.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT);

        for(int i = 0; i < boxes.length; i++) {
            for(int j = 0; j < boxes[0].length; j++) {
                nums[i][j] = -1;
                boxes[i][j] = new Button(this);
                boxes[i][j].setTextSize(18);
                boxes[i][j].setTextColor(Color.rgb(51, 51, 51));
                boxes[i][j].setAllCaps(false);
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
                                if(boxActions[i2][j2].equals(getString(R.string.block))) {
                                    options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.custom_graycoloredbox);
                                } else {
                                    options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                                }
                                optionsAction[currentNumOptionRowIndex] = boxActions[i2][j2];
                                optionNums[currentNumOptionRowIndex] = nums[i2][j2];
                            } else {
                                nums[currentNumOptionRowIndex][currentNumOptionColIndex] = nums[i2][j2];
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setText(boxes[i2][j2].getText().toString());
                                if(boxActions[i2][j2].equals(getString(R.string.block))) {
                                    if(currentNumOptionRowIndex == startingY && currentNumOptionColIndex == startingX) {
                                        boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_graycoloredbox_orangeborder);
                                    } else {
                                        boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_graycoloredbox);
                                    }
                                } else {
                                    if(currentNumOptionRowIndex == startingY && currentNumOptionColIndex == startingX) {
                                        boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox_orangeborder);
                                    } else {
                                        boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                                    }
                                }
                                boxActions[currentNumOptionRowIndex][currentNumOptionColIndex] = boxActions[i2][j2];
                            }
                        } else {
                            if(currentNumOptionColIndex == -1) {
                                options[currentNumOptionRowIndex].setText("");
                                options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                                optionsAction[currentNumOptionRowIndex] = "";
                                optionsUsed++;
                            } else {
                                if(currentNumOptionRowIndex == startingY && currentNumOptionColIndex == startingX) {
                                    boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setText(getString(R.string.start));
                                    boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox_orangeborder);
                                } else {
                                    boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setText("");
                                    boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                                }

                                boxActions[currentNumOptionRowIndex][currentNumOptionColIndex] = "";
                                nums[currentNumOptionRowIndex][currentNumOptionColIndex] = -1;
                            }
                        }

                        nums[i2][j2] = currentNumSelected;
                        boxActions[i2][j2] = currentActionSelected;

                        switch (currentActionSelected) {
                            case "left":
                                boxes[i2][j2].setText(currentNumSelected + getString(R.string.l));
                                break;
                            case "right":
                                boxes[i2][j2].setText(currentNumSelected + getString(R.string.r));
                                break;
                            case "up":
                                boxes[i2][j2].setText(currentNumSelected + getString(R.string.u));
                                break;
                            case "down":
                                boxes[i2][j2].setText(currentNumSelected + getString(R.string.d));
                                break;
                            case "block":
                                boxes[i2][j2].setText("");
                                break;
                        }

                        if(i2 == startingY && j2 == startingX) {
                            if(currentActionSelected.equals(getString(R.string.block))) {
                                boxes[i2][j2].setBackgroundResource(R.drawable.custom_graycoloredbox_orangeborder);
                            } else {
                                boxes[i2][j2].setBackgroundResource(R.drawable.custom_whitecoloredbox_orangeborder);
                            }
                        } else {
                            if(currentActionSelected.equals(getString(R.string.block))) {
                                boxes[i2][j2].setBackgroundResource(R.drawable.custom_graycoloredbox);
                            } else {
                                boxes[i2][j2].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                            }
                        }

                        isNumSelected = false;

                        currentNumOptionRowIndex = -1;
                        currentNumOptionColIndex = -1;
                    } else if(!boxActions[i2][j2].isEmpty() && !boxes[i2][j2].getText().toString().equals(getString(R.string.start))) {
                        isNumSelected = true;
                        currentNumSelected = nums[i2][j2];
                        currentActionSelected = boxActions[i2][j2];

                        if(boxActions[i2][j2].equals(getString(R.string.block))) {
                            boxes[i2][j2].setBackgroundResource(R.drawable.custom_graycoloredbox_blueborder);
                        } else {
                            boxes[i2][j2].setBackgroundResource(R.drawable.custom_whitecoloredbox_blueborder);
                        }

                        currentNumOptionRowIndex = i2;
                        currentNumOptionColIndex = j2;
                    }
                });

                boxes[i][j].setOnLongClickListener(view -> {
                    if(optionsUsed == optionsLength && ((i2 == startingY && j2 == startingX && !boxes[i2][j2].getText().toString().equals(getString(R.string.start))) || !containsStartingPosition) && !boxActions[i2][j2].equals(getString(R.string.block))) {
                        if(isNumSelected) {
                            isNumSelected = false;
                            if(boxActions[currentNumOptionRowIndex][currentNumOptionColIndex].equals(getString(R.string.block))) {
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_graycoloredbox);
                            } else {
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                            }
                        }

                        for (int i1 = 0; i1 < boxes.length; i1++) {
                            for (int j1 = 0; j1 < boxes[0].length; j1++) {
                                boxes[i1][j1].setEnabled(false);

                                if(!boxActions[i1][j1].equals("") && !boxActions[i1][j1].equals(getString(R.string.block)) && !boxActions[i1][j1].equals(getString(R.string.target))) {
                                    totalActionsUsed++;
                                }
                            }
                        }

                        startingX = j2;
                        startingY = i2;

                        currentNumOptionRowIndex = startingY;
                        currentNumOptionColIndex = startingX;

                        currentAction = nums[currentNumOptionRowIndex][currentNumOptionColIndex];
                        previousAction = currentAction;
                        currentDirection = boxActions[currentNumOptionRowIndex][currentNumOptionColIndex];

                        startTimer();

                        return true;
                    } else {
                        return false;
                    }
                });
            }
        }

        setTargetPosition();

        LinearLayout.LayoutParams gridParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        gridParams.topMargin = 20;
        gridParams.bottomMargin = 100;

        linLay.addView(gridLayout, gridParams);

        switch (currentLevel) {
            case 1:
            case 2:
                optionsLength = 2;
                break;
            case 3:
            case 4:
            case 21:
                optionsLength = 4;
                break;
            case 5:
            case 27:
                optionsLength = 7;
                break;
            case 6:
                optionsLength = 5;
                break;
            case 7:
            case 8:
            case 9:
            case 11:
                optionsLength = 6;
                break;
            case 10:
                optionsLength = 3;
                break;
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 19:
            case 28:
                optionsLength = 9;
                break;
            case 17:
            case 25:
            case 26:
                optionsLength = 8;
                break;
            case 18:
            case 23:
            case 24:
                optionsLength = 11;
                break;
            case 20:
            case 22:
                optionsLength = 10;
                break;
        }

        options = new Button[optionsLength];
        optionNums = new int[optionsLength];
        optionsAction = new String[optionsLength];

        numOptionsGrid = new GridLayout(this);
        numOptionsGrid.setRowCount((int) Math.ceil(optionsLength/4));
        numOptionsGrid.setColumnCount(4);

        LinearLayout.LayoutParams numOptionsParams = new LinearLayout.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT);
        numOptionsParams.topMargin = 20;
        numOptionsParams.bottomMargin = 20;
        numOptionsParams.leftMargin = 10;
        numOptionsParams.rightMargin = 10;

        for(int i = 0; i < options.length; i++) {
            options[i] = new Button(this);
            options[i].setTextSize(18);
            options[i].setBackgroundResource(R.drawable.custom_whitecoloredbox);

            numOptionsGrid.addView(options[i], numOptionsParams);

            int i2 = i;
            options[i].setOnClickListener(view -> {
                if(isNumSelected) {
                    if(!optionsAction[i2].isEmpty()) {
                        if(currentNumOptionColIndex == -1) {
                            options[currentNumOptionRowIndex].setText(options[i2].getText().toString());
                            optionsAction[currentNumOptionRowIndex] = optionsAction[i2];
                            optionNums[currentNumOptionRowIndex] = optionNums[i2];
                            if(optionsAction[i2].equals(getString(R.string.block))) {
                                options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.custom_graycoloredbox);
                            } else {
                                options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                            }
                        } else {
                            nums[currentNumOptionRowIndex][currentNumOptionColIndex] = optionNums[i2];
                            boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setText(options[i2].getText().toString());
                            if(optionsAction[i2].equals(getString(R.string.block))) {
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_graycoloredbox);
                            } else {
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                            }
                            boxActions[currentNumOptionRowIndex][currentNumOptionColIndex] = optionsAction[i2];
                        }
                    } else {
                        if(currentNumOptionColIndex == -1) {
                            options[currentNumOptionRowIndex].setText("");
                            optionsAction[currentNumOptionRowIndex] = "";
                            options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                        } else {
                            nums[currentNumOptionRowIndex][currentNumOptionColIndex] = -1;
                            if(currentNumOptionRowIndex == startingY && currentNumOptionColIndex == startingX) {
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setText(getString(R.string.start));
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox_orangeborder);
                            } else {
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setText("");
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                            }

                            boxActions[currentNumOptionRowIndex][currentNumOptionColIndex] = "";
                            optionsUsed--;
                        }
                    }

                    switch (currentActionSelected) {
                        case "left":
                            options[i2].setText(currentNumSelected + getString(R.string.l));
                            break;
                        case "right":
                            options[i2].setText(currentNumSelected + getString(R.string.r));
                            break;
                        case "up":
                            options[i2].setText(currentNumSelected + getString(R.string.u));
                            break;
                        case "down":
                            options[i2].setText(currentNumSelected + getString(R.string.d));
                            break;
                        case "block":
                            options[i2].setText("");
                            break;
                    }

                    if(currentActionSelected.equals(getString(R.string.block))) {
                        options[i2].setBackgroundResource(R.drawable.custom_graycoloredbox);
                    } else {
                        options[i2].setBackgroundResource(R.drawable.custom_whitecoloredbox);
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

                    if(optionsAction[i2].equals(getString(R.string.block))) {
                        options[i2].setBackgroundResource(R.drawable.custom_graycoloredbox_blueborder);
                    } else {
                        options[i2].setBackgroundResource(R.drawable.custom_whitecoloredbox_blueborder);
                    }

                    currentNumOptionRowIndex = i2;
                    currentNumOptionColIndex = -1;
                }
            });
        }

        levels();

        for(int i = 0; i < options.length; i++) {
            if(!optionsAction[i].equals(getString(R.string.block))) {
                totalJumps += optionNums[i];
            }
        }

        LinearLayout.LayoutParams numOptionsGridParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        numOptionsGridParams.gravity = Gravity.CENTER_HORIZONTAL;

        linLay.addView(numOptionsGrid, numOptionsGridParams);

        relLay.setOnClickListener(view -> {
            if(isNumSelected) {
                isNumSelected = false;

                currentNumSelected = -1;

                if(currentNumOptionColIndex == -1) {
                    if(currentActionSelected.equals(getString(R.string.block))) {
                        options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.custom_graycoloredbox);
                    } else {
                        options[currentNumOptionRowIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                    }
                } else {
                    if(currentActionSelected.equals(getString(R.string.block))) {
                        if(currentNumOptionRowIndex == startingY && currentNumOptionColIndex == startingX) {
                            boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_graycoloredbox_orangeborder);
                        } else {
                            boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_graycoloredbox);
                        }
                    } else {
                        if(currentNumOptionRowIndex == startingY && currentNumOptionColIndex == startingX) {
                            boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox_orangeborder);
                        } else {
                            boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                        }
                    }
                }

                currentActionSelected = "";

                currentNumOptionRowIndex = -1;
                currentNumOptionColIndex = -1;
            }
        });

        setContentView(scrlView);
    }

    public void startTimer() {
        timer = new CountDownTimer(1500L * totalJumps, 1000) {
            @Override
            public void onTick(long l) {
                if(previousCurrentNumOptionRowIndex != -1 && previousCurrentNumOptionColIndex != -1) {
                    if(previousCurrentNumOptionRowIndex == endingY && previousCurrentNumOptionColIndex == endingX) {
                        boxes[previousCurrentNumOptionRowIndex][previousCurrentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox_greenborder);
                    } else if (containsStartingPosition && previousCurrentNumOptionRowIndex == startingY && previousCurrentNumOptionColIndex == startingX) {
                        boxes[previousCurrentNumOptionRowIndex][previousCurrentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox_orangeborder);
                    } else {
                        boxes[previousCurrentNumOptionRowIndex][previousCurrentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                    }
                }

                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_bluecoloredbox);

                if(failedToSolvePuzzle) {
                    if(currentNumOptionRowIndex == endingY && currentNumOptionColIndex == endingX) {
                        boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_redcoloredbox);
                        boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setTextColor(Color.WHITE);
                    }
                    timer.onFinish();
                }

                previousCurrentNumOptionRowIndex = currentNumOptionRowIndex;
                previousCurrentNumOptionColIndex = currentNumOptionColIndex;

                if(puzzleSolved) {
                    boxes[endingY][endingX].setTextColor(Color.WHITE);
                    boxes[endingY][endingX].setBackgroundResource(R.drawable.custom_greencoloredbox_darkgreenborder);
                    timer.onFinish();
                } else {
                    if (currentAction - 1 >= 0) {
                        switch (currentDirection) {
                            case "left":
                                if (currentNumOptionColIndex - 1 >= 0) {
                                    if(!boxActions[currentNumOptionRowIndex][currentNumOptionColIndex - 1].equals(getString(R.string.block))) {
                                        currentNumOptionColIndex--;
                                    }

                                    currentAction--;

                                    if(currentNumOptionColIndex - 1 >= 0) {
                                        if (boxActions[currentNumOptionRowIndex][currentNumOptionColIndex - 1].equals(getString(R.string.block))) {
                                            totalActionsUsed--;
                                            targetReached();
                                        }
                                    }
                                } else {
                                    failedToSolvePuzzle = true;
                                }
                                break;
                            case "right":
                                if (currentNumOptionColIndex + 1 < boxes[0].length) {
                                    if(!boxActions[currentNumOptionRowIndex][currentNumOptionColIndex + 1].equals(getString(R.string.block))) {
                                        currentNumOptionColIndex++;
                                    }

                                    currentAction--;

                                    if(currentNumOptionColIndex + 1 < boxes[0].length) {
                                        if (boxActions[currentNumOptionRowIndex][currentNumOptionColIndex + 1].equals(getString(R.string.block))) {
                                            totalActionsUsed--;
                                            targetReached();
                                        }
                                    }
                                } else {
                                    failedToSolvePuzzle = true;
                                }
                                break;
                            case "up":
                                if (currentNumOptionRowIndex - 1 >= 0) {
                                    if(!boxActions[currentNumOptionRowIndex - 1][currentNumOptionColIndex].equals(getString(R.string.block))) {
                                        currentNumOptionRowIndex--;
                                    }

                                    currentAction--;

                                    if(currentNumOptionRowIndex - 1 >= 0) {
                                        if (boxActions[currentNumOptionRowIndex - 1][currentNumOptionColIndex].equals(getString(R.string.block))) {
                                            totalActionsUsed--;
                                            targetReached();
                                        }
                                    }
                                } else {
                                    failedToSolvePuzzle = true;
                                }
                                break;
                            case "down":
                                if (currentNumOptionRowIndex + 1 < boxes.length) {
                                    if(!boxActions[currentNumOptionRowIndex + 1][currentNumOptionColIndex].equals(getString(R.string.block))) {
                                        currentNumOptionRowIndex++;
                                    }

                                    currentAction--;

                                    if(currentNumOptionRowIndex + 1 < boxes[0].length) {
                                        if (boxActions[currentNumOptionRowIndex + 1][currentNumOptionColIndex].equals(getString(R.string.block))) {
                                            totalActionsUsed--;
                                            targetReached();
                                        }
                                    }
                                } else {
                                    failedToSolvePuzzle = true;
                                }
                                break;
                        }
                    }

                    if (currentAction == 0 && !failedToSolvePuzzle) {
                        totalActionsUsed--;

                        if (boxActions[currentNumOptionRowIndex][currentNumOptionColIndex].equals(getString(R.string.target))) {
                            if(totalActionsUsed == 0) {
                                puzzleSolved = true;
                            } else {
                                failedToSolvePuzzle = true;
                            }
                        } else if (!boxActions[currentNumOptionRowIndex][currentNumOptionColIndex].isEmpty()) {
                            currentAction = nums[currentNumOptionRowIndex][currentNumOptionColIndex];
                            currentDirection = boxActions[currentNumOptionRowIndex][currentNumOptionColIndex];
                        } else {
                            failedToSolvePuzzle = true;
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                timer.cancel();

                if(puzzleSolved) {
                    toast.setView(levelCompleteLayout);
                } else if(failedToSolvePuzzle) {
                    toast.setView(tryAgainLayout);
                }

                toast.show();
            }
        };

        timer.start();

    }

    public void targetReached() {
        if(boxActions[currentNumOptionRowIndex][currentNumOptionColIndex].equals(getString(R.string.target))) {
            if (totalActionsUsed == 0) {
                puzzleSolved = true;
            } else {
                failedToSolvePuzzle = true;
            }
        } else if(boxActions[currentNumOptionRowIndex][currentNumOptionColIndex].equals(getString(R.string.up)) && (currentNumOptionRowIndex - 1 < 0 || boxActions[currentNumOptionRowIndex - 1][currentNumOptionColIndex].equals(getString(R.string.block)))) {
            failedToSolvePuzzle = true;
        } else if(boxActions[currentNumOptionRowIndex][currentNumOptionColIndex].equals(getString(R.string.down)) && (currentNumOptionRowIndex + 1 >= boxActions.length || boxActions[currentNumOptionRowIndex + 1][currentNumOptionColIndex].equals(getString(R.string.block)))) {
            failedToSolvePuzzle = true;
        } else if(boxActions[currentNumOptionRowIndex][currentNumOptionColIndex].equals(getString(R.string.left)) && (currentNumOptionColIndex - 1 < 0 || boxActions[currentNumOptionRowIndex][currentNumOptionColIndex - 1].equals(getString(R.string.block)))) {
            failedToSolvePuzzle = true;
        } else if(boxActions[currentNumOptionRowIndex][currentNumOptionColIndex].equals(getString(R.string.right)) && (currentNumOptionColIndex + 1 >= boxActions[0].length || boxActions[currentNumOptionRowIndex][currentNumOptionColIndex + 1].equals(getString(R.string.block)))) {
            failedToSolvePuzzle = true;
        } else {
            currentAction = nums[currentNumOptionRowIndex][currentNumOptionColIndex];
            currentDirection = boxActions[currentNumOptionRowIndex][currentNumOptionColIndex];
        }
    }

    public void restartCurrentLevel() {
        setTargetPosition();

        for(int i = 0; i < boxes.length; i++) {
            for(int j = 0; j < boxes[0].length; j++) {
                boxActions[i][j] = "";

                if(i == endingY && j == endingX) {
                    boxes[i][j].setText(getString(R.string.end));
                    boxes[i][j].setEnabled(false);
                    boxActions[i][j] = getString(R.string.target);
                } else {
                    boxes[i][j].setEnabled(true);
                    boxes[i][j].setText("");
                    boxes[i][j].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                    boxActions[i][j] = "";
                }

                nums[i][j] = -1;
            }
        }

        isNumSelected = false;
        currentNumSelected = -1;
        currentActionSelected = "";
        puzzleSolved = false;
        failedToSolvePuzzle = false;
        optionsUsed = 0;
        currentNumOptionRowIndex = -1;
        currentNumOptionColIndex = -1;
        previousCurrentNumOptionRowIndex = -1;
        previousCurrentNumOptionColIndex = -1;
        totalActionsUsed = 0;

        levels();
    }

    public void setTargetPosition() {
        boxes[endingY][endingX].setText(getString(R.string.end));
        boxes[endingY][endingX].setTextColor(Color.BLACK);
        boxes[endingY][endingX].setBackgroundResource(R.drawable.custom_whitecoloredbox_greenborder);
        boxes[endingY][endingX].setEnabled(false);
        boxActions[endingY][endingX] = getString(R.string.target);
    }

    public void levels() {
        if(containsStartingPosition) {
            boxes[startingY][startingX].setText(getString(R.string.start));
            boxes[startingY][startingX].setBackgroundResource(R.drawable.custom_whitecoloredbox_orangeborder);
        }

        switch (currentLevel) {
            case 1:                                                                                 //1
                assignAction(0, 2, getString(R.string.d), getString(R.string.down));
                assignAction(1, 1, getString(R.string.r), getString(R.string.right));
                break;
            case 2:                                                                                 //2
                assignAction(0, 1, getString(R.string.r), getString(R.string.right));
                assignAction(1, 1, getString(R.string.u), getString(R.string.up));
                break;
            case 3:                                                                                 //3
                assignAction(0, 1, getString(R.string.d), getString(R.string.down));
                assignAction(1, 1, getString(R.string.u), getString(R.string.up));
                assignAction(2, 1, getString(R.string.l), getString(R.string.left));
                assignAction(3, 1, getString(R.string.l), getString(R.string.left));
                break;
            case 4:                                                                                 //4
                assignAction(0, 1, getString(R.string.u), getString(R.string.up));
                assignAction(1, 2, getString(R.string.l), getString(R.string.left));
                assignAction(2, 1, getString(R.string.r), getString(R.string.right));
                assignAction(3, 1, getString(R.string.l), getString(R.string.left));
                break;
            case 5:                                                                                 //5
                assignAction(0, 1, getString(R.string.u), getString(R.string.up));
                assignAction(1, 2, getString(R.string.r), getString(R.string.right));
                assignAction(2, 2, getString(R.string.d), getString(R.string.down));
                assignAction(3, 1, getString(R.string.l), getString(R.string.left));
                assignAction(4, 1, getString(R.string.l), getString(R.string.left));
                assignAction(5, 1, getString(R.string.u), getString(R.string.up));
                assignAction(6, 2, getString(R.string.r), getString(R.string.right));
                break;
            case 6:                                                                                 //6
                assignAction(0, 1, getString(R.string.l), getString(R.string.left));
                assignAction(1, 1, getString(R.string.u), getString(R.string.up));
                assignAction(2, 2, getString(R.string.l), getString(R.string.left));
                assignAction(3, 1, getString(R.string.r), getString(R.string.right));
                assignAction(4, 2, getString(R.string.d), getString(R.string.down));
                break;
            case 7:                                                                                 //7
                assignAction(0, 2, getString(R.string.d), getString(R.string.down));
                assignAction(1, 1, getString(R.string.l), getString(R.string.left));
                assignAction(2, 2, getString(R.string.l), getString(R.string.left));
                assignAction(3, 1, getString(R.string.u), getString(R.string.up));
                assignAction(4, 1, getString(R.string.r), getString(R.string.right));
                assignAction(5, 3, getString(R.string.r), getString(R.string.right));
                break;
            case 8:                                                                                 //8
                assignAction(0, 1, getString(R.string.u), getString(R.string.up));
                assignAction(1, 1, getString(R.string.r), getString(R.string.right));
                assignAction(2, 2, getString(R.string.d), getString(R.string.down));
                assignAction(3, 2, getString(R.string.r), getString(R.string.right));
                assignAction(4, 1, getString(R.string.r), getString(R.string.right));
                assignAction(5, 2, getString(R.string.l), getString(R.string.left));
                break;
            case 9:                                                                                 //9
                assignAction(0, 1, getString(R.string.d), getString(R.string.down));
                assignAction(1, 1, getString(R.string.l), getString(R.string.left));
                assignAction(2, 2, getString(R.string.d), getString(R.string.down));
                assignAction(3, 1, getString(R.string.l), getString(R.string.left));
                assignAction(4, 2, getString(R.string.u), getString(R.string.up));
                assignAction(5, 1, getString(R.string.d), getString(R.string.down));
                break;
            case 10:                                                                                //10
                assignAction(0, 0, "", getString(R.string.block));
                assignAction(1, 3, getString(R.string.r), getString(R.string.right));
                assignAction(2, 1, getString(R.string.d), getString(R.string.down));
                break;
            case 11:                                                                                //11
                assignAction(0, 3, getString(R.string.u), getString(R.string.up));
                assignAction(1, 2, getString(R.string.r), getString(R.string.right));
                assignAction(2, 2, getString(R.string.r), getString(R.string.right));
                assignAction(3, 0, "", getString(R.string.block));
                assignAction(4, 0, "", getString(R.string.block));
                assignAction(5, 0, "", getString(R.string.block));
                break;
            case 12:                                                                                //12
                assignAction(0, 3, getString(R.string.r), getString(R.string.right));
                assignAction(1, 1, getString(R.string.d), getString(R.string.down));
                assignAction(2, 1, getString(R.string.d), getString(R.string.down));
                assignAction(3, 1, getString(R.string.d), getString(R.string.down));
                assignAction(4, 2, getString(R.string.r), getString(R.string.right));
                assignAction(5, 2, getString(R.string.l), getString(R.string.left));
                assignAction(6, 2, getString(R.string.u), getString(R.string.up));
                assignAction(7, 0, "", getString(R.string.block));
                assignAction(8, 0, "", getString(R.string.block));
                break;
            case 13:                                                                                //13
                assignAction(0, 1, getString(R.string.d), getString(R.string.down));
                assignAction(1, 2, getString(R.string.l), getString(R.string.left));
                assignAction(2, 3, getString(R.string.r), getString(R.string.right));
                assignAction(3, 1, getString(R.string.d), getString(R.string.down));
                assignAction(4, 3, getString(R.string.r), getString(R.string.right));
                assignAction(5, 3, getString(R.string.l), getString(R.string.left));
                assignAction(6, 1, getString(R.string.u), getString(R.string.up));
                assignAction(7, 0, "", getString(R.string.block));
                assignAction(8, 0, "", getString(R.string.block));
                break;
            case 14:                                                                                //14
                assignAction(0, 3, getString(R.string.r), getString(R.string.right));
                assignAction(1, 1, getString(R.string.d), getString(R.string.down));
                assignAction(2, 2, getString(R.string.l), getString(R.string.left));
                assignAction(3, 1, getString(R.string.d), getString(R.string.down));
                assignAction(4, 2, getString(R.string.u), getString(R.string.up));
                assignAction(5, 2, getString(R.string.u), getString(R.string.up));
                assignAction(6, 2, getString(R.string.d), getString(R.string.down));
                assignAction(7, 0, "", getString(R.string.block));
                assignAction(8, 0, "", getString(R.string.block));
                break;
            case 15:                                                                                //15
                assignAction(0, 3, getString(R.string.d), getString(R.string.down));
                assignAction(1, 2, getString(R.string.r), getString(R.string.right));
                assignAction(2, 1, getString(R.string.r), getString(R.string.right));
                assignAction(3, 3, getString(R.string.u), getString(R.string.up));
                assignAction(4, 2, getString(R.string.l), getString(R.string.left));
                assignAction(5, 2, getString(R.string.l), getString(R.string.left));
                assignAction(6, 2, getString(R.string.r), getString(R.string.right));
                assignAction(7, 0, "", getString(R.string.block));
                assignAction(8, 0, "", getString(R.string.block));
                break;
            case 16:                                                                                //16
                nums[1][2] = 2;
                boxes[1][2].setText(nums[1][2] + getString(R.string.l));
                boxes[1][2].setEnabled(false);
                boxActions[1][2] = getString(R.string.left);

                assignAction(0, 1, getString(R.string.l), getString(R.string.left));
                assignAction(1, 2, getString(R.string.r), getString(R.string.right));
                assignAction(2, 3, getString(R.string.d), getString(R.string.down));
                assignAction(3, 1, getString(R.string.u), getString(R.string.up));
                assignAction(4, 2, getString(R.string.r), getString(R.string.right));
                assignAction(5, 2, getString(R.string.l), getString(R.string.left));
                assignAction(6, 2, getString(R.string.u), getString(R.string.up));
                assignAction(7, 1, getString(R.string.r), getString(R.string.right));
                assignAction(8, 0, "", getString(R.string.block));
                break;
            case 17:                                                                                //17
                nums[1][1] = 2;
                boxes[1][1].setText("");
                boxes[1][1].setBackgroundResource(R.drawable.custom_graycoloredbox);
                boxes[1][1].setEnabled(false);
                boxActions[1][1] = getString(R.string.block);

                assignAction(0, 3, getString(R.string.r), getString(R.string.right));
                assignAction(1, 2, getString(R.string.d), getString(R.string.down));
                assignAction(2, 1, getString(R.string.l), getString(R.string.left));
                assignAction(3, 1, getString(R.string.u), getString(R.string.up));
                assignAction(4, 2, getString(R.string.d), getString(R.string.down));
                assignAction(5, 1, getString(R.string.u), getString(R.string.up));
                assignAction(6, 2, getString(R.string.l), getString(R.string.left));
                assignAction(7, 1, getString(R.string.l), getString(R.string.left));
                break;
            case 18:                                                                                //18
                assignAction(0, 1, getString(R.string.d), getString(R.string.down));
                assignAction(1, 1, getString(R.string.d), getString(R.string.down));
                assignAction(2, 3, getString(R.string.l), getString(R.string.left));
                assignAction(3, 2, getString(R.string.d), getString(R.string.down));
                assignAction(4, 2, getString(R.string.r), getString(R.string.right));
                assignAction(5, 3, getString(R.string.r), getString(R.string.right));
                assignAction(6, 1, getString(R.string.l), getString(R.string.left));
                assignAction(7, 3, getString(R.string.u), getString(R.string.up));
                assignAction(8, 2, getString(R.string.r), getString(R.string.right));
                assignAction(9, 0, "", getString(R.string.block));
                assignAction(10, 0, "", getString(R.string.block));
                break;
            case 19:                                                                                //19
                assignAction(0, 1, getString(R.string.d), getString(R.string.down));
                assignAction(1, 3, getString(R.string.u), getString(R.string.up));
                assignAction(2, 2, getString(R.string.r), getString(R.string.right));
                assignAction(3, 1, getString(R.string.d), getString(R.string.down));
                assignAction(4, 1, getString(R.string.l), getString(R.string.left));
                assignAction(5, 3, getString(R.string.d), getString(R.string.down));
                assignAction(6, 0, "", getString(R.string.down));
                assignAction(7, 0, "", getString(R.string.down));
                assignAction(8, 0, "", getString(R.string.down));
                break;
            case 20:                                                                                //20
                nums[0][2] = 0;
                boxes[0][2].setText("");
                boxes[0][2].setBackgroundResource(R.drawable.custom_graycoloredbox);
                boxes[0][2].setEnabled(false);
                boxActions[0][2] = getString(R.string.block);

                assignAction(0, 1, getString(R.string.d), getString(R.string.down));
                assignAction(1, 2, getString(R.string.u), getString(R.string.up));
                assignAction(2, 1, getString(R.string.u), getString(R.string.up));
                assignAction(3, 2, getString(R.string.r), getString(R.string.right));
                assignAction(4, 3, getString(R.string.u), getString(R.string.up));
                assignAction(5, 1, getString(R.string.l), getString(R.string.left));
                assignAction(6, 1, getString(R.string.u), getString(R.string.up));
                assignAction(7, 2, getString(R.string.r), getString(R.string.right));
                assignAction(8, 2, getString(R.string.d), getString(R.string.down));
                assignAction(9, 1, getString(R.string.u), getString(R.string.up));
                break;
            case 21:                                                                                //21
                assignAction(0, 1, getString(R.string.d), getString(R.string.down));
                assignAction(1, 2, getString(R.string.r), getString(R.string.right));
                assignAction(2, 2, getString(R.string.l), getString(R.string.left));
                assignAction(3, 1, getString(R.string.r), getString(R.string.right));
                break;
            case 22:                                                                                //22
                assignAction(0, 3, getString(R.string.u), getString(R.string.up));
                assignAction(1, 2, getString(R.string.r), getString(R.string.right));
                assignAction(2, 3, getString(R.string.d), getString(R.string.down));
                assignAction(3, 2, getString(R.string.l), getString(R.string.left));
                assignAction(4, 2, getString(R.string.l), getString(R.string.left));
                assignAction(5, 1, getString(R.string.d), getString(R.string.down));
                assignAction(6, 2, getString(R.string.d), getString(R.string.down));
                assignAction(7, 0, "", getString(R.string.block));
                assignAction(8, 0, "", getString(R.string.block));
                assignAction(9, 0, "", getString(R.string.block));
                break;
            case 23:                                                                                //23
                assignAction(0, 1, getString(R.string.d), getString(R.string.down));
                assignAction(1, 2, getString(R.string.r), getString(R.string.right));
                assignAction(2, 3, getString(R.string.r), getString(R.string.right));
                assignAction(3, 3, getString(R.string.d), getString(R.string.down));
                assignAction(4, 2, getString(R.string.u), getString(R.string.up));
                assignAction(5, 2, getString(R.string.l), getString(R.string.left));
                assignAction(6, 2, getString(R.string.l), getString(R.string.left));
                assignAction(7, 2, getString(R.string.u), getString(R.string.up));
                assignAction(8, 3, getString(R.string.r), getString(R.string.right));
                assignAction(9, 0, "", getString(R.string.block));
                assignAction(10, 0, "", getString(R.string.block));
                break;
            case 24:                                                                                //24
                assignAction(0, 2, getString(R.string.d), getString(R.string.down));
                assignAction(1, 1, getString(R.string.d), getString(R.string.down));
                assignAction(2, 2, getString(R.string.r), getString(R.string.right));
                assignAction(3, 1, getString(R.string.d), getString(R.string.down));
                assignAction(4, 2, getString(R.string.l), getString(R.string.left));
                assignAction(5, 1, getString(R.string.l), getString(R.string.left));
                assignAction(6, 3, getString(R.string.r), getString(R.string.right));
                assignAction(7, 2, getString(R.string.r), getString(R.string.right));
                assignAction(8, 0, "", getString(R.string.block));
                assignAction(9, 0, "", getString(R.string.block));
                assignAction(10, 0, "", getString(R.string.block));
                break;
            case 25:                                                                                //25
                nums[0][0] = 1;
                boxes[0][0].setText(nums[0][0] + getString(R.string.d));
                boxes[0][0].setEnabled(false);
                boxActions[0][0] = getString(R.string.down);

                nums[1][1] = 1;
                boxes[1][1].setText(nums[0][0] + getString(R.string.d));
                boxes[1][1].setEnabled(false);
                boxActions[1][1] = getString(R.string.down);

                nums[2][0] = 1;
                boxes[2][0].setText(nums[0][0] + getString(R.string.d));
                boxes[2][0].setEnabled(false);
                boxActions[2][0] = getString(R.string.down);

                assignAction(0, 1, getString(R.string.l), getString(R.string.left));
                assignAction(1, 1, getString(R.string.r), getString(R.string.right));
                assignAction(2, 2, getString(R.string.r), getString(R.string.right));
                assignAction(3, 1, getString(R.string.u), getString(R.string.up));
                assignAction(4, 1, getString(R.string.l), getString(R.string.left));
                assignAction(5, 2, getString(R.string.l), getString(R.string.left));
                assignAction(6, 2, getString(R.string.r), getString(R.string.right));
                assignAction(7, 2, getString(R.string.r), getString(R.string.right));
                break;
            case 26:                                                                                //26
                assignAction(0, 1, getString(R.string.l), getString(R.string.left));
                assignAction(1, 2, getString(R.string.d), getString(R.string.down));
                assignAction(2, 2, getString(R.string.d), getString(R.string.down));
                assignAction(3, 3, getString(R.string.u), getString(R.string.up));
                assignAction(4, 2, getString(R.string.r), getString(R.string.right));
                assignAction(5, 2, getString(R.string.d), getString(R.string.down));
                assignAction(6, 2, getString(R.string.l), getString(R.string.left));
                assignAction(7, 0, "", getString(R.string.block));
                break;
            case 27:                                                                                //27
                assignAction(0, 2, getString(R.string.r), getString(R.string.right));
                assignAction(1, 3, getString(R.string.d), getString(R.string.down));
                assignAction(2, 1, getString(R.string.l), getString(R.string.left));
                assignAction(3, 1, getString(R.string.r), getString(R.string.right));
                assignAction(4, 2, getString(R.string.u), getString(R.string.up));
                assignAction(5, 1, getString(R.string.d), getString(R.string.down));
                assignAction(6, 1, getString(R.string.d), getString(R.string.down));
                break;
            case 28:                                                                                //28
                assignAction(0, 1, getString(R.string.d), getString(R.string.down));
                assignAction(1, 3, getString(R.string.l), getString(R.string.left));
                assignAction(2, 3, getString(R.string.u), getString(R.string.up));
                assignAction(3, 3, getString(R.string.r), getString(R.string.right));
                assignAction(4, 1, getString(R.string.d), getString(R.string.down));
                assignAction(5, 2, getString(R.string.d), getString(R.string.down));
                assignAction(6, 2, getString(R.string.r), getString(R.string.right));
                assignAction(7, 0, "", getString(R.string.block));
                assignAction(8, 0, "", getString(R.string.block));
                break;
        }
    }

    public void assignAction(int index, int action, String actionFirstLetter, String actionFullName) {
        optionNums[index] = action;
        if(actionFullName.equals(getString(R.string.block))) {
            options[index].setText("");
            options[index].setBackgroundResource(R.drawable.custom_graycoloredbox);
        } else {
            String str = (action + "").concat(actionFirstLetter);
            options[index].setText(str);
        }
        optionsAction[index] = actionFullName;
    }

}