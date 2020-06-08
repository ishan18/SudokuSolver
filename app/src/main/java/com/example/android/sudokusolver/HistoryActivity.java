package com.example.android.sudokusolver;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.sudokusolver.Data.SudokuContract;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    CursorSudokuAdapter cursorSudokuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        try {
            this.getSupportActionBar().setTitle(getResources().getString(R.string.history));
        }catch (NullPointerException ignored){}

        listView=(ListView) findViewById(R.id.history_list);
        cursorSudokuAdapter=new CursorSudokuAdapter(this,null);
        listView.setAdapter(cursorSudokuAdapter);
        listView.setEmptyView((TextView)findViewById(R.id.empty_view));

        AsyncSudokuHistory asyncSudokuHistory=new AsyncSudokuHistory();
        asyncSudokuHistory.execute(SudokuContract.CONTENT_URI);
    }

    private class AsyncSudokuHistory extends AsyncTask<Uri,Void, Cursor>{

        @Override
        protected void onPostExecute(Cursor cursor) {
            cursorSudokuAdapter.swapCursor(cursor);
        }

        @Override
        protected Cursor doInBackground(Uri... uris) {

            Uri uri=uris[0];

            return getContentResolver().query(uri,null,null,null,null);
        }
    }
}
