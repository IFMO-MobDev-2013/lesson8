package com.example.Indexer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyActivity extends Activity {
    public static final String STRUCTURE_NOTES = "notes";
    public static final String AUTHORITY = "org.nottingham.provider";

    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri URI_NOTES = Uri.withAppendedPath(CONTENT_URI, STRUCTURE_NOTES);

    public static final String T_NOTES_TEXT = "text";

    public static final String STRUCTURE_INDEXES = "indexes";
    public static final String AUTHORITY_IND = "com.example.loboda.provider";

    private static final Uri MY_CONTENT_URI = Uri.parse("content://" + AUTHORITY_IND);
    public static final Uri MY_URI_NOTES = Uri.withAppendedPath(MY_CONTENT_URI, STRUCTURE_INDEXES);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Cursor cursor = getContentResolver().query(URI_NOTES, new String[]{T_NOTES_TEXT}, null, null,null, null);
        Map<String, Integer> words = new HashMap<String, Integer>();
        while(cursor.moveToNext()){
            String text = cursor.getString(cursor.getColumnIndex(T_NOTES_TEXT));
            Pattern pattern = Pattern.compile("([a-zA-Z]+)");
            Matcher matcher = pattern.matcher(text);
            while(matcher.find()){
                String word = matcher.group(1);
                Integer cnt = words.get(word);
                if(cnt == null){
                    cnt = 0;
                }
                ++cnt;
                words.put(word, cnt);
            }
        }
        Set<String> keySet = words.keySet();
        Iterator<String> it = keySet.iterator();
        ContentResolver resolver = getContentResolver();

        resolver.delete(MY_URI_NOTES, null, null);
        while(it.hasNext()){
            String key = it.next();
            ContentValues values = new ContentValues();
            values.put("word", key);
            values.put("count", words.get(key));
            resolver.insert(MY_URI_NOTES, values);
        }
        Cursor records = resolver.query(MY_URI_NOTES, null, null, null, null);
        Toast.makeText(getApplicationContext(), "All indexed", Toast.LENGTH_LONG).show();
        while(records.moveToNext()){
            Log.w("records", records.getString(records.getColumnIndex("word")) + ": " + records.getInt(records.getColumnIndex("count")));
        }
    }
}
