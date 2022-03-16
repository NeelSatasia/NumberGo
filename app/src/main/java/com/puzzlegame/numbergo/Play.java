package com.puzzlegame.numbergo;

import androidx.appcompat.app.AlertDialog;
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
        }

        LinearLayout.LayoutParams boxParams = new LinearLayout.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT);

        for(int i = 0; i < boxes.length; i++) {
            for(int j = 0; j < boxes[0].length; j++) {
                nums[i][j] = -1;
                boxes[i][j] = new Button(this);
                boxes[i][j].setTextSize(20);
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
                                        boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_graycoloredbox_greenborder);
                                    } else {
                                        boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_graycoloredbox);
                                    }
                                } else {
                                    if(currentNumOptionRowIndex == startingY && currentNumOptionColIndex == startingX) {
                                        boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox_greenborder);
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
                                    boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox_greenborder);
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
                                boxes[i2][j2].setBackgroundResource(R.drawable.custom_graycoloredbox_greenborder);
                            } else {
                                boxes[i2][j2].setBackgroundResource(R.drawable.custom_whitecoloredbox_greenborder);
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
                    if(optionsUsed == optionsLength && i2 == startingY && j2 == startingX && !boxes[i2][j2].getText().toString().equals(getString(R.string.start)) && !boxActions[i2][j2].equals(getString(R.string.block))) {
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
                                /*if(i1 != endingY || j1 != endingX) {
                                    boxes[i1][j1].setText("");
                                }*/

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
                optionsLength = 4;
                break;
            case 5:
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
                optionsLength = 9;
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
            options[i].setTextSize(20);
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
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox_greenborder);
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
            if(!optionsAction.equals(getString(R.string.block))) {
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
                        boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_graycoloredbox);
                    } else {
                        boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
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
        timer = new CountDownTimer(1500 * totalJumps, 1000) {
            @Override
            public void onTick(long l) {
                if(previousCurrentNumOptionRowIndex != -1 && previousCurrentNumOptionColIndex != -1) {
                    boxes[previousCurrentNumOptionRowIndex][previousCurrentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                }

                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_bluecoloredbox);

                if(failedToSolvePuzzle) {
                    timer.onFinish();
                }

                previousCurrentNumOptionRowIndex = currentNumOptionRowIndex;
                previousCurrentNumOptionColIndex = currentNumOptionColIndex;

                if(puzzleSolved) {
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
                    boxActions[i][j] = "";
                }

                boxes[i][j].setBackgroundResource(R.drawable.custom_whitecoloredbox);

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
        boxes[endingY][endingX].setEnabled(false);
        boxActions[endingY][endingX] = getString(R.string.target);
    }

    public void levels() {
        boxes[startingY][startingX].setText(getString(R.string.start));
        boxes[startingY][startingX].setBackgroundResource(R.drawable.custom_whitecoloredbox_greenborder);

        switch (currentLevel) {
            case 1:                                                             //1
                optionNums[0] = 2;
                options[0].setText(optionNums[0] + getString(R.string.d));
                optionsAction[0] = getString(R.string.down);

                optionNums[1] = 1;
                options[1].setText(optionNums[1] + getString(R.string.r));
                optionsAction[1] = getString(R.string.right);
                break;
            case 2:                                                             //2
                optionNums[0] = 1;
                options[0].setText(optionNums[0] + getString(R.string.r));
                optionsAction[0] = getString(R.string.right);

                optionNums[1] = 1;
                options[1].setText(optionNums[1] + getString(R.string.u));
                optionsAction[1] = getString(R.string.up);
                break;
            case 3:                                                             //3
                optionNums[0] = 1;
                options[0].setText(optionNums[0] + getString(R.string.d));
                optionsAction[0] = getString(R.string.down);

                optionNums[1] = 1;
                options[1].setText(optionNums[1] + getString(R.string.u));
                optionsAction[1] = getString(R.string.up);

                optionNums[2] = 1;
                options[2].setText(optionNums[2] + getString(R.string.l));
                optionsAction[2] = getString(R.string.left);

                optionNums[3] = 1;
                options[3].setText(optionNums[3] + getString(R.string.l));
                optionsAction[3] = getString(R.string.left);
                break;
            case 4:                                                             //4
                optionNums[0] = 1;
                options[0].setText(optionNums[0] + getString(R.string.u));
                optionsAction[0] = getString(R.string.up);

                optionNums[1] = 2;
                options[1].setText(optionNums[1] + getString(R.string.l));
                optionsAction[1] = getString(R.string.left);

                optionNums[2] = 1;
                options[2].setText(optionNums[2] + getString(R.string.r));
                optionsAction[2] = getString(R.string.right);

                optionNums[3] = 1;
                options[3].setText(optionNums[3] + getString(R.string.l));
                optionsAction[3] = getString(R.string.left);
                break;
            case 5:                                                             //5
                optionNums[0] = 1;
                options[0].setText(optionNums[0] + getString(R.string.u));
                optionsAction[0] = getString(R.string.up);

                optionNums[1] = 2;
                options[1].setText(optionNums[1] + getString(R.string.r));
                optionsAction[1] = getString(R.string.right);

                optionNums[2] = 2;
                options[2].setText(optionNums[2] + getString(R.string.d));
                optionsAction[2] = getString(R.string.down);

                optionNums[3] = 1;
                options[3].setText(optionNums[3] + getString(R.string.l));
                optionsAction[3] = getString(R.string.left);

                optionNums[4] = 1;
                options[4].setText(optionNums[4] + getString(R.string.l));
                optionsAction[4] = getString(R.string.left);

                optionNums[5] = 1;
                options[5].setText(optionNums[5] + getString(R.string.u));
                optionsAction[5] = getString(R.string.up);

                optionNums[6] = 2;
                options[6].setText(optionNums[6] + getString(R.string.r));
                optionsAction[6] = getString(R.string.right);
                break;
            case 6:                                                             //6
                optionNums[0] = 1;
                options[0].setText(optionNums[0] + getString(R.string.r));
                optionsAction[0] = getString(R.string.right);

                optionNums[1] = 1;
                options[1].setText(optionNums[1] + getString(R.string.u));
                optionsAction[1] = getString(R.string.up);

                optionNums[2] = 2;
                options[2].setText(optionNums[2] + getString(R.string.l));
                optionsAction[2] = getString(R.string.left);

                optionNums[3] = 1;
                options[3].setText(optionNums[3] + getString(R.string.r));
                optionsAction[3] = getString(R.string.right);

                optionNums[4] = 2;
                options[4].setText(optionNums[4] + getString(R.string.d));
                optionsAction[4] = getString(R.string.down);
                break;
            case 7:                                                             //7
                optionNums[0] = 2;
                options[0].setText(optionNums[0] + getString(R.string.d));
                optionsAction[0] = getString(R.string.down);

                optionNums[1] = 1;
                options[1].setText(optionNums[1] + getString(R.string.l));
                optionsAction[1] = getString(R.string.left);

                optionNums[2] = 2;
                options[2].setText(optionNums[2] + getString(R.string.l));
                optionsAction[2] = getString(R.string.left);

                optionNums[3] = 1;
                options[3].setText(optionNums[3] + getString(R.string.u));
                optionsAction[3] = getString(R.string.up);

                optionNums[4] = 1;
                options[4].setText(optionNums[4] + getString(R.string.r));
                optionsAction[4] = getString(R.string.right);

                optionNums[5] = 3;
                options[5].setText(optionNums[5] + getString(R.string.r));
                optionsAction[5] = getString(R.string.right);
                break;
            case 8:
                optionNums[0] = 1;
                options[0].setText(optionNums[0] + getString(R.string.u));
                optionsAction[0] = getString(R.string.up);

                optionNums[1] = 1;
                options[1].setText(optionNums[1] + getString(R.string.r));
                optionsAction[1] = getString(R.string.right);

                optionNums[2] = 2;
                options[2].setText(optionNums[2] + getString(R.string.d));
                optionsAction[2] = getString(R.string.down);

                optionNums[3] = 2;
                options[3].setText(optionNums[3] + getString(R.string.r));
                optionsAction[3] = getString(R.string.right);

                optionNums[4] = 1;
                options[4].setText(optionNums[4] + getString(R.string.r));
                optionsAction[4] = getString(R.string.right);

                optionNums[5] = 2;
                options[5].setText(optionNums[5] + getString(R.string.l));
                optionsAction[5] = getString(R.string.left);
                break;
            case 9:
                optionNums[0] = 1;
                options[0].setText(optionNums[0] + getString(R.string.d));
                optionsAction[0] = getString(R.string.down);

                optionNums[1] = 1;
                options[1].setText(optionNums[1] + getString(R.string.l));
                optionsAction[1] = getString(R.string.left);

                optionNums[2] = 2;
                options[2].setText(optionNums[2] + getString(R.string.d));
                optionsAction[2] = getString(R.string.down);

                optionNums[3] = 1;
                options[3].setText(optionNums[3] + getString(R.string.l));
                optionsAction[3] = getString(R.string.left);

                optionNums[4] = 2;
                options[4].setText(optionNums[4] + getString(R.string.u));
                optionsAction[4] = getString(R.string.up);

                optionNums[5] = 1;
                options[5].setText(optionNums[5] + getString(R.string.d));
                optionsAction[5] = getString(R.string.down);
                break;
            case 10:                                                            //10
                optionNums[0] = 0;
                options[0].setBackgroundResource(R.drawable.custom_graycoloredbox);
                optionsAction[0] = getString(R.string.block);

                optionNums[1] = 9;
                options[1].setText(optionNums[1] + getString(R.string.r));
                optionsAction[1] = getString(R.string.right);

                optionNums[2] = 1;
                options[2].setText(optionNums[2] + getString(R.string.d));
                optionsAction[2] = getString(R.string.down);
                break;
            case 11:
                optionNums[0] = 3;
                options[0].setText(optionNums[0] + getString(R.string.u));
                optionsAction[0] = getString(R.string.up);

                optionNums[1] = 2;
                options[1].setText(optionNums[1] + getString(R.string.r));
                optionsAction[1] = getString(R.string.right);

                optionNums[2] = 2;
                options[2].setText(optionNums[2] + getString(R.string.r));
                optionsAction[2] = getString(R.string.right);

                optionNums[3] = 0;
                options[3].setBackgroundResource(R.drawable.custom_graycoloredbox);
                optionsAction[3] = getString(R.string.block);

                optionNums[4] = 0;
                options[4].setBackgroundResource(R.drawable.custom_graycoloredbox);
                optionsAction[4] = getString(R.string.block);

                optionNums[5] = 0;
                options[5].setBackgroundResource(R.drawable.custom_graycoloredbox);
                optionsAction[5] = getString(R.string.block);
                break;
            case 12:
                optionNums[0] = 3;
                options[0].setText(optionNums[0] + getString(R.string.r));
                optionsAction[0] = getString(R.string.right);

                optionNums[1] = 1;
                options[1].setText(optionNums[1] + getString(R.string.d));
                optionsAction[1] = getString(R.string.down);

                optionNums[2] = 1;
                options[2].setText(optionNums[2] + getString(R.string.d));
                optionsAction[2] = getString(R.string.down);

                optionNums[3] = 1;
                options[3].setText(optionNums[3] + getString(R.string.d));
                optionsAction[3] = getString(R.string.down);

                optionNums[4] = 2;
                options[4].setText(optionNums[4] + getString(R.string.r));
                optionsAction[4] = getString(R.string.right);

                optionNums[5] = 2;
                options[5].setText(optionNums[5] + getString(R.string.l));
                optionsAction[5] = getString(R.string.left);

                optionNums[6] = 2;
                options[6].setText(optionNums[6] + getString(R.string.u));
                optionsAction[6] = getString(R.string.up);

                optionNums[7] = 0;
                options[7].setBackgroundResource(R.drawable.custom_graycoloredbox);
                optionsAction[7] = getString(R.string.block);

                optionNums[8] = 0;
                options[8].setBackgroundResource(R.drawable.custom_graycoloredbox);
                optionsAction[8] = getString(R.string.block);
                break;
        }
    }

}