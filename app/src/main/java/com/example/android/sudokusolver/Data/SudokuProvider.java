package com.example.android.sudokusolver.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SudokuProvider extends ContentProvider {

    SudokuDB sudokuDB;
    @Override
    public boolean onCreate() {
        sudokuDB=new SudokuDB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db=sudokuDB.getReadableDatabase();

        return db.query(SudokuContract.TABLE_NAME,null,selection,selectionArgs,null,null,SudokuContract.ID+" DESC");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db=sudokuDB.getWritableDatabase();

        long id=db.insert(SudokuContract.TABLE_NAME,null,values);
        if(id!=-1)
            return ContentUris.withAppendedId(uri,id);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
