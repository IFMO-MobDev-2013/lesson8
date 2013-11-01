package com.example.Suggester;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MyActivity extends Activity {
    public static final String STRUCTURE_INDEXES = "indexes";
    public static final String AUTHORITY_IND = "com.example.loboda.provider";

    private static final Uri MY_CONTENT_URI = Uri.parse("content://" + AUTHORITY_IND);
    public static final Uri MY_URI_NOTES = Uri.withAppendedPath(MY_CONTENT_URI, STRUCTURE_INDEXES);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final ContentResolver resolver = getContentResolver();
        ((EditText)findViewById(R.id.editText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String word = editable.toString();
                Cursor cursor = resolver.query(MY_URI_NOTES, new String[]{"_id", "word"}, "word=?", new String[]{word}, "count asc");
                ((ListView)findViewById(R.id.listView)).setAdapter(new SimpleCursorAdapter(getBaseContext(), R.layout.simple_row,
                        cursor, new String[]{"word"}, new int[]{R.id.textView}, 0));
            }
        });
    }
}
