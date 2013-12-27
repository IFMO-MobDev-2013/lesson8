package com.example.Indexer;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class IndexerActivity extends Activity {
    public static final String STRUCTURE_NOTES = "notes";
    public static final String AUTHORITY = "org.nottingham.provider";

    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri URI_NOTES = Uri.withAppendedPath(CONTENT_URI, STRUCTURE_NOTES);

    private static final String TAG = IndexerActivity.class.getSimpleName();
    private Map<String, Integer> map;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map = new HashMap<>();
        Cursor c = getContentResolver().query(URI_NOTES, null, null, null, null);
        while (c.moveToNext()) {
            for (int i = 0; i < c.getColumnCount(); i++) {
                String note = c.getString(1);
                String[] words = note.split("\\s+");
                for (String word: words) {
                    map.put(word, 0);
                }
                for (String word: words) {
                    if (!map.containsKey(word)) {
                        map.put(word, 0);
                    } else {
                        map.put(word, map.get(word) + 1);
                    }
                }

            }
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry: map.entrySet()) {
            Log.w(TAG, entry.toString());
            final ContentValues values = new ContentValues();

            values.put(IndexerProvider.T_WORDS_COUNT, entry.getValue());
            values.put(IndexerProvider.T_WORDS_WORD, entry.getKey());
            Log.w(TAG, "Inserting into content provider" + "[" + entry.getKey() + "] -> " + entry.getValue());
            sb.append(entry.toString() + "\n");
        }
        Toast.makeText(this, sb, 2000).show();
    }
}