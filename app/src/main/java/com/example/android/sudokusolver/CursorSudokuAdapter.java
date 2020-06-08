package com.example.android.sudokusolver;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.sudokusolver.Data.SudokuContract;

public class CursorSudokuAdapter extends CursorAdapter {
    public CursorSudokuAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.history_list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        StringBuffer board=new StringBuffer(cursor.getString(cursor.getColumnIndex(SudokuContract.BOARD)));
        int status=cursor.getInt(cursor.getColumnIndex(SudokuContract.STATUS));
        int wrong=cursor.getInt(cursor.getColumnIndex(SudokuContract.WRONG_COUNT));
        int hint=cursor.getInt(cursor.getColumnIndex(SudokuContract.HINT_COUNT));
        int difficulty=cursor.getInt(cursor.getColumnIndex(SudokuContract.DIFFICULTY));

        SudokuGrid sudokuGrid=(SudokuGrid)view.findViewById(R.id.sudoku_grid);
        sudokuGrid.setBoard(board);

        TextView statusText=(TextView)view.findViewById(R.id.status);
        if(status==SudokuContract.STATUS_SUCCESS)
            statusText.setText(context.getResources().getString(R.string.status,"Success"));
        else if(status==SudokuContract.STATUS_FAILED)
            statusText.setText(context.getResources().getString(R.string.status,"Failed"));
        else
            statusText.setText(context.getResources().getString(R.string.status,"Tired"));

        TextView wrongText=(TextView)view.findViewById(R.id.wrong);
        wrongText.setText(context.getResources().getString(R.string.wrongCount,wrong));

        TextView hintText=(TextView)view.findViewById(R.id.hint);
        hintText.setText(context.getResources().getString(R.string.hintCount,hint));

        TextView difficultyText=(TextView)view.findViewById(R.id.difficulty_level);
        if(difficulty==SudokuContract.DIFFICULTY_EASY)
            difficultyText.setText(context.getResources().getString(R.string.difficulty_level,"Easy"));
        else if(difficulty==SudokuContract.DIFFICULTY_MEDIUM)
            difficultyText.setText(context.getResources().getString(R.string.difficulty_level,"Medium"));
        else
            difficultyText.setText(context.getResources().getString(R.string.difficulty_level,"Hard"));
    }
}
