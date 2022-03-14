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
import android.widget.TextView;
import android.widget.Toast;

public class Play extends AppCompatActivity {

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
    int currentActionRowIndex = -1;
    int currentActionColIndex = -1;
    int currentNumSelected;
    String currentActionSelected;

    int startingX;
    int startingY;
    int endingX;
    int endingY;

    CountDownTimer timer;

    int currentAction;
    int previousAction;
    String currentDirection;

    int totalJumps;

    boolean puzzleSolved = false;
    boolean failedToSolvePuzzle = false;

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

        relLay = new RelativeLayout(this);
        relLay.setBackgroundColor(Color.WHITE);

        linLay = new LinearLayout(this);
        linLay.setOrientation(LinearLayout.VERTICAL);
        linLay.setGravity(Gravity.CENTER_HORIZONTAL);

        RelativeLayout.LayoutParams linLayParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linLayParams.addRule(RelativeLayout.CENTER_IN_PARENT);

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
            case 10:
                startingX = 0;
                startingY = 2;
                endingX = 1;
                endingY = 3;
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
                                    boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_graycoloredbox);
                                } else {
                                    boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setBackgroundResource(R.drawable.custom_whitecoloredbox);
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
                                } else {
                                    boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setText("");
                                }
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
                            case "up":
                                boxes[i2][j2].setText(currentNumSelected + "U");
                                break;
                            case "down":
                                boxes[i2][j2].setText(currentNumSelected + "D");
                                break;
                            case "block":
                                boxes[i2][j2].setText("");
                                boxes[i2][j2].setBackgroundResource(R.drawable.custom_graycoloredbox);
                                break;
                        }

                        if(!currentActionSelected.equals(getString(R.string.block))) {
                            boxes[i2][j2].setBackgroundResource(R.drawable.custom_whitecoloredbox);
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
                    if(optionsUsed == optionsLength && i2 == startingY && j2 == startingX && !boxes[i2][j2].getText().toString().equals(getString(R.string.start))) {
                        for (int i1 = 0; i1 < boxes.length; i1++) {
                            for (int j1 = 0; j1 < boxes[0].length; j1++) {
                                boxes[i1][j1].setEnabled(false);
                                if(i1 != endingY || j1 != endingX) {
                                    boxes[i1][j1].setText("");
                                }
                            }
                        }

                        startingX = j2;
                        startingY = i2;

                        currentNumOptionRowIndex = startingY;
                        currentNumOptionColIndex = startingX;

                        currentActionRowIndex = startingY;
                        currentActionColIndex = startingX;

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
                optionsLength = 4;
                break;
            case 10:
                optionsLength = 3;
                break;
        }

        options = new Button[optionsLength];
        optionNums = new int[optionsLength];
        optionsAction = new String[optionsLength];

        numOptionsGrid = new GridLayout(this);
        numOptionsGrid.setRowCount(optionsLength/4);
        numOptionsGrid.setColumnCount(4);

        LinearLayout.LayoutParams numOptionsParams = new LinearLayout.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT);
        numOptionsParams.topMargin = 20;
        numOptionsParams.bottomMargin = 20;
        numOptionsParams.leftMargin = 20;

        for(int i = 0; i < options.length; i++) {
            options[i] = new Button(this);
            options[i].setTextSize(20);
            options[i].setBackgroundResource(R.drawable.custom_whitecoloredbox);

            if(i == options.length - 1) {
                numOptionsParams.rightMargin = 20;
            }

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
                            } else {
                                boxes[currentNumOptionRowIndex][currentNumOptionColIndex].setText("");
                            }
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
                        case "up":
                            options[i2].setText(currentNumSelected + "U");
                            break;
                        case "down":
                            options[i2].setText(currentNumSelected + "D");
                            break;
                        case "block":
                            options[i2].setText("");
                            options[i2].setBackgroundResource(R.drawable.custom_graycoloredbox);
                            break;
                    }

                    if(!currentActionSelected.equals(getString(R.string.block))) {
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

        alertDialogBuilder = new AlertDialog.Builder(this);
        popupView = getLayoutInflater().inflate(R.layout.custom_gameover_popup, null);

        alertDialogBuilder.setView(popupView);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        levelState = popupView.findViewById(R.id.levelcomplete);
        previousLevelBtn = popupView.findViewById(R.id.backBtn);
        restartLevelBtn = popupView.findViewById(R.id.restartBtn);
        nextLevelBtn =  popupView.findViewById(R.id.nextBtn);

        restartLevelBtn.setOnClickListener(view -> restartCurrentLevel());

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

        setContentView(relLay);
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
                    timer.cancel();

                    toast.setView(tryAgainLayout);
                    toast.show();
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
                                            currentAction = nums[currentNumOptionRowIndex][currentNumOptionColIndex];
                                            currentDirection = boxActions[currentNumOptionRowIndex][currentNumOptionColIndex];
                                        }
                                    }
                                } else {
                                    timer.cancel();
                                    toast.setView(tryAgainLayout);
                                    toast.show();
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
                                            currentAction = nums[currentNumOptionRowIndex][currentNumOptionColIndex];
                                            currentDirection = boxActions[currentNumOptionRowIndex][currentNumOptionColIndex];
                                        }
                                    }
                                } else {
                                    timer.cancel();
                                    toast.setView(tryAgainLayout);
                                    toast.show();
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
                                            currentAction = nums[currentNumOptionRowIndex][currentNumOptionColIndex];
                                            currentDirection = boxActions[currentNumOptionRowIndex][currentNumOptionColIndex];
                                        }
                                    }
                                } else {
                                    timer.cancel();
                                    toast.setView(tryAgainLayout);
                                    toast.show();
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
                                            currentAction = nums[currentNumOptionRowIndex][currentNumOptionColIndex];
                                            currentDirection = boxActions[currentNumOptionRowIndex][currentNumOptionColIndex];
                                        }
                                    }
                                } else {
                                    timer.cancel();
                                    toast.setView(tryAgainLayout);
                                    toast.show();
                                }
                                break;
                        }
                    }

                    if (currentAction == 0) {
                        if (boxActions[currentNumOptionRowIndex][currentNumOptionColIndex].equals(getString(R.string.target))) {
                            if(optionsUsed == optionsLength) {
                                puzzleSolved = true;
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

                toast.setView(levelCompleteLayout);
                toast.show();
            }
        };

        timer.start();

    }

    public void restartCurrentLevel() {
        alertDialog.dismiss();

        setTargetPosition();

        for(int i = 0; i < boxes.length; i++) {
            for(int j = 0; j < boxes[0].length; j++) {
                boxes[i][j].setText("");
                boxActions[i][j] = "";

                if(!boxActions.equals(getString(R.string.target))) {
                    boxes[i][j].setBackgroundResource(R.drawable.custom_whitecoloredbox);
                    boxes[i][j].setEnabled(false);
                } else {
                    boxes[i][j].setEnabled(true);
                }

                nums[i][j] = -1;
            }
        }

        isNumSelected = false;
        currentNumSelected = -1;
        currentActionSelected = "";
        totalJumps = 0;
        puzzleSolved = false;
        optionsUsed = 0;
        currentNumOptionRowIndex = -1;
        currentNumOptionColIndex = -1;
        previousCurrentNumOptionRowIndex = -1;
        previousCurrentNumOptionColIndex = -1;
        currentActionRowIndex = -1;
        currentActionColIndex = -1;

        levels();
    }

    public void setTargetPosition() {
        boxes[endingY][endingX].setText(getString(R.string.end));
        boxes[endingY][endingX].setEnabled(false);
        boxActions[endingY][endingX] = getString(R.string.target);
    }

    public void levels() {
        boxes[startingY][startingX].setText(getString(R.string.start));

        switch (currentLevel) {
            case 1:                                                     //1
                optionNums[0] = 2;
                options[0].setText(optionNums[0] + "D");
                optionsAction[0] = getString(R.string.down);

                optionNums[1] = 1;
                options[1].setText(optionNums[1] + "R");
                optionsAction[1] = getString(R.string.right);
                break;
            case 2:                                                     //2
                optionNums[0] = 1;
                options[0].setText(optionNums[0] + "R");
                optionsAction[0] = getString(R.string.right);

                optionNums[1] = 1;
                options[1].setText(optionNums[1] + "U");
                optionsAction[1] = getString(R.string.up);
                break;
            case 3:                                                     //3
                optionNums[0] = 1;
                options[0].setText(optionNums[0] + "D");
                optionsAction[0] = getString(R.string.down);

                optionNums[1] = 1;
                options[1].setText(optionNums[1] + "U");
                optionsAction[1] = getString(R.string.up);

                optionNums[2] = 1;
                options[2].setText(optionNums[2] + "L");
                optionsAction[2] = getString(R.string.left);

                optionNums[3] = 1;
                options[3].setText(optionNums[3] + "L");
                optionsAction[3] = getString(R.string.left);
                break;
            case 10:                                                     //10
                boxes[2][0].setText(getString(R.string.start));

                optionNums[0] = 0;
                options[0].setBackgroundResource(R.drawable.custom_graycoloredbox);
                optionsAction[0] = getString(R.string.block);

                optionNums[1] = 9;
                options[1].setText(optionNums[1] + "R");
                optionsAction[1] = getString(R.string.right);

                optionNums[2] = 1;
                options[2].setText(optionNums[2] + "D");
                optionsAction[2] = getString(R.string.down);
                break;
        }
    }

}