package com.example.android.sudokusolver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.android.sudokusolver.Data.SudokuContract;

public class MainActivity extends AppCompatActivity {

    boolean infoVisible,difficultyVisible;
    FrameLayout infoFrame;
    ImageButton info;
    ImageButton history;
    FrameLayout difficultyLevels;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x=(float)event.getX();
        float y=(float)event.getY();

        if(infoVisible && (x<infoFrame.getX() || y>infoFrame.getY()+infoFrame.getHeight())){
            infoVisible=false;
            infoFrame.setVisibility(View.GONE);
            info.setImageResource(R.drawable.info_bright);
        }

        if(difficultyVisible && (x<difficultyLevels.getX() || x>difficultyLevels.getX()+difficultyLevels.getWidth()
            || y<difficultyLevels.getY() || y>difficultyLevels.getY()+difficultyLevels.getHeight())){
            difficultyVisible=false;
            difficultyLevels.setVisibility(View.GONE);
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            this.getSupportActionBar().hide();
        }catch (NullPointerException e){}

        Button solve=(Button) findViewById(R.id.solve);
        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SolverActivity.class);
                startActivity(intent);
            }
        });

        info=(ImageButton) findViewById(R.id.info);
        infoVisible=false;

        infoFrame=(FrameLayout) findViewById(R.id.fragment_frame);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(infoVisible){
                    infoFrame.setVisibility(View.GONE);
                    infoVisible=false;
                    info.setImageResource(R.drawable.info_bright);
                }else {
                    infoFrame.setVisibility(View.VISIBLE);
                    infoVisible=true;
                    info.setImageResource(R.drawable.info_dark);
                }
            }
        });

        difficultyLevels=(FrameLayout)findViewById(R.id.difficulty_level);
        Button easy=(Button)findViewById(R.id.easy_button);
        Button medium=(Button)findViewById(R.id.medium_button);
        Button hard=(Button)findViewById(R.id.hard_button);
        difficultyVisible=false;

        Button game=(Button) findViewById(R.id.sudoku_game);
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficultyLevels.setVisibility(View.VISIBLE);
                difficultyVisible=true;
            }
        });

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,GameActivity.class);
                difficultyVisible=false;
                difficultyLevels.setVisibility(View.GONE);
                intent.putExtra(SudokuContract.DIFFICULTY_LEVEL,SudokuContract.EASY_LEVEL);
                startActivity(intent);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,GameActivity.class);
                difficultyVisible=false;
                difficultyLevels.setVisibility(View.GONE);
                intent.putExtra(SudokuContract.DIFFICULTY_LEVEL,SudokuContract.MEDIUM_LEVEL);
                startActivity(intent);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,GameActivity.class);
                difficultyVisible=false;
                difficultyLevels.setVisibility(View.GONE);
                intent.putExtra(SudokuContract.DIFFICULTY_LEVEL,SudokuContract.HARD_LEVEL);
                startActivity(intent);
            }
        });

        history=(ImageButton)findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
