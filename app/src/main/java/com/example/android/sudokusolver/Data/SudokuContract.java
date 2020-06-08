package com.example.android.sudokusolver.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class SudokuContract implements BaseColumns {

    public static final int EASY_LEVEL=10;
    public static final int MEDIUM_LEVEL=5;
    public static final int HARD_LEVEL=0;
    public static final String DIFFICULTY_LEVEL="level";

    public static final String TABLE_NAME="SudokuGrid";
    public static final String ID=BaseColumns._ID;
    public static final String BOARD="board";
    public static final String STATUS="status";
    public static final String WRONG_COUNT="wrongCount";
    public static final String HINT_COUNT="hintCount";
    public static final String DIFFICULTY="difficulty";

    public static final int DIFFICULTY_EASY=0;
    public static final int DIFFICULTY_MEDIUM=1;
    public static final int DIFFICULTY_HARD=2;
    public static final int STATUS_FAILED=0;
    public static final int STATUS_SUCCESS=1;
    public static final int STATUS_TIRED=2;

    public static final String TABLE_CREATE="CREATE TABLE "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            BOARD+" TEXT, "+STATUS+" INTEGER, "+WRONG_COUNT+" INTEGER, "+DIFFICULTY+" INTEGER, "+HINT_COUNT+" INTEGER);";

    public static final String CONTENT_AUTHORITY="com.example.android.sudokusolver";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,SudokuContract.TABLE_NAME);
}
