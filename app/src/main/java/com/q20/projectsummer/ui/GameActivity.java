package com.q20.projectsummer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.q20.projectsummer.Custom.CustomActivity;
import com.q20.projectsummer.R;

import java.nio.BufferUnderflowException;
import java.util.Random;

/**
 * Created by Alireza Arvandi on 10/11/2016.
 */

public class GameActivity extends CustomActivity implements View.OnClickListener {

    private int maxRow = 3;
    private int maxColumn = 11;


  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


    }

    @Override
    public void onClick(View v) {

    }

    private void creatKeyboard(){
        RelativeLayout relativeLayout[];
    }

}
