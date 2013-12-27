package com.example.Suggester;

import android.app.Activity;
import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    public static final String STRUCTURE_NOTES = "words";
    public static final String AUTHORITY = "com.example.provider";

    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri URI_INDEXER = Uri.withAppendedPath(CONTENT_URI, STRUCTURE_NOTES);
    private SimpleCursorAdapter adapter;
    private ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listView = (ListView)findViewById(R.id.listView);
        String[] from = new String[]{"word"};
        int[] to = new int[]{R.id.text1};
        adapter = new SimpleCursorAdapter(this, R.layout.main, getContentResolver().query(URI_INDEXER, null, null, null, null), from, to);
        listView.setAdapter(adapter);

    }
}
