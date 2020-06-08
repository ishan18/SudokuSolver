package com.example.android.sudokusolver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SolverActivity extends AppCompatActivity {

    SudokuGrid sudokuGrid;
    NumberPanel numberPanel;
    Button solve;
    Button hint;
    FrameLayout solveCard;
    ImageView angry,solved;
    ImageButton eraser;
    TextView noSolution;
    private int mCurrentPosition;
    private int hintsUsed;
    StringBuffer currentBoard;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float x=(float) event.getX()-sudokuGrid.getX();
        float y=(float) event.getY()-sudokuGrid.getY()-2*sudokuGrid.getCellSize();
        if(x>0 && y>0 && x<sudokuGrid.getCellSize()*9 && y<sudokuGrid.getCellSize()*9)
        {
            int pos=sudokuGrid.getPosition(x,y);
            if(pos<0)
                return true;
            mCurrentPosition=pos;
            sudokuGrid.setCurrentSelected(mCurrentPosition);
        }
        x=(float) event.getX()-numberPanel.getX();
        y=(float) event.getY()-numberPanel.getY()-2*sudokuGrid.getCellSize();
        if(x>0 && y>0 && x<numberPanel.getPadSize()*9 && y<numberPanel.getPadSize()){
            int pos=numberPanel.getPosition(x);
            if(pos<0)
                return true;
            pos++;
            StringBuffer stringBuffer=new StringBuffer(currentBoard);
            stringBuffer.setCharAt(mCurrentPosition, (char) (pos+'0'));
            SolveSudoku solveSudoku=new SolveSudoku(stringBuffer.toString());
            if(solveSudoku.solve()!=null){
                if(sudokuGrid.setValue(mCurrentPosition,pos))
                    currentBoard.setCharAt(mCurrentPosition, (char) (pos+'0'));
            }
            else {
                if(sudokuGrid.setWrongValue(mCurrentPosition,pos))
                    currentBoard.setCharAt(mCurrentPosition, (char) (pos+'0'));
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_solver_activity,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solver);

        sudokuGrid=(SudokuGrid) findViewById(R.id.sudoku_grid);
        numberPanel=(NumberPanel) findViewById(R.id.number_panel);
        mCurrentPosition=0;

        hintsUsed=0;

        currentBoard=new StringBuffer();
        for(int i=0;i<81;i++)
            currentBoard.append(' ');

        solve=(Button) findViewById(R.id.solve_it);

        solveCard=(FrameLayout) findViewById(R.id.solve_card);
        angry=(ImageView) findViewById(R.id.image_angry);
        noSolution=(TextView)findViewById(R.id.text_angry);
        solved=(ImageView)findViewById(R.id.image_solved);

        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SolveSudoku solveSudoku=new SolveSudoku(sudokuGrid.getBoard().toString());
                StringBuffer solution=solveSudoku.solve();

                numberPanel.setVisibility(View.GONE);
                solveCard.setVisibility(View.GONE);
                eraser.setVisibility(View.GONE);
                if(solution==null){
                    sudokuGrid.setVisibility(View.GONE);
                    angry.setVisibility(View.VISIBLE);
                    noSolution.setVisibility(View.VISIBLE);
                    return;
                }
                sudokuGrid.setBoard(solution);
                solved.setVisibility(View.VISIBLE);
            }
        });

        eraser=(ImageButton)findViewById(R.id.eraser);

        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sudokuGrid.eraseValue(mCurrentPosition))
                    currentBoard.setCharAt(mCurrentPosition,' ');
            }
        });

        hint=(Button) findViewById(R.id.hint_button);
        hint.setText(getResources().getString(R.string.hint,hintsUsed));
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hintsUsed>=3)
                    return;
                SolveSudoku solveSudoku=new SolveSudoku(sudokuGrid.getBoard().toString());
                StringBuffer solution=solveSudoku.solve();

                if(solution==null){
                    Toast.makeText(SolverActivity.this,"Invalid Sudoku Board",Toast.LENGTH_SHORT).show();
                    return;
                }
                char hintChar=solution.charAt(mCurrentPosition);
                if(sudokuGrid.setValue(mCurrentPosition,hintChar-'0')){
                    currentBoard.setCharAt(mCurrentPosition,hintChar);
                    hintsUsed++;
                    hint.setText(getResources().getString(R.string.hint,hintsUsed));
                }
            }
        });
    }
}