package com.example.Lesson8Indexer;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class IndexerActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private TextView successView;
    private TextView informationView;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private Hashtable<String, Integer> dictionary;

    public static final String STRUCTURE_NOTES = "notes";
    public static final String AUTHORITY = "org.nottingham.provider";

    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri URI_NOTES = Uri.withAppendedPath(CONTENT_URI, STRUCTURE_NOTES);

    private static final String  T_NOTES = "Notes";

    public static final String T_NOTES_ID = "note_id";
    public static final String T_NOTES_TEXT = "text";
    public static final String T_NOTES_UPDATED = "updated";




    public void onIndexing(View view) {
        successView.setText("Indexing...");
        informationView.setText("Please, wait for results.");
        try {
            int notesCount = 0;
            int wordsCount = 0;
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(URI_NOTES, null, null, null, null);
            String s;
            String[] ss;
            StringBuilder sb;
            char c;
            dictionary = new Hashtable<String, Integer>();
            int textColumn = cursor.getColumnIndex(T_NOTES_TEXT);
            while (cursor.moveToNext()) {
                notesCount++;
                s = cursor.getString(textColumn);
                sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    c = s.charAt(i);
                    if (((c <= 'z') && (c >= 'a')) || ((c <= 'Z') && (c >= 'A')) || ((c <= '9') && (c >= '0'))) {
                        sb.append(c);
                    } else {
                        sb.append(" ");
                    }
                }
                ss = sb.toString().split("\\s");
                for (int i = 0; i < ss.length; i++) {
                    if (!ss[i].isEmpty()) {
                        ss[i] = ss[i].toLowerCase();
                        if (!dictionary.containsKey(ss[i])) {
                            dictionary.put(ss[i], 1);
                            wordsCount++;
                        } else {
                            Integer integer = dictionary.get(ss[i]);
                            dictionary.put(ss[i], integer + 1);
                        }
                    }
                }
            }
            cursor.close();
            Enumeration<String> enumeration = dictionary.keys();
            int min = -1;
            int max = -1;
            String maxWord = "NO_SUCH_WORD";
            String minWord = "NO_SUCH_WORD";
            while (enumeration.hasMoreElements()) {
                s = enumeration.nextElement();
                Integer integer = dictionary.get(s);
                if ((max == -1) || (max < integer)) {
                    max = integer;
                    maxWord = s;
                }
                if ((min == -1) || (min > integer)) {
                    min = integer;
                    minWord = s;
                }
                adapter.add("Word = \"" + s + "\", count = " + integer + ".");
                adapter.notifyDataSetChanged();
            }
            Log.w("maxWord", "Word with max count is \"" + maxWord + "\", count = " + max + ".");
            Log.w("minWord", "Word with min count is \"" + minWord + "\", count = " + min + ".");
            successView.setText("The notes has been indexed.");
            informationView.setText(notesCount + " notes have been indexed, " + wordsCount + " different words in these notes.");
        } catch (Exception e) {
            successView.setText("Bad database.");
            informationView.setText(e.getMessage());
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        successView = (TextView) findViewById(R.id.success);
        successView.setText("Ready to start indexing.");
        successView.setTextSize(20);
        informationView = (TextView) findViewById(R.id.information);
        informationView.setText("No notes has been indexed by now.");
        informationView.setTextSize(20);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, R.layout.text);
        listView.setAdapter(adapter);
    }
}
