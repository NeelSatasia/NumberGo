package com.puzzlegame.numbergo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;

public class Play extends AppCompatActivity {

    RelativeLayout relLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        relLay = new RelativeLayout(this);

    }
}