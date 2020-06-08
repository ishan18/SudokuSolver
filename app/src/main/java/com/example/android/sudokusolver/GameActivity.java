package com.example.android.sudokusolver;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.android.sudokusolver.Data.SudokuContract;

import java.util.ArrayList;
import java.util.Collections;


public class GameActivity extends AppCompatActivity {

    SudokuGrid sudokuGrid;
    NumberPanel numberPanel;
    int mCurrentPosition;
    ImageButton eraser;
    Button hint;
    ArrayList<Integer> values;
    ArrayList<Integer> position;
    StringBuffer board,currentBoard;
    ImageView solvedImage;
    ConstraintLayout hintCard;
    private int hintsUsed;
    private int wrong;
    TextView wrongCount;
    boolean tired=false;
    ImageView angry;
    TextView angryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        try {
            this.getSupportActionBar().setTitle(getResources().getString(R.string.solve_sudoku));
        }catch (NullPointerException ignored){}

        sudokuGrid=(SudokuGrid) findViewById(R.id.sudoku_grid);
        numberPanel=(NumberPanel) findViewById(R.id.number_panel);
        mCurrentPosition=0;

        angry=(ImageView)findViewById(R.id.image_angry);
        angryText=(TextView)findViewById(R.id.text_angry);

        hintsUsed=0;
        wrong=0;

        wrongCount=(TextView)findViewById(R.id.wrong);
        wrongCount.setText(getResources().getString(R.string.wrong,wrong));

        Button giveUp=(Button)findViewById(R.id.give_up);
        giveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudokuGrid.setBoard(board);
                tired=true;
                onCompleted();
            }
        });

        solvedImage=(ImageView)findViewById(R.id.image_solved);
        hintCard=(ConstraintLayout)findViewById(R.id.hint_card);

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
                char hintChar=board.charAt(mCurrentPosition);
                if(sudokuGrid.setValue(mCurrentPosition,hintChar-'0')){
                    currentBoard.setCharAt(mCurrentPosition,hintChar);
                    hintsUsed++;
                    hint.setText(getResources().getString(R.string.hint,hintsUsed));
                }
                if(checkComplete())
                    onCompleted();
            }
        });

        values=new ArrayList<>();
        for(int i=0;i<9;i++)
            values.add(i+1);
        position=new ArrayList<>();
        for(int i=0;i<81;i++)
            position.add(i);

        Collections.shuffle(values);
        board=new StringBuffer();
        for(int i=0;i<81;i++)
            board.append(' ');
        int x=0;
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++){
                board.setCharAt(i*9+j, (char) (values.get(x)+'0'));
                x++;
            }
        Collections.shuffle(values);x=0;
        for(int i=3;i<6;i++)
            for(int j=3;j<6;j++){
                board.setCharAt(i*9+j, (char) (values.get(x)+'0'));
                x++;
            }
        Collections.shuffle(values);x=0;
        for(int i=6;i<9;i++)
            for(int j=6;j<9;j++){
                board.setCharAt(i*9+j, (char) (values.get(x)+'0'));
                x++;
            }
        SolveSudoku solveSudoku=new SolveSudoku(board.toString());
        board=solveSudoku.solve();

        currentBoard=new StringBuffer(board);
        Collections.shuffle(position);

        int blanks=solveSudoku.countBlank(position,board);
        int level=blanks-getIntent().getIntExtra(SudokuContract.DIFFICULTY_LEVEL,SudokuContract.EASY_LEVEL)+20;
        for(int i=0;i<level;i++)
            currentBoard.setCharAt(position.get(i),' ');
        sudokuGrid.setBoard(currentBoard);
    }

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
            if(board.toString().charAt(mCurrentPosition)==(char)(pos+'0')){
                if(sudokuGrid.setValue(mCurrentPosition,pos))
                    currentBoard.setCharAt(mCurrentPosition, (char) (pos+'0'));

                if(checkComplete())
                    onCompleted();
            }
            else{
                if(sudokuGrid.setWrongValue(mCurrentPosition,pos)){
                    currentBoard.setCharAt(mCurrentPosition,(char)(pos+'0'));
                    wrong++;
                    wrongCount.setText(getResources().getString(R.string.wrong,wrong));
                    if(wrong>=3){
                        wrongTerminated();
                    }
                }
            }
        }
        return true;
    }

    private void wrongTerminated() {
        ContentValues cv=new ContentValues();
        cv.put(SudokuContract.BOARD,board.toString());
        cv.put(SudokuContract.STATUS,SudokuContract.STATUS_FAILED);
        cv.put(SudokuContract.WRONG_COUNT,wrong);
        cv.put(SudokuContract.HINT_COUNT,hintsUsed);
        int difficulty=getIntent().getIntExtra(SudokuContract.DIFFICULTY_LEVEL,SudokuContract.EASY_LEVEL);
        if(difficulty==SudokuContract.EASY_LEVEL)
            cv.put(SudokuContract.DIFFICULTY,SudokuContract.DIFFICULTY_EASY);
        else if(difficulty==SudokuContract.MEDIUM_LEVEL)
            cv.put(SudokuContract.DIFFICULTY,SudokuContract.DIFFICULTY_MEDIUM);
        else
            cv.put(SudokuContract.DIFFICULTY,SudokuContract.DIFFICULTY_HARD);

        getContentResolver().insert(SudokuContract.BASE_CONTENT_URI,cv);

        eraser.setVisibility(View.GONE);
        numberPanel.setVisibility(View.GONE);
        hintCard.setVisibility(View.GONE);
        sudokuGrid.setVisibility(View.GONE);
        angryText.setVisibility(View.VISIBLE);
        angry.setVisibility(View.VISIBLE);
    }

    private boolean checkComplete(){
        return sudokuGrid.checkComplete();
    }

    private void onCompleted() {
        eraser.setVisibility(View.GONE);
        numberPanel.setVisibility(View.GONE);
        hintCard.setVisibility(View.GONE);
        solvedImage.setVisibility(View.VISIBLE);

        ContentValues cv=new ContentValues();
        cv.put(SudokuContract.BOARD,board.toString());
        if(tired)
            cv.put(SudokuContract.STATUS,SudokuContract.STATUS_TIRED);
        else
            cv.put(SudokuContract.STATUS,SudokuContract.STATUS_SUCCESS);
        cv.put(SudokuContract.WRONG_COUNT,wrong);
        cv.put(SudokuContract.HINT_COUNT,hintsUsed);
        int difficulty=getIntent().getIntExtra(SudokuContract.DIFFICULTY_LEVEL,SudokuContract.EASY_LEVEL);
        if(difficulty==SudokuContract.EASY_LEVEL)
            cv.put(SudokuContract.DIFFICULTY,SudokuContract.DIFFICULTY_EASY);
        else if(difficulty==SudokuContract.MEDIUM_LEVEL)
            cv.put(SudokuContract.DIFFICULTY,SudokuContract.DIFFICULTY_MEDIUM);
        else
            cv.put(SudokuContract.DIFFICULTY,SudokuContract.DIFFICULTY_HARD);

        getContentResolver().insert(SudokuContract.BASE_CONTENT_URI,cv);
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
}
