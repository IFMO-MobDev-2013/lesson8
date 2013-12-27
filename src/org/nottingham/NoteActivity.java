package org.nottingham;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class NoteActivity extends Activity {
    private static final String TAG = NoteActivity.class.getSimpleName();
    private long index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        index = 0;

        final EditText text = (EditText) findViewById(R.id.EditText1);
        
        findViewById(R.id.btn_new_note).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String note = text.getText().toString();
                addNote(note);
            }

        });

        findViewById(R.id.btn_next).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = nextNote(true);
                if (note != null) {
                    text.setText(note);
                }
            }
        });

        findViewById(R.id.btn_prev).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = nextNote(false);
                if (note != null) {
                    text.setText(note);
                }

            }
        });
        printData();



    }
    
    private void addNote(String note) {
        final ContentValues values = new ContentValues();
        
        values.put(NotesProvider.T_NOTES_TEXT, note);
        values.put(NotesProvider.T_NOTES_UPDATED, System.currentTimeMillis());
        getContentResolver().insert(NotesProvider.URI_NOTES, values);
        
        printData();
    }

    private String nextNote(boolean direction) {
        try {
            Cursor c = getContentResolver().query(Uri.withAppendedPath(NotesProvider.URI_NOTES, "/" + (direction ? (++index) : (--index))), null, null, null, null);
            if (c.moveToFirst()) {
                return c.getString(1);
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    private void printData() {
        Cursor c = getContentResolver().query(NotesProvider.URI_NOTES, null, null, null, null);
        
        while (c.moveToNext()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < c.getColumnCount(); i++) {
                sb.append("[").append(c.getColumnName(i)).append("] : ")
                    .append(c.getString(i)).append(" ");
            }
            Log.w(TAG, "printData() " + c.getPosition() + ": " + sb.toString());
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.note, menu);
        return true;
    }
    
    

}
