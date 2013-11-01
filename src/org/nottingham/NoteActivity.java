package org.nottingham;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class NoteActivity extends Activity {
    private static final String TAG = NoteActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        final EditText text = (EditText) findViewById(R.id.EditText1);
        
        findViewById(R.id.btn_new_note).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String note = text.getText().toString();
                addNote(note);
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
